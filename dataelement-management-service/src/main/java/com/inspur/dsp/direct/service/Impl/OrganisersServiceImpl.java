package com.inspur.dsp.direct.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dao.DomainDataElementMapper;
import com.inspur.dsp.direct.dao.NegotiationRecordMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.bo.DomainSourceUnitInfo;
import com.inspur.dsp.direct.entity.dto.AddNegotiationDto;
import com.inspur.dsp.direct.entity.dto.GetDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.ManualSourceDto;
import com.inspur.dsp.direct.entity.dto.NegotiationObj;
import com.inspur.dsp.direct.entity.dto.VerifiedDataSourceDto;
import com.inspur.dsp.direct.entity.excel.DataElementExcel;
import com.inspur.dsp.direct.entity.excel.PsDataElementExcel;
import com.inspur.dsp.direct.entity.vo.DataElementInfoVo;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.SelectSourceUnitVo;
import com.inspur.dsp.direct.entity.vo.SourceRequestList;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.NodeStatusEnums;
import com.inspur.dsp.direct.enums.SortFieldEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TaskTypeEnums;
import com.inspur.dsp.direct.service.OrganisersService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganisersServiceImpl implements OrganisersService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final DomainDataElementMapper domainDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;
    private final NegotiationRecordMapper negotiationTaskMapper;
    private final SourceEventRecordMapper sourceEventRecordMapper;

    /**
     * 组织方获取数据元分页列表
     *
     * @param dto
     * @return
     */
    @Override
    public Page<DataElementPageInfoVo> getDataElementPage(GetDataElementPageDto dto) {
        // 获取排序sql
        String sortSql = SortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
        Page<DataElementPageInfoVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<DataElementPageInfoVo> list = baseDataElementMapper.getDataElementPage(page, dto, sortSql);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(vo -> {
                vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
            });
        }
        return page.setRecords(list);
    }

    /**
     * 批量发起定源, 限制仅可以选择有唯一采集单位的数据
     *
     * @param deIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInitiateSource(List<String> deIds) {
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取数据元来源单位信息
        List<DomainSourceUnitInfo> domainSourceUnitInfos = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(deIds);
        if (CollectionUtils.isEmpty(domainSourceUnitInfos)) {
            throw new IllegalArgumentException("请选择有效基准数据元!");
        }
        // 数据转换为Map<baseDataId, DomainSourceUnitInfo>, Map<基准数据元唯一标识, 提供单位对象>
        Map<String, DomainSourceUnitInfo> domainSourceUnitInfoMap = domainSourceUnitInfos.stream()
                .collect(Collectors
                        .toMap(DomainSourceUnitInfo::getBaseDataelementDataid
                                , domainSourceUnitInfo -> domainSourceUnitInfo
                                , (oldValue, newValue) -> oldValue
                        ));
        // 更新基准数据元信息
        List<BaseDataElement> updateBaseElements = new ArrayList<>();
        // 新增确认任务
        List<ConfirmationTask> confirmationTasks = new ArrayList<>();
        // 遍历map,构建确认任务列表
        domainSourceUnitInfoMap.forEach((deId, info) -> {
            if (Objects.nonNull(info)) {
                // 发起定源时间
                Date date = new Date();
                ConfirmationTask confirmationTask = new ConfirmationTask();
                confirmationTask.setTaskId(UUID.randomUUID().toString());
                confirmationTask.setBaseDataelementDataid(deId);
                confirmationTask.setSendDate(date);
                confirmationTask.setSenderAccount(userInfo.getAccount());
                confirmationTask.setSenderName(userInfo.getName());
                // 待确认
                confirmationTask.setTasktype(TaskTypeEnums.CONFIRMATION_TASK.getCode());
                confirmationTask.setStatus(ConfirmationTaskEnums.PENDING.getCode());
                confirmationTask.setProcessingUnitCode(info.getSourceUnitCode());
                confirmationTask.setProcessingUnitName(info.getSourceUnitName());
                confirmationTask.setSendUnitCode(userInfo.getOrgCode());
                confirmationTask.setSendUnitName(userInfo.getOrgName());
                confirmationTasks.add(confirmationTask);
                // 更新相关基准数据元的发起定源时间,状态为确认中
                BaseDataElement baseDataElement = new BaseDataElement();
                baseDataElement.setDataid(deId);
                baseDataElement.setSendDate(date);
                baseDataElement.setStatus(StatusEnums.CONFIRMING.getCode());
                baseDataElement.setLastModifyAccount(userInfo.getAccount());
                baseDataElement.setLastModifyDate(date);
                updateBaseElements.add(baseDataElement);
            }
        });
        // 批量更新数据元信息
        baseDataElementMapper.updateById(updateBaseElements);
        // 批量新增确认任务
        confirmationTaskMapper.insert(confirmationTasks);
    }

    private void buildSourceRequestList(List<SourceRequestList> sourceRequestLists,
                                        ConfirmationTask confirmationTask,
                                        BaseDataElement baseDataElement,
                                        String dataid) {
        if (confirmationTask.getStatus().equals(StatusEnums.PENDING_NEGOTIATION.getCode())) {
            // 被拒绝的情况：四个节点
            buildRejectedSourceRequestList(sourceRequestLists, confirmationTask, baseDataElement, dataid);
        } else {
            // 正常情况：三个节点
            buildNormalSourceRequestList(sourceRequestLists, confirmationTask, baseDataElement);
        }
    }

    private void buildRejectedSourceRequestList(List<SourceRequestList> sourceRequestLists,
                                                ConfirmationTask confirmationTask,
                                                BaseDataElement baseDataElement,
                                                String dataid) {
        // 第一个节点: 发起定源(已完成)
        sourceRequestLists.add(createSourceRequestListNode("发起定源",
                NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                confirmationTask.getSenderName(),
                confirmationTask.getSendDate(),
                null,
                null));

        // 第二个节点: 采集单位确认(已完成，但被拒绝)
        sourceRequestLists.add(createSourceRequestListNode("采集单位确认",
                NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                confirmationTask.getProcessingUnitName(),
                confirmationTask.getProcessingDate(),
                confirmationTask.getProcessingResult(),
                confirmationTask.getProcessingOpinion()));

        // 第三个节点: 发起协商
        NegotiationRecord negotiationRecord = negotiationTaskMapper.selectFirstByBaseDataelementDataid(dataid);
        SourceRequestList negotiationNode = new SourceRequestList();
        negotiationNode.setNodeName("发起协商");
        if (Objects.nonNull(negotiationRecord)) {
            negotiationNode.setNodeFeedback(null);
            negotiationNode.setNodeHandleUserName(negotiationRecord.getSenderName());
            negotiationNode.setNodeResult(null);
            negotiationNode.setPassDate(negotiationRecord.getSendDate());
            negotiationNode.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
        } else {
            negotiationNode.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
        }
        sourceRequestLists.add(negotiationNode);

        // 第四个节点: 手动定源
        SourceRequestList manualSourceNode = new SourceRequestList();
        manualSourceNode.setNodeName("手动定源");
        if (confirmationTask.getStatus().equals(StatusEnums.NEGOTIATING.getCode())) {
            manualSourceNode.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
        } else if (confirmationTask.getStatus().equals(StatusEnums.DESIGNATED_SOURCE.getCode())) {
            manualSourceNode.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
            manualSourceNode.setNodeHandleUserName(baseDataElement.getLastModifyAccount());
            manualSourceNode.setPassDate(baseDataElement.getLastModifyDate());
        }
        sourceRequestLists.add(manualSourceNode);
    }

    private void buildNormalSourceRequestList(List<SourceRequestList> sourceRequestLists,
                                              ConfirmationTask confirmationTask,
                                              BaseDataElement baseDataElement) {
        String status = confirmationTask.getStatus();

        if (status.equals(StatusEnums.PENDING_SOURCE.getCode())) {
            // 待定源
            sourceRequestLists.add(createSourceRequestListNode("发起定源", NodeStatusEnums.HANDLING.getStatus()));
            sourceRequestLists.add(createSourceRequestListNode("采集单位确认", NodeStatusEnums.WAIT_HANDLE.getStatus()));
            sourceRequestLists.add(createSourceRequestListNode("核定数源单位", NodeStatusEnums.WAIT_HANDLE.getStatus()));
        } else if (status.equals(StatusEnums.CONFIRMING.getCode())) {
            // 确认中
            sourceRequestLists.add(createSourceRequestListNode("发起定源",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    confirmationTask.getSenderName(),
                    confirmationTask.getSendDate()));
            sourceRequestLists.add(createSourceRequestListNode("采集单位确认", NodeStatusEnums.HANDLE_COMPLETE.getStatus()));
            sourceRequestLists.add(createSourceRequestListNode("核定数源单位", NodeStatusEnums.WAIT_HANDLE.getStatus()));
        } else if (status.equals(StatusEnums.PENDING_APPROVAL.getCode())) {
            // 待核定
            sourceRequestLists.add(createSourceRequestListNode("发起定源",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    confirmationTask.getSenderName(),
                    confirmationTask.getSendDate()));
            sourceRequestLists.add(createSourceRequestListNode("采集单位确认",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    confirmationTask.getProcessingUnitName(),
                    confirmationTask.getProcessingDate(),
                    confirmationTask.getProcessingResult()));
            sourceRequestLists.add(createSourceRequestListNode("核定数源单位", NodeStatusEnums.HANDLING.getStatus()));
        } else if (status.equals(StatusEnums.DESIGNATED_SOURCE.getCode())) {
            // 已定源
            sourceRequestLists.add(createSourceRequestListNode("发起定源",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    confirmationTask.getSenderName(),
                    confirmationTask.getSendDate()));
            sourceRequestLists.add(createSourceRequestListNode("采集单位确认",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    confirmationTask.getProcessingUnitName(),
                    confirmationTask.getProcessingDate(),
                    confirmationTask.getProcessingResult()));
            sourceRequestLists.add(createSourceRequestListNode("核定数源单位",
                    NodeStatusEnums.HANDLE_COMPLETE.getStatus(),
                    baseDataElement.getLastModifyAccount(),
                    baseDataElement.getLastModifyDate()));
        }
    }

    private SourceRequestList createSourceRequestListNode(String nodeName, String status) {
        SourceRequestList node = new SourceRequestList();
        node.setNodeName(nodeName);
        node.setNodeShowStatus(status);
        return node;
    }

    private SourceRequestList createSourceRequestListNode(String nodeName, String status,
                                                          String handleUserName, Date passDate) {
        SourceRequestList node = createSourceRequestListNode(nodeName, status);
        node.setNodeHandleUserName(handleUserName);
        node.setPassDate(passDate);
        return node;
    }

    private SourceRequestList createSourceRequestListNode(String nodeName, String status,
                                                          String handleUserName, Date passDate,
                                                          String result) {
        SourceRequestList node = createSourceRequestListNode(nodeName, status, handleUserName, passDate);
        node.setNodeResult(result);
        return node;
    }

    private SourceRequestList createSourceRequestListNode(String nodeName, String status,
                                                          String handleUserName, Date passDate,
                                                          String result, String feedback) {
        SourceRequestList node = createSourceRequestListNode(nodeName, status, handleUserName, passDate, result);
        node.setNodeFeedback(feedback);
        return node;
    }

    /**
     * 导出数据元
     *
     * @param dto
     * @param response
     */
    @Override
    public void exportDataElement(GetDataElementPageDto dto, HttpServletResponse response) {
        // 获取排序sql
        String sortSql = SortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
        // 获取数据元数据,不分页的
        List<DataElementPageInfoVo> list = baseDataElementMapper.getDataElementPage(dto, sortSql);
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("没有可导出的数据!");
        }
        // 设置响应头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 根据状态设置文件名
        String baseFileName = "数据元列表";
        if (dto.getStatusList().contains(StatusEnums.PENDING_SOURCE.getCode())) {
            baseFileName = "待定源数据元列表";
        } else {
            baseFileName = "确认中数据元列表";
        }
        try {
            String fileName = java.net.URLEncoder.encode(baseFileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        } catch (Exception e) {
            log.error("设置文件名失败", e);
            throw new RuntimeException("设置文件名失败", e);
        }
        if (dto.getStatusList().contains(StatusEnums.PENDING_SOURCE.getCode())) {
            // 转换成Excel数据实体
            List<PsDataElementExcel> psExcelList = list.stream().map(PsDataElementExcel::toExcel).collect(Collectors.toList());
            // 使用easyexcel导出数据
            try {
                EasyExcel.write(response.getOutputStream(), PsDataElementExcel.class)
                        .sheet("数据元列表")
                        .doWrite(psExcelList);
            } catch (IOException e) {
                log.error("导出数据元失败", e);
                throw new RuntimeException("导出数据元失败", e);
            }
        } else {
            // 转换成Excel数据实体
            List<DataElementExcel> excelList = list.stream().map(DataElementExcel::toExcel).collect(Collectors.toList());
            // 使用easyexcel导出数据
            try {
                EasyExcel.write(response.getOutputStream(), DataElementExcel.class)
                        .sheet("数据元列表")
                        .doWrite(excelList);
            } catch (IOException e) {
                log.error("导出数据元失败", e);
                throw new RuntimeException("导出数据元失败", e);
            }
        }
    }

    /**
     * 组织方发起定源协商
     *
     * @param dto
     */
    @Override
    public void initiateNegotiation(AddNegotiationDto dto) {
        String dataid = dto.getDataid();
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        List<NegotiationObj> negotiationUnitCodes = dto.getNegotiationUnitCodes();
        if (CollectionUtils.isEmpty(negotiationUnitCodes)) {
            throw new IllegalArgumentException("请选择要协商的数源单位!");
        }
        // 判断数据元是否是已拒绝
//        if (!StatusEnums.REJECTED.getCode().equals(baseDataElement.getStatus())) {
//            throw new IllegalArgumentException("数据元不是已拒绝状态!");
//        }
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 新建协商任务
        NegotiationRecord record = new NegotiationRecord();
        record.setRecordId(UUID.randomUUID().toString());
        record.setBaseDataelementDataid(dataid);
        record.setSendDate(new Date());
        record.setSenderAccount(userInfo.getAccount());
        record.setSenderName(userInfo.getName());
        // TODO 协商业务需要重写
        negotiationTaskMapper.insert(record);
        // 修改数据元状态为协商中
        baseDataElement.setStatus(StatusEnums.NEGOTIATING.getCode());
        baseDataElementMapper.updateById(baseDataElement);
    }

    /**
     * 获取数据源单位列表,模糊查询部门信息
     *
     * @param unitName
     * @return
     */
    @Override
    public List<SelectSourceUnitVo> selectSourceUnit(String unitName) {
        // 调用bsp服务获取部门信息

        return Collections.emptyList();
    }

    /**
     * 核定数源
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifiedDataSource(VerifiedDataSourceDto dto) {
        String dataid = dto.getDataid();
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        // 待核定状态的数据元可以核定数源
        if (!StatusEnums.PENDING_APPROVAL.getCode().equals(baseDataElement.getStatus())) {
            throw new IllegalArgumentException("数据元状态不是已确认状态!");
        }
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 修改基准数据元定源信息
        baseDataElement.setSourceUnitCode(dto.getSourceUnitCode());
        baseDataElement.setSourceUnitName(dto.getSourceUnitName());
        baseDataElement.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
        baseDataElement.setPublishDate(new Date());
        baseDataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElement.setLastModifyDate(new Date());
        // 更新基准数据元信息
        baseDataElementMapper.updateById(baseDataElement);
    }

    /**
     * 获取数据元详情
     *
     * @param dataid
     * @return
     */
    @Override
    public DataElementInfoVo info(String dataid) {
        // 查询数据元详情
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        // 查询数据元关联的领域数据元信息
        DomainDataElement domainDataElement = domainDataElementMapper.selectFirstByBaseDataelementDataid(dataid);

        // 流程节点信息
        List<SourceRequestList> sourceRequestLists = new ArrayList<>();
        // 查询数据元关联定源任务表信息
        ConfirmationTask confirmationTask = confirmationTaskMapper.selectFirstByBaseDataelementDataid(dataid);
        // 封装流程节点信息, 默认是三个节点, 发起定源, 采集单位确认, 核定数源单位
        // 通过状态判断,需要创建几个节点
        // 定源任务状态如果不是已拒绝,则创建三个流程节点,否则创建四个流程节点
        if (Objects.nonNull(confirmationTask) && StatusEnums.PENDING_NEGOTIATION.getCode().equals(confirmationTask.getStatus())) {
            // 创建四个流程节点,发起定源(已通过), 采集单位确认(已拒绝), 发起协商(判断条件,是否关联表有值), 手动定源(判断条件,已经发起协商的数据元状态为已定源)
            // 第一个节点, 发起定源
            SourceRequestList one = new SourceRequestList();
            one.setNodeName("发起定源");
            one.setNodeFeedback(null);
            one.setNodeHandleUserName(confirmationTask.getSenderName());
            one.setNodeResult(null);
            one.setPassDate(confirmationTask.getSendDate());
            one.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
            sourceRequestLists.add(one);
            // 第二个节点, 采集单位确认
            SourceRequestList two = new SourceRequestList();
            two.setNodeName("采集单位确认");
            two.setNodeFeedback(confirmationTask.getProcessingOpinion());
            two.setNodeHandleUserName(confirmationTask.getProcessingUnitName());
            two.setNodeResult(confirmationTask.getProcessingResult());
            two.setPassDate(confirmationTask.getProcessingDate());
            two.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
            sourceRequestLists.add(two);
            // 第三个节点, 发起协商
            // 查询发起协商表
            NegotiationRecord negotiationRecord = negotiationTaskMapper.selectFirstByBaseDataelementDataid(dataid);
            SourceRequestList three = new SourceRequestList();
            three.setNodeName("发起协商");
            if (Objects.nonNull(negotiationRecord)) {
                three.setNodeFeedback(null);
                three.setNodeHandleUserName(negotiationRecord.getSenderName());
                three.setNodeResult(null);
                three.setPassDate(negotiationRecord.getSendDate());
                three.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
            } else {
                // 没有发起协商的信息,则展示中,蓝框需要选中,发起协商节点
                three.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
            }
            // 第四个节点, 手动定源
            SourceRequestList four = new SourceRequestList();
            four.setNodeName("手动定源");
            // 手动定源节点, 判断当前订单的状态是否是协商中, 如果是,则展示中,蓝框需要选中, 但没有其他信息
            if (confirmationTask.getStatus().equals(StatusEnums.NEGOTIATING.getCode())) {
                four.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
            }
            // 如果状态是已经定源, 则展示完成
            if (confirmationTask.getStatus().equals(StatusEnums.DESIGNATED_SOURCE.getCode())) {
                four.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                // 处理人信息取基准元数据对象中的最后修改人信息
                four.setNodeHandleUserName(baseDataElement.getLastModifyAccount());
                four.setPassDate(baseDataElement.getLastModifyDate());
            }
        } else {
            // 创建三个流程节点,发起定源, 采集单位确认,
            // 判断订单状态, 待定源, 三个节点中, 发起定源节点显示状态为蓝框选中, 采集单位确认节点和核定数源单位节点为待处理(灰色),
            if (Objects.isNull(confirmationTask) && baseDataElement.getStatus().equals(StatusEnums.PENDING_SOURCE.getCode())) {
                SourceRequestList one = new SourceRequestList();
                one.setNodeName("发起定源");
                one.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
                sourceRequestLists.add(one);
                SourceRequestList two = new SourceRequestList();
                two.setNodeName("采集单位确认");
                two.setNodeShowStatus(NodeStatusEnums.WAIT_HANDLE.getStatus());
                sourceRequestLists.add(two);
                SourceRequestList three = new SourceRequestList();
                three.setNodeName("核定数源单位");
                three.setNodeShowStatus(NodeStatusEnums.WAIT_HANDLE.getStatus());
                sourceRequestLists.add(three);
            }
            // 订单状态为确认中, 三个节点, 发起定源节点显示状态为完成, 采集单位确认节点为蓝色, 核定数源单位节点为灰色
            if (Objects.nonNull(confirmationTask) && confirmationTask.getStatus().equals(StatusEnums.CONFIRMING.getCode())) {
                SourceRequestList one = new SourceRequestList();
                one.setNodeName("发起定源");
                one.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                one.setNodeHandleUserName(confirmationTask.getSenderName());
                one.setPassDate(confirmationTask.getSendDate());
                sourceRequestLists.add(one);
                SourceRequestList two = new SourceRequestList();
                two.setNodeName("采集单位确认");
                two.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                sourceRequestLists.add(two);
                SourceRequestList three = new SourceRequestList();
                three.setNodeName("核定数源单位");
                three.setNodeShowStatus(NodeStatusEnums.WAIT_HANDLE.getStatus());
            }
            // 订单状态为待核定, 三个节点, 发起定源节点显示状态为完成, 采集单位确认节点为完成, 核定数源单位节点为蓝色
            if (Objects.nonNull(confirmationTask) && confirmationTask.getStatus().equals(StatusEnums.PENDING_APPROVAL.getCode())) {
                SourceRequestList one = new SourceRequestList();
                one.setNodeName("发起定源");
                one.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                one.setNodeHandleUserName(confirmationTask.getSenderName());
                one.setPassDate(confirmationTask.getSendDate());
                sourceRequestLists.add(one);
                SourceRequestList two = new SourceRequestList();
                two.setNodeName("采集单位确认");
                two.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                two.setNodeResult(confirmationTask.getProcessingResult());
                two.setNodeHandleUserName(confirmationTask.getProcessingUnitName());
                two.setPassDate(confirmationTask.getProcessingDate());
                sourceRequestLists.add(two);
                SourceRequestList three = new SourceRequestList();
                three.setNodeName("核定数源单位");
                three.setNodeShowStatus(NodeStatusEnums.HANDLING.getStatus());
                sourceRequestLists.add(three);
            }
            // 订单状态为已定源, 三个节点, 发起定源节点显示状态为完成, 采集单位确认节点为完成, 核定数源单位节点为完成
            if (Objects.nonNull(confirmationTask) && confirmationTask.getStatus().equals(StatusEnums.DESIGNATED_SOURCE.getCode())) {
                SourceRequestList one = new SourceRequestList();
                one.setNodeName("发起定源");
                one.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                one.setNodeHandleUserName(confirmationTask.getSenderName());
                one.setPassDate(confirmationTask.getSendDate());
                sourceRequestLists.add(one);
                SourceRequestList two = new SourceRequestList();
                two.setNodeName("采集单位确认");
                two.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                two.setNodeResult(confirmationTask.getProcessingResult());
                two.setNodeHandleUserName(confirmationTask.getProcessingUnitName());
                two.setPassDate(confirmationTask.getProcessingDate());
                sourceRequestLists.add(two);
                SourceRequestList three = new SourceRequestList();
                three.setNodeName("核定数源单位");
                three.setNodeShowStatus(NodeStatusEnums.HANDLE_COMPLETE.getStatus());
                three.setNodeHandleUserName(baseDataElement.getLastModifyAccount());
                three.setPassDate(baseDataElement.getLastModifyDate());
                sourceRequestLists.add(three);
            }
        }

        // 关联
        DataElementInfoVo vo = new DataElementInfoVo();
        vo.setCollectUnitCode(domainDataElement.getSourceUnitCode());
        vo.setCollectUnitName(domainDataElement.getSourceUnitName());
        vo.setDataElementId(baseDataElement.getDataElementId());
        vo.setDataFormat(baseDataElement.getDataFormat());
        vo.setDataid(baseDataElement.getDataid());
        vo.setDatatype(baseDataElement.getDatatype());
        vo.setDefinition(baseDataElement.getDefinition());
        vo.setName(baseDataElement.getName());
        vo.setPublishDate(baseDataElement.getPublishDate());
        vo.setRemarks(baseDataElement.getRemarks());
        vo.setSourceRequestList(sourceRequestLists);
        vo.setSourceUnitCode(baseDataElement.getSourceUnitCode());
        vo.setSourceUnitName(baseDataElement.getSourceUnitName());
        vo.setStatus(baseDataElement.getStatus());
        vo.setStatusDesc(StatusEnums.getDescByCode(baseDataElement.getStatus()));
        vo.setValueDomain(baseDataElement.getValueDomain());
        return vo;
    }

    /**
     * 手动定源
     *
     * @param dto
     */
    @Override
    public void manualSource(ManualSourceDto dto) {
        String dataid = dto.getDataid();
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        // 协商中状态的数据元可以核定数源 TODO 接口调试中,先注释掉
//        if (!StatusEnums.NEGOTIATING.getCode().equals(baseDataElement.getStatus())) {
//            throw new IllegalArgumentException("数据元状态不是协商中状态!");
//        }
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 修改基准数据元定源信息
        baseDataElement.setSourceUnitCode(dto.getSourceUnitCode());
        baseDataElement.setSourceUnitName(dto.getSourceUnitName());
        baseDataElement.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
        baseDataElement.setPublishDate(new Date());
        baseDataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElement.setLastModifyDate(new Date());
        // 更新基准数据元信息
        baseDataElementMapper.updateById(baseDataElement);
    }
}



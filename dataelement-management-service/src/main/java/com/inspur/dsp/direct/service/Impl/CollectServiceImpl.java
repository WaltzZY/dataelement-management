package com.inspur.dsp.direct.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.RefuseDto;
import com.inspur.dsp.direct.entity.excel.PendingCollectDataExcel;
import com.inspur.dsp.direct.entity.excel.ProcessedCollectDataExcel;
import com.inspur.dsp.direct.entity.vo.CollectDataInfoVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import com.inspur.dsp.direct.enums.CollSortFieldEnums;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TaskTypeEnums;
import com.inspur.dsp.direct.service.CollectService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class CollectServiceImpl implements CollectService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;

    /**
     * 获取确认数据元列表
     *
     * @param dto
     * @return
     */
    @Override
    public Page<GetCollectDataVo> getCollectDataElementPage(CollectDataElementPageDto dto) {
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 拿到当前登录人的部门code
        String organCode = userInfo.getOrgCode();
        // 排序sql
        String sortSql = CollSortFieldEnums.getSortFieldSql(dto.getSortField());
        // 查询 base_data_element 表和 domain_data_element 表和 confirmation_task 确认任务表
        // domain_data_element 表确认要显示的内容, base_data_element 表确认数据元的信息和状态, confirmation_task 表确认任务信息,接收时间,处理时间
        Page<GetCollectDataVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<GetCollectDataVo> data = baseDataElementMapper.getCollectData(page, dto, sortSql, organCode);
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(vo -> {
                vo.setStatusDesc(ConfirmationTaskEnums.getDescByCode(vo.getStatus()));
            });
        }
        return page.setRecords(data);
    }

    /**
     * 拒绝/通过成为数源单位
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuse(RefuseDto dto) {
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String organCode = userInfo.getOrgCode();
        List<String> dataid = dto.getDataid();
        if (CollectionUtils.isEmpty(dataid)) {
            throw new RuntimeException("请选择数据元!!!");
        }
        List<ConfirmationTask> updateConfList = new ArrayList<>();
        List<BaseDataElement> updateBaseDataList = new ArrayList<>();
        for (String id : dataid) {
            // 校验数据元是否存在
            BaseDataElement baseDataElement = baseDataElementMapper.selectById(id);
            if (Objects.isNull(baseDataElement)) {
                throw new RuntimeException("数据元不存在!!!");
            }
            // 拿到自己的待确认任务
            ConfirmationTask confirmationTask = confirmationTaskMapper
                    .selectFirstByTasktypeAndBaseDataelementDataidAndStatusAndProcessingUnitCode(TaskTypeEnums.CONFIRMATION_TASK.getCode()
                            , id, ConfirmationTaskEnums.PENDING.getCode(), organCode);
            String handleStatus = dto.getHandleStatus();
            String dataStatus = "";
            String confirmationTaskStatus = "";
            if (ConfirmationTaskEnums.CONFIRMED.getCode().equals(handleStatus)) {
                // 已确认状态 -> 确认 -> 待核定
                dataStatus = StatusEnums.PENDING_APPROVAL.getCode();
                if (TaskTypeEnums.CONFIRMATION_TASK.getCode().equals(confirmationTask.getTasktype())) {
                    // 确认任务状态:已确认
                    confirmationTaskStatus = ConfirmationTaskEnums.CONFIRMED.getCode();
                }
            }
            if (ConfirmationTaskEnums.REJECTED.getCode().equals(handleStatus)) {
                dataStatus = StatusEnums.PENDING_NEGOTIATION.getCode();
                confirmationTaskStatus = ConfirmationTaskEnums.REJECTED.getCode();
            }
            if (!StringUtils.hasText(dataStatus)) {
                throw new RuntimeException("请选择处理状态!");
            }
            // 更新数据元状态
            baseDataElement.setStatus(dataStatus);
            baseDataElement.setLastModifyAccount(userInfo.getAccount());
            baseDataElement.setLastModifyDate(new Date());
            updateBaseDataList.add(baseDataElement);
            // 更新数据元任务状态
            confirmationTask.setStatus(confirmationTaskStatus);
            confirmationTask.setProcessingDate(new Date());
            confirmationTask.setProcessingResult(dto.getOpinion());
            confirmationTask.setProcessingOpinion(dto.getOpinion());
            confirmationTask.setProcessorAccount(userInfo.getAccount());
            confirmationTask.setProcessorName(userInfo.getName());
            updateConfList.add(confirmationTask);
        }
        baseDataElementMapper.updateById(updateBaseDataList);
        confirmationTaskMapper.updateById(updateConfList);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public CollectDataInfoVo info(String id) {
        // 查询数据元详情
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(id);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        // 查询数据元任务详情
        ConfirmationTask confirmationTask = confirmationTaskMapper.selectFirstByBaseDataelementDataid(id);
        // 构建响应数据
        CollectDataInfoVo vo = new CollectDataInfoVo();
        vo.setDataElementId(baseDataElement.getDataElementId());
        vo.setDataFormat(baseDataElement.getDataFormat());
        vo.setDataid(baseDataElement.getDataid());
        vo.setDatatype(baseDataElement.getDatatype());
        vo.setDefinition(baseDataElement.getDefinition());
        vo.setName(baseDataElement.getName());
        vo.setPublishDate(baseDataElement.getPublishDate());
        vo.setRefuseReason(confirmationTask.getProcessingOpinion());
        vo.setRemarks(baseDataElement.getRemarks());
        vo.setSendDate(confirmationTask.getSendDate());
        vo.setSenderName(confirmationTask.getSenderName());
        vo.setSourceUnitCode(baseDataElement.getSourceUnitCode());
        vo.setSourceUnitName(baseDataElement.getSourceUnitName());
        vo.setStatus(baseDataElement.getStatus());
        vo.setStatusDesc(StatusEnums.getDescByCode(baseDataElement.getStatus()));
        vo.setTaskId(confirmationTask.getTaskId());
        vo.setValueDomain(baseDataElement.getValueDomain());
        return vo;
    }

    /**
     * 导出数据
     *
     * @param dto
     * @param response
     */
    @Override
    public void exportData(CollectDataElementPageDto dto, HttpServletResponse response) {
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 拿到当前登录人的部门code
        String organCode = userInfo.getOrgCode();
        // 排序sql
        String sortSql = CollSortFieldEnums.getSortFieldSql(dto.getSortField());
        // 查询数据,不分页
        List<GetCollectDataVo> list = baseDataElementMapper.getCollectData(null, dto, sortSql, organCode);
        if (CollectionUtils.isEmpty(list)) {
            log.warn("无待处理数据");
        }
        // 设置响应头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 根据状态设置文件名
        String baseFileName = "数据元列表";
        if ("pending".equals(dto.getAuditStatus())) {
            baseFileName = "待处理数据元列表";
        } else {
            baseFileName = "已处理数据元列表";
        }
        try {
            String fileName = java.net.URLEncoder.encode(baseFileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        } catch (Exception e) {
            log.error("设置文件名失败", e);
            throw new RuntimeException("设置文件名失败", e);
        }
        if ("pending".equals(dto.getAuditStatus())) {
            // 转换成Excel数据实体
            List<PendingCollectDataExcel> pendingCollectDataExcels = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                GetCollectDataVo vo = list.get(i);
                PendingCollectDataExcel excel = PendingCollectDataExcel.toExcel(vo, i + 1);
                pendingCollectDataExcels.add(excel);
            }
            // 使用easyexcel导出数据
            try {
                EasyExcel.write(response.getOutputStream(), PendingCollectDataExcel.class)
                        .sheet("数据元列表")
                        .doWrite(pendingCollectDataExcels);
            } catch (IOException e) {
                log.error("导出数据元失败", e);
                throw new RuntimeException("导出数据元失败", e);
            }
        } else {
            // 转换成Excel数据实体
            List<ProcessedCollectDataExcel> excelList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                GetCollectDataVo vo = list.get(i);
                ProcessedCollectDataExcel excel = ProcessedCollectDataExcel.toExcel(vo, i + 1);
                excelList.add(excel);
            }
            // 使用easyexcel导出数据
            try {
                EasyExcel.write(response.getOutputStream(), ProcessedCollectDataExcel.class)
                        .sheet("数据元列表")
                        .doWrite(excelList);
            } catch (IOException e) {
                log.error("导出数据元失败", e);
                throw new RuntimeException("导出数据元失败", e);
            }
        }
    }
}

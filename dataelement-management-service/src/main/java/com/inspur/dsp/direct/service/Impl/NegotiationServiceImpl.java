package com.inspur.dsp.direct.service.Impl;

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
import com.inspur.dsp.direct.dbentity.NegotiationRecordDetail;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BatchNegotiationDto;
import com.inspur.dsp.direct.entity.dto.ExportDoingNegoDTO;
import com.inspur.dsp.direct.entity.dto.ExportDoneNegoDTO;
import com.inspur.dsp.direct.entity.dto.ExportToDoNegoDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationFailDetailDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationResutDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.vo.CollectUnitsAndStatusVO;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.NePageSortFieldEnums;
import com.inspur.dsp.direct.enums.RecordSourceTypeEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.exception.CustomException;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.NegotiationService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NegotiationServiceImpl implements NegotiationService {

    private final NegotiationRecordMapper negotiationMapper;
    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;
    private final NegotiationRecordMapper negotiationRecordMapper;
    private final DomainDataElementMapper domainDataElementMapper;
    private final SourceEventRecordMapper sourceEventRecordMapper;
    private final CommonService commonService;

    @Override
    public Page<NegotiationDataElementVO> getNegotiationDataElementList(NegotiationParmDTO negDTO) {
        // 1. 使用NegDTO.sortField和NegDTO.sortOrder构建查询条件
        String sortSql = NePageSortFieldEnums.getOrderBySql(negDTO.getSortField(), negDTO.getSortOrder());

        // 2. 初始化Page<NegotiationDataElementVO>对象
        Page<NegotiationDataElementVO> page = new Page<>(negDTO.getPageNum(), negDTO.getPageSize());

        // 3. 调用NegotiationMapper.GetNegotiationDataElementList方法，获取返回vos
        List<NegotiationDataElementVO> vos = negotiationMapper.getNegotiationDataElementList(page, negDTO, sortSql);

        // 4. 调用collUnits方法，将返回的vos进行处理
        collUnits(vos);

        // 5. page.setRecords(vos);
        page.setRecords(vos);

        // 6. 返回page
        return page;
    }

    /**
     * 处理基准数据元的任务信息
     * @param vos
     */
    private void collUnits(List<NegotiationDataElementVO> vos) {
        // 1. 循环List<NegotiationDataElementVO>列表
        for (NegotiationDataElementVO vo : vos) {
            // 查询基准数据元下所有的任务 使用NegotiationDataElementVO的dataid作为参数，调用ConfirmationTaskMapper.selectAllByTasktypeAndBaseDataelementDataid()
            List<ConfirmationTask> confirmationTasks = confirmationTaskMapper.selectAllByTasktypeAndBaseDataelementDataid(null, vo.getDataid());

            // 创建List<CollectUnitsAndStatusVO>类
            List<CollectUnitsAndStatusVO> collectUnitsAndStatusList = new ArrayList<>();

            // 循环返回的List<ConfirmationTask>
            for (ConfirmationTask task : confirmationTasks) {
                CollectUnitsAndStatusVO collectUnitsAndStatus = new CollectUnitsAndStatusVO();

                // 如果ConfirmationTask的状态为"已确认"、"已认领"，则CollectUnitsAndStatusVO.status为true；否则为false
                collectUnitsAndStatus.setStatus(ConfirmationTaskEnums.CONFIRMED.getCode().equals(task.getStatus()) ||
                        ConfirmationTaskEnums.CLAIMED.getCode().equals(task.getStatus()));

                collectUnitsAndStatus.setUnitCode(task.getProcessingUnitCode());
                collectUnitsAndStatus.setUnitName(task.getProcessingUnitName());

                // 添加CollectUnitsAndStatusVO到List<CollectUnitsAndStatusVO>中
                collectUnitsAndStatusList.add(collectUnitsAndStatus);
            }

            // 将List<CollectUnitsAndStatusVO>赋值到NegotiationDataElementVO.collectunitsandstatus属性中
            vo.setCollectUnitsAndStatus(collectUnitsAndStatusList);
        }

        // 2. 遍历vos，使用vos.status为参数，调用StatusEnums.getDescByCode，获取返回值desc，赋值vos.statusDesc=desc
        for (NegotiationDataElementVO vo : vos) {
            String desc = StatusEnums.getDescByCode(vo.getStatus());
            vo.setStatusDesc(desc);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitBatchNegotiation(BatchNegotiationDto dto) {
        String negotiationContent = dto.getNegotiationContent();
        List<String> dataids = dto.getDataids();

        // 调用BaseDataElementMapper.selectBatchIds(list<string> dataids)，获取List<BaseDataElement>
        List<BaseDataElement> baseDataElements = baseDataElementMapper.selectBatchIds(dataids);

        // 调用BspLoginUserInfoUtils.UserLoginInfo方法，获取currentuser
        UserLoginInfo currentUser = BspLoginUserInfoUtils.getUserInfo();

        List<BaseDataElement> updateBaseDataElements = new ArrayList<>();
        // 循环List<BaseDataElement>
        Date date = new Date();
        for (BaseDataElement baseDataElement : baseDataElements) {
            // 赋值BaseDataElement.status为statusEnums的"协商中"代码
            baseDataElement.setStatus(StatusEnums.NEGOTIATING.getCode());
            baseDataElement.setLastModifyDate(date);
            baseDataElement.setLastModifyAccount(currentUser.getAccount());

            // 构建NegotiationRecord对象
            NegotiationRecord negotiationRecord = new NegotiationRecord();

            // 为NegotiationRecord对象赋值
            negotiationRecord.setRecordId(UUID.randomUUID().toString());
            negotiationRecord.setBaseDataelementDataid(baseDataElement.getDataid());
            negotiationRecord.setSendDate(date);
            negotiationRecord.setSenderAccount(currentUser.getAccount());
            negotiationRecord.setSenderName(currentUser.getName());
            negotiationRecord.setNegotiationContent(negotiationContent);

            // 使用BaseDataElement.dataid作为参数，使用DomainDataElementMapper.selectAllByBaseDataelementDataid方法获取List<DomainDataElement>
            List<DomainDataElement> domainDataElements = domainDataElementMapper.selectAllByBaseDataelementDataid(baseDataElement.getDataid());

            // 协商详情集合
            List<NegotiationRecordDetail> details = new ArrayList<>();

            // 循环List<DomainDataElement>
            for (DomainDataElement domainDataElement : domainDataElements) {
                // 在循环中创建NegotiationRecordDetail对象
                NegotiationRecordDetail detail = new NegotiationRecordDetail();
                // 赋值
                detail.setRecordDetailId(UUID.randomUUID().toString());
                detail.setRecordId(negotiationRecord.getRecordId());
                detail.setNegotiationUnitCode(domainDataElement.getSourceUnitCode());
                detail.setNegotiationUnitName(domainDataElement.getSourceUnitName());
                // 将NegotiationRecordDetail对象添加进NegotiationRecord.List<NegotiationRecordDetail>中
                details.add(detail);
            }
            negotiationRecord.setNegotiationRecordDetailList(details);
            // 调用NegotiationRecordMapper.insertRecordAndDetail(NegotiationRecord)方法保存NegotiationRecord
            negotiationRecordMapper.insertRecordAndDetail(negotiationRecord);
            // 将BaseDataElement对象添加进List<BaseDataElement>中, 批量更新
            updateBaseDataElements.add(baseDataElement);
        }
        // 调用BaseDataElementMapper.updateById(BaseDataElement)方法保存BaseDataElement
        baseDataElementMapper.updateById(updateBaseDataElements);


    }

    @Override
    public void submitSingleNegotiation(SingleNegotiationDto dto) {
        List<String> unitcodes = dto.getUnitcodes();
        String dataid = dto.getDataid();
        String negotiationContent = dto.getNegotiationContent();
        // 统一社会信用代码去重
        List<String> unitcodesDistinct = unitcodes.stream().distinct().collect(Collectors.toList());
        // 以unitcodes作为参数，调用CommonService.getOrgInfoByBatchOrgCode()获取List<OrganizationUnit>
        List<OrganizationUnit> organizationUnits = commonService.getOrgInfoByBatchOrgCode(unitcodesDistinct);

        // 调用BspLoginUserInfoUtils.UserLoginInfo方法，获取currentuser
        UserLoginInfo currentUser = BspLoginUserInfoUtils.getUserInfo();
        // 当前时间
        Date date = new Date();

        // 以dataid为参数，调用BaseDataElementMapper.selectById,获取BaseDataElement对象
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);

        // 赋值BaseDataElement.status为statusEnums的"协商中"代码
        baseDataElement.setStatus(StatusEnums.NEGOTIATING.getCode());
        baseDataElement.setLastModifyDate(date);
        baseDataElement.setLastModifyAccount(currentUser.getAccount());

        // 构建NegotiationRecord对象
        NegotiationRecord negotiationRecord = new NegotiationRecord();

        // 为NegotiationRecord对象赋值
        negotiationRecord.setRecordId(UUID.randomUUID().toString());
        negotiationRecord.setBaseDataelementDataid(baseDataElement.getDataid());
        negotiationRecord.setSendDate(date);
        negotiationRecord.setSenderAccount(currentUser.getAccount());
        negotiationRecord.setSenderName(currentUser.getName());
        negotiationRecord.setNegotiationContent(negotiationContent);

        List<NegotiationRecordDetail> details = new ArrayList<>();

        // 循环List<OrganizationUnit>
        for (OrganizationUnit unit : organizationUnits) {
            // 在循环中创建NegotiationRecordDetail对象
            NegotiationRecordDetail detail = new NegotiationRecordDetail();

            // 赋值
            detail.setRecordDetailId(UUID.randomUUID().toString());
            detail.setRecordId(negotiationRecord.getRecordId());
            detail.setNegotiationUnitCode(unit.getUnitCode());
            detail.setNegotiationUnitName(unit.getUnitName());

            // 将NegotiationRecordDetail对象添加进NegotiationRecord.List<NegotiationRecordDetail>中
            details.add(detail);
        }

        negotiationRecord.setNegotiationRecordDetailList(details);

        // 调用BaseDataElementMapper.updateById(BaseDataElement)方法保存BaseDataElement
        baseDataElementMapper.updateById(baseDataElement);

        // 调用NegotiationRecordMapper.insertRecordAndDetail(NegotiationRecord)方法保存NegotiationRecord
        negotiationRecordMapper.insertRecordAndDetail(negotiationRecord);
    }

    @Override
    public NegotiationRecord getNegotiationRecordInfo(String dataid) {
        // 使用dataid为参数，调用NegotiationRecordMapper.selectFirstByBaseDataelementDataid方法，获取NegotiationRecord对象
        NegotiationRecord negotiationRecord = negotiationRecordMapper.selectFirstByBaseDataelementDataid(dataid);
        // 返回NegotiationRecord对象
        return negotiationRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSingleNegotiationResult(String dataid, String orgCode) {
        // 1. 创建MAP(string，string) NegResultMap
        Map<String, String> negResultMap = new HashMap<>();

        // 2. 在MAP中添加dataid,org_code
        negResultMap.put(dataid, orgCode);

        // 3. 调用方法7：SubmitNegotiationResult(MAP NegResultMap)
        submitNegotiationResult(negResultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportNegotiationReturnDTO importNegotiationResult(MultipartFile file) {
        // 1. 文件校验 file 不为null , 文件格式为xlsx/xls, 文件大小小于100m
        if (file == null) {
            throw new CustomException("文件不能为空");
        }

        if (file.isEmpty()) {
            throw new CustomException("文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
            throw new CustomException("文件格式不正确，请上传Excel文件(.xlsx或.xls)");
        }

        long fileSize = file.getSize();
        long maxSize = 100 * 1024 * 1024; // 100MB
        if (fileSize > maxSize) {
            throw new CustomException("文件大小不能超过100MB");
        }

        // 2. 调用CommonService.importExcelData 解析为对象数据
        List<ImportNegotiationResutDTO> vdto = commonService.importExcelData(file, ImportNegotiationResutDTO.class);

        // 3. 创建MAP(string,string) negResultMap
        Map<String, String> negResultMap = new HashMap<>();

        // 4. 循环vdto
        for (ImportNegotiationResutDTO dto : vdto) {
            // 以dto.dataElementName为参数，调用baseDataElementMapper的selectByName方法，获取数据元BaseDataElement对象bde
            BaseDataElement bde = baseDataElementMapper.selectFirstByName(dto.getDataElementName());

            // 在negResultMap中添加{bde.dataid,dto.unitCode}
            if (bde != null) {
                negResultMap.put(bde.getDataid(), dto.getUnitCode());
            }
        }

        // 5. 以negResultMap为参数，调用SubmitNegotiationResult方法
        return submitNegotiationResult(negResultMap);
    }

    @Override
    public void exportNegotiationList(NegotiationParmDTO dto, String exportFlag, HttpServletResponse response) {
        // 1. 使用NegDTO.sortField和NegDTO.sortOrder构建查询条件
        String sortSql = NePageSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
        // 2. 调用NegotiationMapper.GetNegotiationDataElementList方法，获取返回voslist
        List<NegotiationDataElementVO> voslist = negotiationMapper.getNegotiationDataElementList(null, dto, sortSql);
        // 3. 调用collUnits方法，将返回的vos进行处理
        collUnits(voslist);
        // 4. 判断exportFlag，初始化不同的对象
        switch (exportFlag) {
            case "todonego":
                exportToDoNego(voslist, response);
                break;
            case "doingnego":
                exportDoingNego(voslist, response);
                break;
            case "donenego":
                exportDoneNego(voslist, response);
                break;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ImportNegotiationReturnDTO submitNegotiationResult(Map<String, String> negResultMap) {
        // 1. 创建ImportNegotiationReturnDTO
        ImportNegotiationReturnDTO result = new ImportNegotiationReturnDTO();
        List<ImportNegotiationFailDetailDTO> failDetails = new ArrayList<>();
        long successCount = 0;
        long totalCount = negResultMap.size();

        // 2. 循环NegResultMap
        for (Map.Entry<String, String> entry : negResultMap.entrySet()) {
            String dataid = entry.getKey();
            String orgCode = entry.getValue();

            try {
                // 获取当前登录人信息
                UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

                // 调用baseDataElementMapper中的mybatisPlus的默认方法selectbyId()，查询基准数据元信息BaseDataElement
                BaseDataElement baseinfo = baseDataElementMapper.selectById(dataid);

                // 调用组织机构信息，deptinfo = OrgService.GetOrgInfo(org_code)
                OrganizationUnit unit = commonService.getOrgInfoByOrgCode(orgCode);

                // 创建一个SourceEventRecord对象
                SourceEventRecord sourceEventRecord = new SourceEventRecord();
                sourceEventRecord.setRecordId(UUID.randomUUID().toString());
                sourceEventRecord.setDataElementId(baseinfo.getDataElementId());
                sourceEventRecord.setDataElementName(baseinfo.getName());
                sourceEventRecord.setSourceType(RecordSourceTypeEnums.NEGOTIATION_RESULT_ENTRY.getCode());
                sourceEventRecord.setSourceDate(new Date());
                sourceEventRecord.setOperatorAccount(userInfo.getAccount());
                sourceEventRecord.setSourceUnitCode(unit.getUnitCode());
                sourceEventRecord.setSourceUnitName(unit.getUnitName());
                sourceEventRecord.setSendUnitCode(userInfo.getOrgCode());
                sourceEventRecord.setSendUnitName(userInfo.getOrgName());

                // baseinfo赋值
                baseinfo.setConfirmDate(new Date());
                baseinfo.setSourceUnitCode(unit.getUnitCode());
                baseinfo.setSourceUnitName(unit.getUnitName());
                baseinfo.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
                baseinfo.setLastModifyDate(new Date());
                baseinfo.setLastModifyAccount(userInfo.getAccount());

                // 执行BaseDataElementMapper的mybatisPlus框架方法baseDataElementMapper.updateById()，更新基准数据元信息
                baseDataElementMapper.updateById(baseinfo);

                // 执行SourceEventRecordMapper的mybatisPlus框架方法sourceEventRecordMapper.insert()，批量插入定源记录
                sourceEventRecordMapper.insert(sourceEventRecord);

                successCount++;

            } catch (Exception e) {
                // 循环时发现异常则记录在ImportNegotiationFailDetailDTO中
                ImportNegotiationFailDetailDTO failDetail = new ImportNegotiationFailDetailDTO();
                BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
                if (baseDataElement != null) {
                    failDetail.setDataid(dataid);
                    failDetail.setName(baseDataElement.getName());
                }
                failDetail.setUnit_code(orgCode);
                failDetail.setFailReason(e.getMessage());
                failDetails.add(failDetail);
            }
        }

        // 统计失败数、成功数，总数赋值对应的属性
        result.setTotal(totalCount);
        result.setSucessQty(successCount);
        result.setFailQty(totalCount - successCount);
        result.setFailDetails(failDetails);

        // 返回异常记录
        return result;
    }

    // 私有辅助方法
    private String buildSortSql(String sortField, String sortOrder) {
        if (sortField != null && sortOrder != null) {
            return "ORDER BY " + sortField + " " + sortOrder;
        }
        return "";
    }

    private void exportToDoNego(List<NegotiationDataElementVO> voslist, HttpServletResponse response) {
        // 初始化List<ExportToDoNegoDTO> todoNegoList
        List<ExportToDoNegoDTO> todoNegoList = new ArrayList<>();

        int seqNo = 1;
        // 遍历voslist，依次取出vos
        for (NegotiationDataElementVO vos : voslist) {
            // 创建ExportToDoNegoDTO todoneg
            ExportToDoNegoDTO todoneg = new ExportToDoNegoDTO();
            todoneg.setSeqNo(String.valueOf(seqNo++));
            todoneg.setName(vos.getName());

            // todonet.agreeUnit=vos.collectUnitsAndStatus对象中，status=true的unitName的"|"分隔的合并字符串
            String agreeUnit = vos.getCollectUnitsAndStatus().stream()
                    .filter(CollectUnitsAndStatusVO::getStatus)
                    .map(CollectUnitsAndStatusVO::getUnitName)
                    .collect(Collectors.joining("|"));
            todoneg.setAgreeUnit(agreeUnit);

            // todonet.refuseUnit=vos.collectUnitsAndStatus对象中，status=false的unitName的"|"分隔的合并字符串
            String refuseUnit = vos.getCollectUnitsAndStatus().stream()
                    .filter(unit -> !unit.getStatus())
                    .map(CollectUnitsAndStatusVO::getUnitName)
                    .collect(Collectors.joining("|"));
            todoneg.setRefuseUnit(refuseUnit);

            todoneg.setStatus(vos.getStatusDesc());
            todoneg.setSendDate(vos.getSendDate());

            // 添加todoneg到todoNegoList中
            todoNegoList.add(todoneg);
        }

        // 调用CommonService.exportExcelData
        try {
            commonService.exportExcelData(todoNegoList, response, "待协商数据", ExportToDoNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[待协商数据]失败", e);
            throw new RuntimeException("导出数据[待协商数据]失败");
        }
    }

    private void exportDoingNego(List<NegotiationDataElementVO> voslist, HttpServletResponse response) {
        // 初始化List<ExportDoingNegoDTO> doingNegoList
        List<ExportDoingNegoDTO> doingNegoList = new ArrayList<>();

        int seqNo = 1;
        // 遍历voslist，依次取出vos
        for (NegotiationDataElementVO vos : voslist) {
            // 创建ExportDoingNegoDTO doingneg
            ExportDoingNegoDTO doingneg = new ExportDoingNegoDTO();
            doingneg.setSeqNo(String.valueOf(seqNo++));
            doingneg.setName(vos.getName());

            // agreeUnit=vos.collectUnitsAndStatus对象中，status=true的unitName的"|"分隔的合并字符串
            String agreeUnit = vos.getCollectUnitsAndStatus().stream()
                    .filter(CollectUnitsAndStatusVO::getStatus)
                    .map(CollectUnitsAndStatusVO::getUnitName)
                    .collect(Collectors.joining("|"));
            doingneg.setAgreeUnit(agreeUnit);

            // refuseUnit=vos.collectUnitsAndStatus对象中，status=false的unitName的"|"分隔的合并字符串
            String refuseUnit = vos.getCollectUnitsAndStatus().stream()
                    .filter(unit -> !unit.getStatus())
                    .map(CollectUnitsAndStatusVO::getUnitName)
                    .collect(Collectors.joining("|"));
            doingneg.setRefuseUnit(refuseUnit);

            doingneg.setStatus(vos.getStatusDesc());
            doingneg.setSendDate(vos.getSendDate());

            // negoSendDate=，需要以vos.dataid作为参数，使用NegotiationRecordMapper.selectById查询出NegotiationRecord对象，取NegotiationRecord.sendDate
            NegotiationRecord negotiationRecord = negotiationRecordMapper.selectFirstByBaseDataelementDataidNotDetail(vos.getDataid());
            if (negotiationRecord != null && negotiationRecord.getSendDate() != null) {
                doingneg.setNegoSendDate(negotiationRecord.getSendDate());
            }

            // 添加doingneg到doingNegoList中
            doingNegoList.add(doingneg);
        }

        // 调用CommonService.ExportToExcel(doingNegoList)
        try {
            commonService.exportExcelData(doingNegoList, response, "协商中数据", ExportDoingNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[协商中数据]失败", e);
            throw new RuntimeException("导出数据[协商中数据]失败");
        }
    }

    private void exportDoneNego(List<NegotiationDataElementVO> voslist, HttpServletResponse response) {
        // 初始化List<ExportDoneNegoDTO> doneNegoList
        List<ExportDoneNegoDTO> doneNegoList = new ArrayList<>();

        int seqNo = 1;
        // 遍历voslist，依次取出vos
        for (NegotiationDataElementVO vos : voslist) {
            // 创建ExportDoneNegoDTO doneneg
            ExportDoneNegoDTO doneneg = new ExportDoneNegoDTO();
            doneneg.setSeqNo(String.valueOf(seqNo++));
            doneneg.setName(vos.getName());

            // collectUnit=vos.collectUnitsAndStatus对象中，所有unitName的"|"分隔的合并字符串
            String collectUnit = vos.getCollectUnitsAndStatus().stream()
                    .map(CollectUnitsAndStatusVO::getUnitName)
                    .collect(Collectors.joining("|"));
            doneneg.setCollectUnit(collectUnit);

            doneneg.setStatus(vos.getStatusDesc());
            doneneg.setSendDate(vos.getSendDate());
            doneneg.setSourceUnitName(vos.getSourceUnitName());
            doneneg.setConfirmDate(vos.getConfirmDate());

            // 添加doneneg到doneNegoList中
            doneNegoList.add(doneneg);
        }

        // 调用CommonService.ExportToExcel(doneNegoList)
        try {
            commonService.exportExcelData(doneNegoList, response, "已定源数据", ExportDoneNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[已定源数据]失败", e);
            throw new RuntimeException("导出数据[已定源数据]失败");
        }
    }
}

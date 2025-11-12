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
import com.inspur.dsp.direct.entity.dto.ExportNegotiationFailDTO;
import com.inspur.dsp.direct.entity.dto.ExportToDoNegoDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationFailDetailDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationResutDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationResultDto;
import com.inspur.dsp.direct.entity.vo.CollectUnitsAndStatusVO;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.entity.vo.NegotiationRecordInfoVo;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.NePageSortFieldEnums;
import com.inspur.dsp.direct.enums.RecordSourceTypeEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TemplateTypeEnums;
import com.inspur.dsp.direct.exception.CustomException;
import com.inspur.dsp.direct.service.BaseDataElementService;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.NegotiationService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
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
    private final BaseDataElementService baseDataElementService;

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
                collectUnitsAndStatus.setDataElementName(vo.getName());
                collectUnitsAndStatus.setProcessingDate(task.getProcessingDate());
                collectUnitsAndStatus.setProcessingOpinion(task.getProcessingOpinion());
                collectUnitsAndStatus.setTaskStatus(task.getStatus());
                collectUnitsAndStatus.setTaskStatusDesc(ConfirmationTaskEnums.getDescByCode(task.getStatus()));

                // 添加 CollectUnitsAndStatusVO到List<CollectUnitsAndStatusVO>中
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
    @Transactional(rollbackFor = Exception.class)
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
    public NegotiationRecordInfoVo getNegotiationRecordInfo(String dataid) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (baseDataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        // 使用dataid为参数，调用NegotiationRecordMapper.selectFirstByBaseDataelementDataid方法，获取NegotiationRecord对象
        NegotiationRecord negotiationRecord = negotiationRecordMapper.selectFirstByBaseDataelementDataid(dataid);
        // 返回NegotiationRecordInfoVo对象
        NegotiationRecordInfoVo vo = NegotiationRecordInfoVo.toVo(negotiationRecord);
        // 关联的基准数据元状态也返回
        vo.setStatus(baseDataElement.getStatus());
        vo.setStatusDesc(StatusEnums.getDescByCode(baseDataElement.getStatus()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSingleNegotiationResult(SingleNegotiationResultDto dto) {
        // 创建单条导入数据DTO
        ImportNegotiationResutDTO resutDTO = new ImportNegotiationResutDTO();
        resutDTO.setDataElementName(dto.getDataid());
        resutDTO.setUnitCode(dto.getOrgCode());

        // 创建List并添加单条数据
        List<ImportNegotiationResutDTO> dataList = new ArrayList<>();
        dataList.add(resutDTO);

        // 调用新的方法处理
        submitNegotiationResult(dataList);
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

        // 3. 检查解析后的数据是否为空
        if (vdto == null || vdto.isEmpty()) {
            throw new CustomException("文件内容为空或格式不正确，请检查文件内容");
        }

        // 4. 直接传递List到处理方法
        return submitNegotiationResult(vdto);
    }

    @Override
    public void exportNegotiationList(NegotiationParmDTO dto, HttpServletResponse response) {
        String exportFlag = dto.getExportFlag();
        if (!StringUtils.hasText(exportFlag)) {
            throw new CustomException("请选择导出类型");
        }
        // 1. 使用NegDTO.sortField和NegDTO.sortOrder构建查询条件
        String sortSql = NePageSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
        // 2. 调用NegotiationMapper.GetNegotiationDataElementList方法，获取返回voslist
        List<NegotiationDataElementVO> voslist = negotiationMapper.getNegotiationDataElementList(null, dto, sortSql);
        if (CollectionUtils.isEmpty(voslist)) {
            voslist = new ArrayList<>();
        }
        // 3. 调用collUnits方法，将返回的vos进行处理
        collUnits(voslist);
        // 4. 判断exportFlag，初始化不同的对象
        if (StatusEnums.PENDING_NEGOTIATION.getCode().equals(exportFlag)) {
            exportToDoNego(voslist, response);
        } else if (StatusEnums.NEGOTIATING.getCode().equals(exportFlag)) {
            exportDoingNego(voslist, response);
        } else if (StatusEnums.DESIGNATED_SOURCE.getCode().equals(exportFlag)) {
            exportDoneNego(voslist, response);
        }
    }

    /**
     * 处理协商结果（接收List）
     */
    @Transactional(rollbackFor = Exception.class)
    public ImportNegotiationReturnDTO submitNegotiationResult(List<ImportNegotiationResutDTO> dataList) {
        // 1. 创建ImportNegotiationReturnDTO
        ImportNegotiationReturnDTO result = new ImportNegotiationReturnDTO();
        List<ImportNegotiationFailDetailDTO> failDetails = new ArrayList<>();
        long successCount = 0;
        long totalCount = dataList.size();

        // 2. 数据完整性检查 - 检查数据元名称和单位代码是否都有值
        List<ImportNegotiationResutDTO> incompleteData = dataList.stream()
                .filter(dto -> !StringUtils.hasText(dto.getDataElementName()) || !StringUtils.hasText(dto.getUnitCode()))
                .collect(Collectors.toList());

        // 处理数据不完整的情况
        for (ImportNegotiationResutDTO dto : incompleteData) {
            ImportNegotiationFailDetailDTO failDetail = new ImportNegotiationFailDetailDTO();
            failDetail.setName(dto.getDataElementName());
            failDetail.setUnit_code(dto.getUnitCode());
            failDetail.setFailReason("数据不完整");
            failDetails.add(failDetail);
        }

        // 3. 获取完整数据的条目
        List<ImportNegotiationResutDTO> completeData = dataList.stream()
                .filter(dto -> StringUtils.hasText(dto.getDataElementName()) && StringUtils.hasText(dto.getUnitCode()))
                .collect(Collectors.toList());

        // 4. 冲突检测 - 检测基准数据元与数源单位的组合是否唯一
        // 使用Map记录每个数据元名称对应的所有单位代码
        Map<String, Set<String>> dataElementUnitMap = new HashMap<>();
        Set<String> conflictDataElements = new HashSet<>();

        for (ImportNegotiationResutDTO dto : completeData) {
            String dataElementName = dto.getDataElementName();
            String unitCode = dto.getUnitCode();

            dataElementUnitMap.putIfAbsent(dataElementName, new HashSet<>());
            dataElementUnitMap.get(dataElementName).add(unitCode);

            // 如果同一个数据元有多个不同的单位代码，标记为冲突
            if (dataElementUnitMap.get(dataElementName).size() > 1) {
                conflictDataElements.add(dataElementName);
            }
        }

        // 处理冲突的条目，全部标记为失败
        for (ImportNegotiationResutDTO dto : completeData) {
            if (conflictDataElements.contains(dto.getDataElementName())) {
                ImportNegotiationFailDetailDTO failDetail = new ImportNegotiationFailDetailDTO();
                failDetail.setName(dto.getDataElementName());
                failDetail.setUnit_code(dto.getUnitCode());

                // 获取单位名称
                OrganizationUnit unit = commonService.getOrgInfoByOrgCode(dto.getUnitCode());
                if (unit != null) {
                    failDetail.setUnit_name(unit.getUnitName());
                }

                failDetail.setFailReason("数据与其它条目冲突，基准数据元与数源单位的组合必须唯一");
                failDetails.add(failDetail);
            }
        }

        // 5. 重复数据检测 - 找出在当前导入批次中重复的数据元名称（排除冲突的条目）
        // 按照 "数据元名称|单位代码" 组合进行分组
        Map<String, List<ImportNegotiationResutDTO>> duplicateGroups = completeData.stream()
                .filter(dto -> !conflictDataElements.contains(dto.getDataElementName()))
                .collect(Collectors.groupingBy(dto -> dto.getDataElementName() + "|" + dto.getUnitCode()));

        List<ImportNegotiationResutDTO> validData = new ArrayList<>();

        for (Map.Entry<String, List<ImportNegotiationResutDTO>> group : duplicateGroups.entrySet()) {
            List<ImportNegotiationResutDTO> duplicates = group.getValue();
            if (duplicates.size() > 1) {
                // 保留第一条，其余标记为重复失败
                validData.add(duplicates.get(0));
                for (int i = 1; i < duplicates.size(); i++) {
                    ImportNegotiationFailDetailDTO failDetail = new ImportNegotiationFailDetailDTO();
                    failDetail.setName(duplicates.get(i).getDataElementName());
                    failDetail.setUnit_code(duplicates.get(i).getUnitCode());

                    // 获取单位名称
                    OrganizationUnit unit = commonService.getOrgInfoByOrgCode(duplicates.get(i).getUnitCode());
                    if (unit != null) {
                        failDetail.setUnit_name(unit.getUnitName());
                    }

                    failDetail.setFailReason("数据与其它条目重复，系统已保留首次出现的数据");
                    failDetails.add(failDetail);
                }
            } else {
                validData.add(duplicates.get(0));
            }
        }

        // 6. 循环validData处理正常数据
        for (ImportNegotiationResutDTO dto : validData) {
            String dataElementName = dto.getDataElementName();
            String orgCode = dto.getUnitCode();

            try {
                // 获取当前登录人信息
                UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

                // 先尝试按ID查询基准数据元信息
                BaseDataElement baseinfo = baseDataElementMapper.selectById(dataElementName);
                if (Objects.isNull(baseinfo)) {
                    // 如果按ID查不到，则按名称查询
                    baseinfo = baseDataElementMapper.selectFirstByName(dataElementName);
                }

                StringBuilder errorSb = new StringBuilder();

                // 检查是否找到基准数据元
                if (Objects.isNull(baseinfo)) {
                    errorSb.append("系统在状态为协商中的基准数据元集合中未找到匹配的内容");
                } else {
                    // 检查数据元状态是否为协商中
                    if (!StatusEnums.NEGOTIATING.getCode().equals(baseinfo.getStatus())) {
                        errorSb.append("系统在状态为协商中的基准数据元集合中未找到匹配的内容");
                    }
                }

                // 调用组织机构信息，检查组织或单位是否存在
                OrganizationUnit unit = null;
                if (StringUtils.hasText(orgCode)) {
                    unit = commonService.getOrgInfoByOrgCode(orgCode);
                }
                if (Objects.isNull(unit)) {
                    if (errorSb.length() > 0) {
                        errorSb.append("\n");
                    }
                    errorSb.append("未查询到相关组织或单位");
                }

                if (StringUtils.hasText(errorSb.toString())) {
                    throw new CustomException(errorSb.toString());
                }

                // 创建一个SourceEventRecord对象
                SourceEventRecord sourceEventRecord = new SourceEventRecord();
                sourceEventRecord.setRecordId(UUID.randomUUID().toString());
                sourceEventRecord.setDataElementId(baseinfo.getDataid());
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

                // 新增基准数据元联系人信息
                baseDataElementService.insertDataElementContact(baseinfo.getDataid());

                successCount++;

            } catch (Exception e) {
                // 循环时发现异常则记录在ImportNegotiationFailDetailDTO中
                ImportNegotiationFailDetailDTO failDetail = new ImportNegotiationFailDetailDTO();

                // 尝试获取数据元信息用于失败详情
                BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataElementName);
                if (baseDataElement == null) {
                    baseDataElement = baseDataElementMapper.selectFirstByName(dataElementName);
                }
                if (baseDataElement != null) {
                    failDetail.setDataid(dataElementName);
                    failDetail.setName(baseDataElement.getName());
                } else {
                    failDetail.setName(dataElementName);
                }

                // 获取单位名称
                OrganizationUnit unit = commonService.getOrgInfoByOrgCode(orgCode);
                failDetail.setUnit_code(orgCode);
                if (unit != null) {
                    failDetail.setUnit_name(unit.getUnitName());
                }

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
            todoneg.setDefinition(vos.getDefinition());

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
            commonService.exportExcelData(todoNegoList, response, "待协商基准数据元清单", ExportToDoNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[待协商基准数据元清单]失败", e);
            throw new RuntimeException("导出数据[待协商基准数据元清单]失败");
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
            doingneg.setDefinition(vos.getDefinition());

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

            // 查询协商单位名称并设置
            List<String> negotiationUnitNames = negotiationRecordMapper.selectNegotiationUnitNamesByBaseDataelementDataid(vos.getDataid());
            String negotiationUnitNamesStr = negotiationUnitNames.stream()
                    .filter(name -> name != null && !name.trim().isEmpty())
                    .collect(Collectors.joining("|"));
            doingneg.setNegotiationUnitNames(negotiationUnitNamesStr);

            // 添加doingneg到doingNegoList中
            doingNegoList.add(doingneg);
        }

        // 调用CommonService.ExportToExcel(doingNegoList)
        try {
            commonService.exportExcelData(doingNegoList, response, "协商中基准数据元清单", ExportDoingNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[协商中基准数据元清单]失败", e);
            throw new RuntimeException("导出数据[协商中基准数据元清单]失败");
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
            doneneg.setDefinition(vos.getDefinition());

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
            commonService.exportExcelData(doneNegoList, response, "已定源基准数据元清单", ExportDoneNegoDTO.class);
        } catch (IOException e) {
            log.error("导出数据[已定源基准数据元清单]失败", e);
            throw new RuntimeException("导出数据[已定源基准数据元清单]失败");
        }
    }

    @Override
    public void exportNegotiationFailList(List<ImportNegotiationFailDetailDTO> failDetails, HttpServletResponse response) {
        // 初始化List<ExportNegotiationFailDTO> failList
        List<ExportNegotiationFailDTO> failList = new ArrayList<>();

        if (CollectionUtils.isEmpty(failDetails)) {
            failDetails = new ArrayList<>();
        }

        int seqNo = 1;
        // 遍历failDetails，依次取出failDetail
        for (ImportNegotiationFailDetailDTO failDetail : failDetails) {
            // 创建ExportNegotiationFailDTO exportFail
            ExportNegotiationFailDTO exportFail = new ExportNegotiationFailDTO();
            exportFail.setSeqNo(String.valueOf(seqNo++));
            exportFail.setDataElementName(failDetail.getName());
            exportFail.setSourceUnitName(failDetail.getUnit_name());
            exportFail.setSourceUnitCode(failDetail.getUnit_code());
            exportFail.setFailReason(failDetail.getFailReason());

            // 添加exportFail到failList中
            failList.add(exportFail);
        }

        // 调用CommonService.exportExcelData
        try {
            commonService.exportExcelData(failList, response, "导入失败清单(录入协商结果)", ExportNegotiationFailDTO.class);
        } catch (IOException e) {
            log.error("导出数据[导入失败清单(录入协商结果)]失败", e);
            throw new RuntimeException("导出数据[导入失败清单(录入协商结果)]失败");
        }
    }

    @Override
    public void downloadImportTemplate(TemplateTypeEnums templateType, HttpServletResponse response) {
        log.info("开始下载导入模板，模板类型：{}", templateType.getDesc());

        try {
            switch (templateType) {
                case IMPORT_DATASOURCE_RESULT:
                    String templateFileName = "定源结果导入模板.xlsx";
                    String downloadFileName = "导入定源结果模板.xlsx";
                    downloadTemplateFile(templateFileName, downloadFileName, response);
                    break;

                default:
                    throw new IllegalArgumentException("不支持的模板类型：" + templateType.getDesc());
            }

            log.info("模板下载完成，模板类型：{}", templateType.getDesc());
        } catch (Exception e) {
            log.error("下载模板失败，模板类型：{}", templateType.getDesc(), e);
            throw new RuntimeException("下载模板失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载模板文件（支持jar包运行）
     */
    private void downloadTemplateFile(String templateFileName, String downloadFileName, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" +
            URLEncoder.encode(downloadFileName, "UTF-8"));

        // 尝试从classpath读取模板文件（支持jar包运行）
        try (InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateFileName)) {
            if (templateStream == null) {
                // 如果classpath中没有，尝试从项目根目录读取（开发环境）
                String projectRoot = System.getProperty("user.dir");
                String templateFilePath = projectRoot + File.separator + templateFileName;

                File templateFile = new File(templateFilePath);
                if (!templateFile.exists()) {
                    throw new RuntimeException("模板文件不存在: " + templateFilePath + " 且在classpath中也未找到: templates/" + templateFileName);
                }

                try (FileInputStream fis = new FileInputStream(templateFile);
                     OutputStream os = response.getOutputStream()) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                }
            } else {
                // 从classpath读取模板文件
                try (OutputStream os = response.getOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = templateStream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                }
            }
        }

        log.info("成功下载模板文件: {}", templateFileName);
    }
}
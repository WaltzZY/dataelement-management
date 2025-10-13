package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.AlldataelementinfoMapper;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.OrganisersClaim.ClaimDomainDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.DataElementPageExportDto;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.entity.dto.ExcelRowDto;
import com.inspur.dsp.direct.entity.dto.ImportFailureExportDTO;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.FailureDetailVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.enums.RecordSourceTypeEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import com.inspur.dsp.direct.enums.AlldataelementSortFieldEnums;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Calendar;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 数据元信息相关业务实现类
 *
 * @author Claude Code
 * @since 2025-09-22
 */
@Slf4j
@Service
public class AlldataelementinfoServiceImpl implements AlldataelementinfoService {

    @Autowired
    private AlldataelementinfoMapper alldataelementinfoMapper;

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ClaimDomainDataElementMapper claimDomainDataElementMapper;

    /**
     * 在 Service 层处理日期边界逻辑
     */
    @Override
    public Page<DataElementPageInfoVo> getAllDataElementPage(DataElementPageQueryDto queryDto) {
        // 处理日期边界问题
        normalizeDateRange(queryDto);

        // 校验并规范化排序参数
        validateAndNormalizeSortParams(queryDto);

        // 创建分页对象，从queryDto获取分页参数
        Page<DataElementPageInfoVo> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        List<DataElementPageInfoVo> data = alldataelementinfoMapper.getAllDataElementPage(page, queryDto);

        // 设置状态描述
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(vo -> {
                vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
            });
        }

        return page.setRecords(data);
    }

    /**
     * 规范化日期范围查询参数
     * 解决前端传入年月日、数据库存储年月日时分秒的匹配问题
     */
    private void normalizeDateRange(DataElementPageQueryDto queryDto) {
        // 处理发起时间范围
        if (queryDto.getSendDateBegin() != null) {
            // 开始时间设置为当天 00:00:00
            queryDto.setSendDateBegin(getStartOfDay(queryDto.getSendDateBegin()));
        }

        if (queryDto.getSendDateEnd() != null) {
            // 结束时间设置为当天 23:59:59
            queryDto.setSendDateEnd(getEndOfDay(queryDto.getSendDateEnd()));
        }

        // 处理定源时间范围
        if (queryDto.getConfirmDateBegin() != null) {
            queryDto.setConfirmDateBegin(getStartOfDay(queryDto.getConfirmDateBegin()));
        }

        if (queryDto.getConfirmDateEnd() != null) {
            queryDto.setConfirmDateEnd(getEndOfDay(queryDto.getConfirmDateEnd()));
        }

        log.debug("日期范围规范化完成 - 发起时间: {} ~ {}, 定源时间: {} ~ {}",
                queryDto.getSendDateBegin(), queryDto.getSendDateEnd(),
                queryDto.getConfirmDateBegin(), queryDto.getConfirmDateEnd());
    }

    /**
     * 获取某一天的开始时间 (00:00:00.000)
     */
    private Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取某一天的结束时间 (23:59:59.999)
     */
    private Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    /**
     * 校验并规范化排序参数
     */
    private void validateAndNormalizeSortParams(DataElementPageQueryDto queryDto) {
        String sortField = queryDto.getSortField();
        String sortOrder = queryDto.getSortOrder();

        // 如果没有指定排序字段，使用默认排序
        if (sortField == null || sortField.trim().isEmpty()) {
            queryDto.setSortField(null);
            queryDto.setSortOrder(null);
            return;
        }

        // 校验排序字段是否有效
        if (!AlldataelementSortFieldEnums.isValidSortField(sortField)) {
            log.warn("无效的排序字段: {}, 使用默认排序", sortField);
            queryDto.setSortField(null);
            queryDto.setSortOrder(null);
            return;
        }

        // 校验并规范化排序方向
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            queryDto.setSortOrder("asc"); // 默认升序
        } else {
            String normalizedOrder = sortOrder.trim().toLowerCase();
            if (!normalizedOrder.equals("asc") && !normalizedOrder.equals("desc")) {
                log.warn("无效的排序方向: {}, 使用默认升序", sortOrder);
                queryDto.setSortOrder("asc");
            } else {
                queryDto.setSortOrder(normalizedOrder);
            }
        }

        log.info("使用排序字段: {}, 排序方向: {}", queryDto.getSortField(), queryDto.getSortOrder());
    }

    @Override
    public UploadConfirmResultVo uploadconfirmunitfile(MultipartFile file) {
        log.info("开始处理上传文件: {}", file.getOriginalFilename());

        // 文件校验
        validateFile(file);

        // 读取Excel文件
        List<ExcelRowDto> excelData = commonService.importExcelData(file, ExcelRowDto.class);
        /**
         * Excel读取异常
         */

        // 处理统计
        int totalCount = excelData.size();
        int successCount = 0;
        int failureCount = 0;
        List<FailureDetailVo> failureDetails = new ArrayList<>();

        // 预处理：检测数据完整性、重复数据和冲突数据
        Map<String, List<ExcelRowDto>> duplicateGroups = new HashMap<>();
        Map<String, List<Integer>> conflictGroups = new HashMap<>();
        Set<Integer> invalidRows = new HashSet<>();
        
        preprocessData(excelData, duplicateGroups, conflictGroups, invalidRows, failureDetails);

        // 逐行处理Excel数据
        for (int i = 0; i < excelData.size(); i++) {
            ExcelRowDto row = excelData.get(i);
            int rowNumber = i + 1;
            
            // 如果该行已在预处理中标记为无效，跳过处理
            if (invalidRows.contains(i)) {
                failureCount++;
                continue;
            }
            
            try {
                ProcessResult result = processRow(row, rowNumber, duplicateGroups, conflictGroups, i);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failureCount++;
                    failureDetails.add(result.getFailureDetail());
                }
            } catch (Exception e) {
                log.error("处理第{}行数据时发生异常: {}", rowNumber, e.getMessage(), e);
                failureCount++;
                FailureDetailVo failureDetail = FailureDetailVo.builder()
                        .serialNumber(row.getSerialNumber())
                        .name(row.getElementName())
                        .unit_code(row.getUnitCode())
                        .failReason("系统异常: " + e.getMessage())
                        .build();
                failureDetails.add(failureDetail);
            }
        }

        log.info("文件处理完成, 总数: {}, 成功: {}, 失败: {}",
                totalCount, successCount, failureCount);

        return UploadConfirmResultVo.builder()
                .total(totalCount)
                .sucessQty(successCount)
                .failQty(failureCount)
                .failDetails(failureDetails)
                .build();
    }

    /**
     * 文件校验
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            throw new IllegalArgumentException("文件格式不正确，仅支持xlsx和xls格式");
        }

        if (file.getSize() > 100 * 1024 * 1024) { // 100MB限制
            throw new IllegalArgumentException("文件大小不能超过100MB");
        }
    }

    /**
     * 预处理数据：检测数据完整性、重复数据和冲突数据
     */
    private void preprocessData(List<ExcelRowDto> excelData, 
                               Map<String, List<ExcelRowDto>> duplicateGroups,
                               Map<String, List<Integer>> conflictGroups,
                               Set<Integer> invalidRows,
                               List<FailureDetailVo> failureDetails) {
        
        // 用于记录数据出现位置
        Map<String, Integer> dataFirstOccurrence = new HashMap<>();
        Map<String, Integer> combinationFirstOccurrence = new HashMap<>();
        
        for (int i = 0; i < excelData.size(); i++) {
            ExcelRowDto row = excelData.get(i);
            
            // 6. 数据不完整检查（包括列名写错）
            if (!isDataComplete(row)) {
                invalidRows.add(i);
                FailureDetailVo failureDetail = FailureDetailVo.builder()
                        .serialNumber(row.getSerialNumber())
                        .name(row.getElementName())
                        .unit_code(row.getUnitCode())
                        .failReason("数据不完整（包括列名写错）")
                        .build();
                failureDetails.add(failureDetail);
                continue;
            }
            
            String dataKey = row.getElementName();
            String combinationKey = row.getElementName() + "|||" + row.getUnitCode();
            
            // 4. 数据与其它条目重复检测（完全相同的数据元名称和数源单位组合）
            if (combinationFirstOccurrence.containsKey(combinationKey)) {
                // 标记为重复数据失败
                invalidRows.add(i);
                FailureDetailVo failureDetail = FailureDetailVo.builder()
                        .serialNumber(row.getSerialNumber())
                        .name(row.getElementName())
                        .unit_code(row.getUnitCode())
                        .failReason("数据与其它条目重复，系统已保留首次出现的数据")
                        .build();
                failureDetails.add(failureDetail);
                continue;
            }
            
            // 5. 数据与其它条目冲突检测（同一数据元与不同数源单位的组合）
            if (dataFirstOccurrence.containsKey(dataKey)) {
                // 已经存在同一数据元但不同数源单位的组合
                String existingCombination = null;
                for (String key : combinationFirstOccurrence.keySet()) {
                    if (key.startsWith(dataKey + "|||")) {
                        existingCombination = key;
                        break;
                    }
                }
                
                if (existingCombination != null && !existingCombination.equals(combinationKey)) {
                    // 冲突，将两个条目都标记为失败
                    int firstIndex = combinationFirstOccurrence.get(existingCombination);
                    
                    // 如果首次出现的条目还没有被标记为无效，现在标记并添加失败记录
                    if (!invalidRows.contains(firstIndex)) {
                        invalidRows.add(firstIndex);
                        ExcelRowDto firstRow = excelData.get(firstIndex);
                        FailureDetailVo firstFailure = FailureDetailVo.builder()
                                .serialNumber(firstRow.getSerialNumber())
                                .name(firstRow.getElementName())
                                .unit_code(firstRow.getUnitCode())
                                .failReason("数据与其它条目冲突，基准数据元与数源单位的组合必须唯一")
                                .build();
                        failureDetails.add(firstFailure);
                    }
                    
                    // 标记当前条目为无效并添加失败记录
                    invalidRows.add(i);
                    FailureDetailVo currentFailure = FailureDetailVo.builder()
                            .serialNumber(row.getSerialNumber())
                            .name(row.getElementName())
                            .unit_code(row.getUnitCode())
                            .failReason("数据与其它条目冲突，基准数据元与数源单位的组合必须唯一")
                            .build();
                    failureDetails.add(currentFailure);
                    continue;
                }
            }
            
            // 记录首次出现位置
            dataFirstOccurrence.put(dataKey, i);
            combinationFirstOccurrence.put(combinationKey, i);
        }
    }
    
    /**
     * 检查数据是否完整
     */
    private boolean isDataComplete(ExcelRowDto row) {
        // 检查必要字段是否为空
        return StringUtils.hasText(row.getElementName()) && 
               StringUtils.hasText(row.getUnitCode()) &&
               StringUtils.hasText(row.getSerialNumber());
    }
    
    /**
     * 处理单行数据 - 校验所有条件并收集所有错误信息
     */
    private ProcessResult processRow(ExcelRowDto row, int rowNumber, 
                                   Map<String, List<ExcelRowDto>> duplicateGroups,
                                   Map<String, List<Integer>> conflictGroups,
                                   int currentIndex) {
        log.debug("处理第{}行数据: {}", rowNumber, row);

        List<String> errorMessages = new ArrayList<>();

        // 3. 验证部门是否存在
        OrganizationUnit organizationUnit = null;
        if (StringUtils.hasText(row.getUnitCode())) {
            organizationUnit = commonService.getOrgInfoByOrgCode(row.getUnitCode());
            if (organizationUnit == null) {
                errorMessages.add("数源单位不存在");
            }
        }

        // 2. 根据名称查询数据元
        BaseDataElement element = null;
        if (StringUtils.hasText(row.getElementName())) {
            LambdaQueryWrapper<BaseDataElement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BaseDataElement::getName, row.getElementName());
            element = baseDataElementMapper.selectOne(wrapper);

            if (element == null) {
                errorMessages.add("数据元不存在");
            } else {
                // 1. 检查数据元状态（只有在数据元存在时才检查）
                if (StatusEnums.DESIGNATED_SOURCE.getCode().equals(element.getStatus())) {
                    errorMessages.add("数据元已定源");
                }
            }
        }

        // 如果有任何错误，返回失败结果
        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("\n", errorMessages);
            log.debug("第{}行数据校验失败: {}", rowNumber, combinedErrorMessage);
            return ProcessResult.failure(row, combinedErrorMessage);
        }

        // 所有校验通过，执行更新操作
        updateDataElementStatus(element.getDataid(), row.getUnitCode(), RecordSourceTypeEnums.IMPORT_SOURCE.getCode());

        log.debug("成功处理第{}行数据，数据元ID: {}", rowNumber, element.getDataid());
        return ProcessResult.success();
    }

    /**
     * 统一的更新数据源状态方法
     * @param dataElementId 数据元ID
     * @param unitCode 单位代码
     * @param sourceType 定源方式
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDataElementStatus(String dataElementId, String unitCode, String sourceType) {
        log.info("开始更新数据元状态，数据元ID：{}，单位代码：{}，定源方式：{}", dataElementId, unitCode, sourceType);

        // 获取当前登录用户信息
        log.debug("正在获取当前登录用户信息...");
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 添加用户信息日志
        if (userInfo != null) {
            log.info("获取到当前登录用户信息 - 账号：{}，姓名：{}，机构代码：{}，机构名称：{}",
                    userInfo.getAccount(),
                    userInfo.getName(),
                    userInfo.getOrgCode(),
                    userInfo.getOrgName());
        } else {
            log.warn("未能获取到当前登录用户信息，userInfo为null");
            throw new RuntimeException("获取用户信息失败，请确认用户已登录");
        }

        // 获取当前用户部门对象
        log.debug("正在获取当前用户部门信息，机构代码：{}", userInfo.getOrgCode());
        OrganizationUnit currentUserOrg = commonService.getOrgInfoByOrgCode(userInfo.getOrgCode());
        if (currentUserOrg != null) {
            log.info("获取到当前用户部门信息 - 部门名称：{}，联系人：{}，联系电话：{}",
                    currentUserOrg.getUnitName(),
                    currentUserOrg.getContactName(),
                    currentUserOrg.getContactPhone());
        } else {
            log.warn("未能获取到当前用户部门信息，机构代码：{}", userInfo.getOrgCode());
        }
        // 查询数据元
        BaseDataElement element = baseDataElementMapper.selectById(dataElementId);
        if (element == null) {
            throw new RuntimeException("数据元不存在");
        }

        // 验证数源单位存在性
        OrganizationUnit organizationUnit = commonService.getOrgInfoByOrgCode(unitCode);
        if (organizationUnit == null) {
            throw new RuntimeException("数源单位不存在");
        }

        Date now = new Date();

        // 更新基准数据元信息
        element.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
        element.setSourceUnitCode(unitCode);
        element.setSourceUnitName(organizationUnit.getUnitName());
        element.setConfirmDate(now);
        element.setLastModifyDate(now);
        element.setLastModifyAccount(userInfo.getAccount());

        int updateResult = baseDataElementMapper.updateById(element);
        if (updateResult <= 0) {
            throw new RuntimeException("更新数据元信息失败");
        }

        // 新增定源记录表
        SourceEventRecord record = SourceEventRecord.builder()
                .recordId(UUID.randomUUID().toString())
                .dataElementId(element.getDataid())
                .dataElementName(element.getName())
                .sourceType(sourceType)
                .sourceDate(now)
                .operatorAccount(userInfo.getAccount())
                .contactPhone(organizationUnit.getContactPhone())
                .contactName(organizationUnit.getContactName())
                .sourceUnitCode(unitCode)
                .sourceUnitName(organizationUnit.getUnitName())
                .sendUnitCode(currentUserOrg != null ? currentUserOrg.getUnitCode() : userInfo.getOrgCode())
                .sendUnitName(currentUserOrg != null ? currentUserOrg.getUnitName() : userInfo.getOrgName())
                .build();

        int insertResult = sourceEventRecordMapper.insert(record);
        if (insertResult <= 0) {
            throw new RuntimeException("插入定源事件记录失败");
        }

        log.info("成功更新数据源状态，数据元ID：{}，数源单位：{}，定源方式：{}",
                dataElementId, organizationUnit.getUnitName(), sourceType);
    }

    /**
     * 处理结果封装类
     */
    private static class ProcessResult {
        private boolean success;
        private FailureDetailVo failureDetail;

        private ProcessResult(boolean success, FailureDetailVo failureDetail) {
            this.success = success;
            this.failureDetail = failureDetail;
        }

        public static ProcessResult success() {
            return new ProcessResult(true, null);
        }

        public static ProcessResult failure(ExcelRowDto row, String reason) {
            FailureDetailVo detail = FailureDetailVo.builder()
                    .serialNumber(row.getSerialNumber())
                    .name(row.getElementName())
                    .unit_code(row.getUnitCode())
                    .failReason(reason)
                    .build();
            return new ProcessResult(false, detail);
        }

        public boolean isSuccess() {
            return success;
        }

        public FailureDetailVo getFailureDetail() {
            return failureDetail;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualConfirmUnit(ManualConfirmUnitDto confirmDto) {
        log.info("开始手动定源操作，请求参数：{}", confirmDto);

        try {
            // 调用统一的更新数据源状态方法
            updateDataElementStatus(confirmDto.getDataid(), confirmDto.getSourceUnitId(), RecordSourceTypeEnums.MANUAL_SOURCE.getCode());

            log.info("手动定源操作成功，数据元ID：{}", confirmDto.getDataid());

        } catch (Exception e) {
            log.error("手动定源操作失败，请求参数：{}，错误信息：{}", confirmDto, e.getMessage(), e);
            throw new RuntimeException("手动定源操作失败：" + e.getMessage());
        }
    }
    /**
     * 导出数据元列表
     * @param queryDto 查询条件DTO
     * @param response HttpServletResponse对象
     */
    @Override
    public void exportDataElementList(DataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("开始导出数据元列表，查询条件：{}", queryDto);

        // 直接调用Mapper查询，不使用分页
        List<DataElementPageInfoVo> dataList = alldataelementinfoMapper.getAllDataElementPage(null, queryDto);

        if (CollectionUtils.isEmpty(dataList)) {
            throw new IllegalArgumentException("没有可导出的数据!");
        }

        log.info("查询到{}条数据需要导出", dataList.size());

        // 设置状态描述（因为直接调用Mapper，需要手动设置）
        dataList.forEach(vo -> {
            vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        });

        // 收集采集单位信息
        for (DataElementPageInfoVo vo : dataList) {
            try {
                // 使用dataid查询关联的DomainDataElement列表
                List<DomainDataElement> domainDataElements =
                        claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(vo.getDataid());

                if (!CollectionUtils.isEmpty(domainDataElements)) {
                    // 提取所有source_unit_name并用"|"分割组合（根据OrganisersClaimServiceImpl的实现）
                    String collectUnitNames = domainDataElements.stream()
                            .map(DomainDataElement::getSourceUnitName)
                            .filter(name -> name != null && !name.trim().isEmpty())
                            .collect(Collectors.joining("|"));

                    // 设置采集单位名称
                    vo.setCollectDeptName(collectUnitNames);
                } else {
                    // 如果没有查询到采集单位信息，设置为空字符串
                    vo.setCollectDeptName("");
                }
            } catch (Exception e) {
                log.warn("查询数据元{}的采集单位信息失败: {}", vo.getDataid(), e.getMessage());
                vo.setCollectDeptName("");
            }
        }

        // 转换为导出DTO列表
        List<DataElementPageExportDto> exportList = new ArrayList<>();
        int serialNumber = 1;

        for (DataElementPageInfoVo vo : dataList) {
            DataElementPageExportDto exportDto = new DataElementPageExportDto();
            exportDto.setSerialNumber(String.valueOf(serialNumber++));
            exportDto.setName(vo.getName());
            exportDto.setDefinition(vo.getDefinition());
            exportDto.setStatusDesc(vo.getStatusDesc());
            exportDto.setSourceUnitName(vo.getSourceUnitName());
            exportDto.setCollectDeptName(vo.getCollectDeptName()); // 设置采集单位信息
            exportDto.setSendDate(vo.getSendDate());
            exportDto.setConfirmDate(vo.getConfirmDate());

            exportList.add(exportDto);
        }

        // 调用通用导出服务
        try {
            commonService.exportExcelData(exportList, response, "整体定源情况", DataElementPageExportDto.class);
            log.info("整体定源情况导出完成，共导出{}条记录", exportList.size());
        } catch (IOException e) {
            log.error("导出整体定源情况失败", e);
            throw new RuntimeException("导出整体定源情况失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void exportImportFailureList(List<FailureDetailVo> failureDetails, HttpServletResponse response) {
        log.info("开始导出导入失败清单，失败记录数：{}", failureDetails.size());

        if (CollectionUtils.isEmpty(failureDetails)) {
            throw new IllegalArgumentException("没有失败记录可导出!");
        }

        // 转换为导出DTO列表
        List<ImportFailureExportDTO> exportList = new ArrayList<>();
        
        for (FailureDetailVo failureDetail : failureDetails) {
            ImportFailureExportDTO exportDto = new ImportFailureExportDTO();
            exportDto.setSerialNumber(failureDetail.getSerialNumber());
            exportDto.setName(failureDetail.getName());
            exportDto.setUnitCode(failureDetail.getUnit_code());
            
            // 获取单位名称
            String unitName = "";
            if (failureDetail.getUnit_code() != null && !failureDetail.getUnit_code().trim().isEmpty()) {
                try {
                    OrganizationUnit organizationUnit = commonService.getOrgInfoByOrgCode(failureDetail.getUnit_code());
                    if (organizationUnit != null) {
                        unitName = organizationUnit.getUnitName();
                    }
                } catch (Exception e) {
                    log.warn("获取单位名称失败，单位代码：{}, 错误：{}", failureDetail.getUnit_code(), e.getMessage());
                }
            }
            exportDto.setUnitName(unitName);
            exportDto.setFailReason(failureDetail.getFailReason());
            
            exportList.add(exportDto);
        }

        // 调用通用导出服务
        try {
            commonService.exportExcelData(exportList, response, "导入失败清单（整体定源情况）", ImportFailureExportDTO.class);
            log.info("导入失败清单导出完成，共导出{}条记录", exportList.size());
        } catch (IOException e) {
            log.error("导出导入失败清单失败", e);
            throw new RuntimeException("导出导入失败清单失败: " + e.getMessage(), e);
        }
    }
}
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
import com.inspur.dsp.direct.entity.dto.DetermineResultExcelRowDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.FailureDetailVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.entity.vo.ImportDetermineResultVo;
import com.inspur.dsp.direct.entity.vo.DetermineResultFailureDetailVo;
import com.inspur.dsp.direct.enums.RecordSourceTypeEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TemplateTypeEnums;
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
import java.io.*;
import java.net.URLEncoder;
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

        // 检查解析后的数据是否为空
        if (excelData == null || excelData.isEmpty()) {
            throw new IllegalArgumentException("文件内容为空或格式不正确，请检查文件内容");
        }

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
                failureDetails.add(createFailureDetail(row, "系统异常: " + e.getMessage()));
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

            // 3. 数据不完整检查（包括列名写错）
            if (!isDataComplete(row)) {
                invalidRows.add(i);
                failureDetails.add(createFailureDetail(row, "数据不完整"));
                continue;
            }

            String dataKey = row.getElementName();
            String combinationKey = row.getElementName() + "|||" + row.getUnitCode();

            // 4. 数据与其它条目冲突检测（同一数据元与不同数源单位的组合）
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
                        failureDetails.add(createFailureDetail(firstRow,
                                "数据与其它条目冲突，基准数据元与数源单位的组合必须唯一"));
                    }

                    // 标记当前条目为无效并添加失败记录
                    invalidRows.add(i);
                    failureDetails.add(createFailureDetail(row,
                            "数据与其它条目冲突，基准数据元与数源单位的组合必须唯一"));
                    continue;
                }
            }

            // 5. 数据与其它条目重复检测（完全相同的数据元名称和数源单位组合）
            if (combinationFirstOccurrence.containsKey(combinationKey)) {
                // 标记为重复数据失败
                invalidRows.add(i);
                failureDetails.add(createFailureDetail(row,
                        "数据与其它条目重复，系统已保留首次出现的数据"));
                continue;
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
     * 创建失败详情对象（包含单位名称）
     */
    private FailureDetailVo createFailureDetail(ExcelRowDto row, String failReason) {
        String unitName = "";
        if (StringUtils.hasText(row.getUnitCode())) {
            try {
                OrganizationUnit unit = commonService.getOrgInfoByOrgCode(row.getUnitCode());
                if (unit != null) {
                    unitName = unit.getUnitName();
                }
            } catch (Exception e) {
                log.warn("获取单位名称失败，单位代码: {}, 错误: {}", row.getUnitCode(), e.getMessage());
            }
        }

        return FailureDetailVo.builder()
                .serialNumber(row.getSerialNumber())
                .name(row.getElementName())
                .unit_code(row.getUnitCode())
                .unit_name(unitName)  // 设置单位名称
                .failReason(failReason)
                .build();
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
            return ProcessResult.failure(row, combinedErrorMessage, organizationUnit);
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
     * 定源处理结果封装类
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

        public static ProcessResult failure(ExcelRowDto row, String reason, OrganizationUnit organizationUnit) {
            String unitName = "";
            if (organizationUnit != null) {
                unitName = organizationUnit.getUnitName();
            }

            FailureDetailVo detail = FailureDetailVo.builder()
                    .serialNumber(row.getSerialNumber())
                    .name(row.getElementName())
                    .unit_code(row.getUnitCode())
                    .unit_name(unitName)  // 设置单位名称
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
    public ImportDetermineResultVo importDetermineResult(MultipartFile file) {
        log.info("开始处理定数结果导入文件: {}", file.getOriginalFilename());

        // 1. 文件校验
        validateDetermineResultFile(file);

        // 2. 数据读取
        List<DetermineResultExcelRowDto> excelData = readDetermineResultExcelData(file);

        // 3. 数据预处理
        return processDetermineResultData(excelData);
    }

    /**
     * 定数结果文件校验
     */
    private void validateDetermineResultFile(MultipartFile file) {
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
     * 读取定数结果Excel数据
     */
    private List<DetermineResultExcelRowDto> readDetermineResultExcelData(MultipartFile file) {
        try {
            List<DetermineResultExcelRowDto> excelData = commonService.importExcelData(file, DetermineResultExcelRowDto.class);

            // 检查解析后的数据是否为空
            if (excelData == null || excelData.isEmpty()) {
                throw new IllegalArgumentException("文件内容为空或格式不正确，请检查文件内容");
            }

            return excelData;
        } catch (Exception e) {
            log.error("读取Excel文件失败: {}", e.getMessage(), e);
            throw new IllegalArgumentException("读取Excel文件失败: " + e.getMessage());
        }
    }

    /**
     * 处理定数结果数据
     */
    private ImportDetermineResultVo processDetermineResultData(List<DetermineResultExcelRowDto> excelData) {
        int totalCount = excelData.size();
        int successCount = 0;
        int failureCount = 0;
        List<DetermineResultFailureDetailVo> failureDetails = new ArrayList<>();

        // 数据类型集合（根据需求中提到的数据类型校验）
        Set<String> validDataTypes = getValidDataTypes();

        // 预处理：检测数据完整性、重复数据和冲突数据
        Map<String, List<DetermineResultExcelRowDto>> duplicateGroups = new HashMap<>();
        Map<String, List<Integer>> conflictGroups = new HashMap<>();
        Set<Integer> invalidRows = new HashSet<>();

        preprocessDetermineResultData(excelData, duplicateGroups, conflictGroups, invalidRows, failureDetails, validDataTypes);

        // 逐行处理Excel数据
        for (int i = 0; i < excelData.size(); i++) {
            DetermineResultExcelRowDto row = excelData.get(i);
            int rowNumber = i + 1;

            // 如果该行已在预处理中标记为无效，跳过处理
            if (invalidRows.contains(i)) {
                failureCount++;
                continue;
            }

            try {
                DetermineResultProcessResult result = processDetermineResultRow(row, rowNumber, duplicateGroups, conflictGroups, i, validDataTypes);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failureCount++;
                    failureDetails.add(result.getFailureDetail());
                }
            } catch (Exception e) {
                log.error("处理第{}行数据时发生异常: {}", rowNumber, e.getMessage(), e);
                failureCount++;
                failureDetails.add(createDetermineResultFailureDetail(row, "系统异常: " + e.getMessage()));
            }
        }

        log.info("定数结果文件处理完成, 总数: {}, 成功: {}, 失败: {}",
                totalCount, successCount, failureCount);

        return ImportDetermineResultVo.builder()
                .total(totalCount)
                .successQty(successCount)
                .failQty(failureCount)
                .failDetails(failureDetails)
                .build();
    }

    /**
     * 获取有效的数据类型集合
     */
    private Set<String> getValidDataTypes() {
        // 根据需求设置有效的数据类型，这里可以从配置文件或数据库读取
        Set<String> validTypes = new HashSet<>();
        validTypes.add("字符型");
        validTypes.add("数值型");
        validTypes.add("日期型");
        validTypes.add("时间型");
        validTypes.add("时间日期型");
        validTypes.add("布尔型");
        validTypes.add("二进制");
        // 可以根据实际需求添加更多数据类型
        return validTypes;
    }

    /**
     * 预处理定数结果数据：检测数据完整性、重复数据和冲突数据
     */
    private void preprocessDetermineResultData(List<DetermineResultExcelRowDto> excelData,
                                              Map<String, List<DetermineResultExcelRowDto>> duplicateGroups,
                                              Map<String, List<Integer>> conflictGroups,
                                              Set<Integer> invalidRows,
                                              List<DetermineResultFailureDetailVo> failureDetails,
                                              Set<String> validDataTypes) {

        // 用于记录数据出现位置
        Map<String, Integer> baseElementFirstOccurrence = new HashMap<>();
        Map<String, Integer> combinationFirstOccurrence = new HashMap<>();

        for (int i = 0; i < excelData.size(); i++) {
            DetermineResultExcelRowDto row = excelData.get(i);

            // 1. 数据类型校验
            if (StringUtils.hasText(row.getDataType()) && !validDataTypes.contains(row.getDataType())) {
                invalidRows.add(i);
                failureDetails.add(createDetermineResultFailureDetail(row, "数据类型不规范"));
                continue;
            }

            // 2. 前三列（基准数据元信息）是否有空值
            if (!isBaseElementInfoComplete(row)) {
                invalidRows.add(i);
                failureDetails.add(createDetermineResultFailureDetail(row, "基准数据元信息不完整"));
                continue;
            }

            // 3. 后三列（领域数据元信息）是否有空值
            if (!isDomainElementInfoComplete(row)) {
                invalidRows.add(i);
                failureDetails.add(createDetermineResultFailureDetail(row, "领域数据元信息不完整"));
                continue;
            }

            String baseElementKey = row.getBaseElementName();
            String combinationKey = row.getBaseElementName() + "|||" + row.getUnitCode();

            // 7&8. 基准数据元各类信息冲突检测
            if (baseElementFirstOccurrence.containsKey(baseElementKey)) {
                // 检查是否与之前的基准数据元信息冲突
                DetermineResultExcelRowDto firstRow = excelData.get(baseElementFirstOccurrence.get(baseElementKey));
                if (!isBaseElementInfoConsistent(row, firstRow)) {
                    // 冲突，将两个条目都标记为失败
                    int firstIndex = baseElementFirstOccurrence.get(baseElementKey);

                    if (!invalidRows.contains(firstIndex)) {
                        invalidRows.add(firstIndex);
                        failureDetails.add(createDetermineResultFailureDetail(firstRow,
                                "数据与其它条目冲突，单条基准数据元的各类信息必须具有唯一性"));
                    }

                    invalidRows.add(i);
                    failureDetails.add(createDetermineResultFailureDetail(row,
                            "数据与其它条目冲突，单条基准数据元的各类信息必须具有唯一性"));
                    continue;
                }
            }

            // 9. 数据与其它条目重复检测
            if (combinationFirstOccurrence.containsKey(combinationKey)) {
                // 标记为重复数据失败
                invalidRows.add(i);
                failureDetails.add(createDetermineResultFailureDetail(row,
                        "数据与其它条目重复，系统已保留首次出现的数据"));
                continue;
            }

            // 记录首次出现位置
            baseElementFirstOccurrence.put(baseElementKey, i);
            combinationFirstOccurrence.put(combinationKey, i);
        }
    }

    /**
     * 检查基准数据元信息是否完整（前三列）
     */
    private boolean isBaseElementInfoComplete(DetermineResultExcelRowDto row) {
        return StringUtils.hasText(row.getBaseElementName()) &&
                StringUtils.hasText(row.getDataType()) &&
                StringUtils.hasText(row.getBaseElementDefinition());
    }

    /**
     * 检查领域数据元信息是否完整（后三列）
     */
    private boolean isDomainElementInfoComplete(DetermineResultExcelRowDto row) {
        return StringUtils.hasText(row.getDomainElementName()) &&
                StringUtils.hasText(row.getDomainElementDefinition()) &&
                StringUtils.hasText(row.getUnitCode());
    }

    /**
     * 检查基准数据元信息是否一致
     */
    private boolean isBaseElementInfoConsistent(DetermineResultExcelRowDto row1, DetermineResultExcelRowDto row2) {
        return Objects.equals(row1.getBaseElementName(), row2.getBaseElementName()) &&
                Objects.equals(row1.getDataType(), row2.getDataType()) &&
                Objects.equals(row1.getBaseElementDefinition(), row2.getBaseElementDefinition());
    }

    /**
     * 处理定数结果单行数据
     */
    private DetermineResultProcessResult processDetermineResultRow(DetermineResultExcelRowDto row, int rowNumber,
                                                                  Map<String, List<DetermineResultExcelRowDto>> duplicateGroups,
                                                                  Map<String, List<Integer>> conflictGroups,
                                                                  int currentIndex,
                                                                  Set<String> validDataTypes) {
        log.debug("处理第{}行定数结果数据: {}", rowNumber, row);

        List<String> errorMessages = new ArrayList<>();

        // 4. 导入的定数结果的基准数据源是否已存在
        BaseDataElement existingBaseElement = null;
        if (StringUtils.hasText(row.getBaseElementName())) {
            LambdaQueryWrapper<BaseDataElement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BaseDataElement::getName, row.getBaseElementName());
            existingBaseElement = baseDataElementMapper.selectOne(wrapper);

            if (existingBaseElement != null) {
                errorMessages.add("该基准数据元已存在于系统中");
            }
        }

        // 5. 采集单位统一社会信用代码是否存在
        OrganizationUnit organizationUnit = null;
        if (StringUtils.hasText(row.getUnitCode())) {
            organizationUnit = commonService.getOrgInfoByOrgCode(row.getUnitCode());
            if (organizationUnit == null) {
                errorMessages.add("系统中未找到采集单位统一社会信用代码对应的组织机构");
            }
        }

        // 6. 领域数据元是否已经和基准数据源存在关联
        if (StringUtils.hasText(row.getDomainElementName()) && organizationUnit != null) {
            // 查询是否已存在该领域数据元与其他基准数据元的关联
            if (isDomainElementAlreadyAssociated(row.getDomainElementName(), row.getUnitCode())) {
                errorMessages.add("领域数据元已与系统中其它的基准数据元关联");
            }
        }

        // 10. 导入的领域数据元和基准数据源已有的领域数据元是否重复
        if (StringUtils.hasText(row.getDomainElementName()) && organizationUnit != null) {
            if (isDomainElementDuplicate(row.getDomainElementName(), row.getUnitCode())) {
                errorMessages.add("领域数据元数据元重复");
            }
        }

        // 如果有任何错误，返回失败结果
        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("\n", errorMessages);
            log.debug("第{}行定数结果数据校验失败: {}", rowNumber, combinedErrorMessage);
            return DetermineResultProcessResult.failure(row, combinedErrorMessage);
        }

        // 所有校验通过，执行数据保存操作
        saveDetermineResult(row, organizationUnit);

        log.debug("成功处理第{}行定数结果数据", rowNumber);
        return DetermineResultProcessResult.success();
    }

    /**
     * 检查领域数据元是否已经与其他基准数据元关联
     */
    private boolean isDomainElementAlreadyAssociated(String domainElementName, String unitCode) {
        // 这里需要根据实际的数据库表结构来查询
        // 假设有一个关联表记录基准数据元和领域数据元的关系
        // 具体实现需要根据实际的表结构调整
        try {
            // 示例查询逻辑，需要根据实际情况调整
            LambdaQueryWrapper<DomainDataElement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DomainDataElement::getName, domainElementName)
                    .eq(DomainDataElement::getSourceUnitCode, unitCode);
            
            List<DomainDataElement> existingElements = claimDomainDataElementMapper.selectList(wrapper);
            return !CollectionUtils.isEmpty(existingElements);
        } catch (Exception e) {
            log.warn("检查领域数据元关联状态失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查领域数据元是否重复
     */
    private boolean isDomainElementDuplicate(String domainElementName, String unitCode) {
        // 检查是否存在相同名称和相同采集单位的领域数据元
        try {
            LambdaQueryWrapper<DomainDataElement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DomainDataElement::getName, domainElementName)
                    .eq(DomainDataElement::getSourceUnitCode, unitCode);
            
            long count = claimDomainDataElementMapper.selectCount(wrapper);
            return count > 0;
        } catch (Exception e) {
            log.warn("检查领域数据元重复状态失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 保存定数结果数据
     */

    private void saveDetermineResult(DetermineResultExcelRowDto row, OrganizationUnit organizationUnit) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("获取用户信息失败，请确认用户已登录");
        }

        Date now = new Date();

        // 1. 创建基准数据元
        BaseDataElement baseElement = new BaseDataElement();
        baseElement.setDataid(UUID.randomUUID().toString());
        baseElement.setName(row.getBaseElementName());
        baseElement.setDefinition(row.getBaseElementDefinition());
        baseElement.setDatatype(row.getDataType());
        baseElement.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
        baseElement.setSourceUnitCode(row.getUnitCode());
        baseElement.setSourceUnitName(organizationUnit.getUnitName());
        baseElement.setConfirmDate(now);
        baseElement.setCreateDate(now);
        baseElement.setLastModifyDate(now);
        baseElement.setCreateAccount(userInfo.getAccount());
        baseElement.setLastModifyAccount(userInfo.getAccount());

        int baseResult = baseDataElementMapper.insert(baseElement);
        if (baseResult <= 0) {
            throw new RuntimeException("创建基准数据元失败");
        }

        // 2. 创建领域数据元
        DomainDataElement domainElement = new DomainDataElement();
        domainElement.setDataid(UUID.randomUUID().toString());
        domainElement.setName(row.getDomainElementName());
        domainElement.setDefinition(row.getDomainElementDefinition());
        domainElement.setSourceUnitCode(row.getUnitCode());
        domainElement.setSourceUnitName(organizationUnit.getUnitName());
        domainElement.setDataElementId(baseElement.getDataid());
        domainElement.setCreateDate(now);
        domainElement.setLastModifyDate(now);
        domainElement.setCreateAccount(userInfo.getAccount());
        domainElement.setLastModifyAccount(userInfo.getAccount());

        int domainResult = claimDomainDataElementMapper.insert(domainElement);
        if (domainResult <= 0) {
            throw new RuntimeException("创建领域数据元失败");
        }

        // 3. 新增定源记录表
        OrganizationUnit currentUserOrg = commonService.getOrgInfoByOrgCode(userInfo.getOrgCode());
        SourceEventRecord record = SourceEventRecord.builder()
                .recordId(UUID.randomUUID().toString())
                .dataElementId(baseElement.getDataid())
                .dataElementName(baseElement.getName())
                .sourceType(RecordSourceTypeEnums.IMPORT_DETERMINE_RESULT.getCode())
                .sourceDate(now)
                .operatorAccount(userInfo.getAccount())
                .contactPhone(organizationUnit.getContactPhone())
                .contactName(organizationUnit.getContactName())
                .sourceUnitCode(row.getUnitCode())
                .sourceUnitName(organizationUnit.getUnitName())
                .sendUnitCode(currentUserOrg != null ? currentUserOrg.getUnitCode() : userInfo.getOrgCode())
                .sendUnitName(currentUserOrg != null ? currentUserOrg.getUnitName() : userInfo.getOrgName())
                .build();

        int recordResult = sourceEventRecordMapper.insert(record);
        if (recordResult <= 0) {
            throw new RuntimeException("插入定源事件记录失败");
        }

        log.info("成功保存定数结果，基准数据元：{}，领域数据元：{}，采集单位：{}",
                baseElement.getName(), domainElement.getName(), organizationUnit.getUnitName());
    }

    /**
     * 创建定数结果失败详情对象
     */
    private DetermineResultFailureDetailVo createDetermineResultFailureDetail(DetermineResultExcelRowDto row, String failReason) {
        return DetermineResultFailureDetailVo.builder()
                .serialNumber(String.valueOf(System.nanoTime())) // 使用纳秒时间戳作为序号
                .baseElementName(row.getBaseElementName())
                .dataType(row.getDataType())
                .baseElementDefinition(row.getBaseElementDefinition())
                .failReason(failReason)
                .build();
    }

    /**
     * 定数结果处理结果封装类
     */
    private static class DetermineResultProcessResult {
        private boolean success;
        private DetermineResultFailureDetailVo failureDetail;

        private DetermineResultProcessResult(boolean success, DetermineResultFailureDetailVo failureDetail) {
            this.success = success;
            this.failureDetail = failureDetail;
        }

        public static DetermineResultProcessResult success() {
            return new DetermineResultProcessResult(true, null);
        }

        public static DetermineResultProcessResult failure(DetermineResultExcelRowDto row, String reason) {
            DetermineResultFailureDetailVo detail = DetermineResultFailureDetailVo.builder()
                    .serialNumber(String.valueOf(System.nanoTime()))
                    .baseElementName(row.getBaseElementName())
                    .dataType(row.getDataType())
                    .baseElementDefinition(row.getBaseElementDefinition())
                    .failReason(reason)
                    .build();
            return new DetermineResultProcessResult(false, detail);
        }

        public boolean isSuccess() {
            return success;
        }

        public DetermineResultFailureDetailVo getFailureDetail() {
            return failureDetail;
        }
    }

    @Override
    public void downloadImportTemplate(TemplateTypeEnums templateType, HttpServletResponse response) {
        log.info("开始下载导入模板，模板类型：{}", templateType.getDesc());

        try {
            String templateFileName;
            String downloadFileName;
            
            switch (templateType) {
                case IMPORT_DETERMINE_RESULT:
                    templateFileName = "定数结果导入模板.xlsx";
                    downloadFileName = "导入定数结果模板.xlsx";
                    break;

                case IMPORT_DATASOURCE_RESULT:
                    templateFileName = "定源结果导入模板.xlsx";
                    downloadFileName = "导入定源结果模板.xlsx";
                    break;

                default:
                    throw new IllegalArgumentException("不支持的模板类型：" + templateType.getDesc());
            }

            // 从项目根目录读取模板文件
            downloadTemplateFile(templateFileName, downloadFileName, response);

            log.info("模板下载完成，模板类型：{}", templateType.getDesc());
        } catch (Exception e) {
            log.error("下载模板失败，模板类型：{}", templateType.getDesc(), e);
            throw new RuntimeException("下载模板失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从项目根目录下载模板文件
     */
    private void downloadTemplateFile(String templateFileName, String downloadFileName, HttpServletResponse response) throws IOException {
        // 获取项目根目录路径
        String projectRoot = System.getProperty("user.dir");
        String templateFilePath = projectRoot + File.separator + templateFileName;
        
        File templateFile = new File(templateFilePath);
        if (!templateFile.exists()) {
            throw new RuntimeException("模板文件不存在: " + templateFilePath);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + 
            URLEncoder.encode(downloadFileName, "UTF-8"));

        // 读取模板文件并写入响应流
        try (FileInputStream fis = new FileInputStream(templateFile);
             OutputStream os = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
        
        log.info("成功下载模板文件: {}", templateFileName);
    }
}
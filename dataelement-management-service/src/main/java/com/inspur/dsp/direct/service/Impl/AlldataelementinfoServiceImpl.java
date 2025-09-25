package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.AlldataelementinfoMapper;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.OrganisersClaim.ClaimDomainDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dao.OrganizationUnitMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.DataElementPageExportDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.entity.dto.ExcelRowDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.entity.vo.FailureDetailVo;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.AlldataelementSortFieldEnums;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private OrganizationUnitMapper organizationUnitMapper;

    @Override
    public Page<DataElementPageInfoVo> getAllDataElementPage(DataElementPageQueryDto queryDto) {
        log.info("开始执行数据元列表查询，查询条件：{}", queryDto);

        // 关键字去空格处理
        if (StringUtils.isNotBlank(queryDto.getKeyword())) {
            queryDto.setKeyword(queryDto.getKeyword().trim());
            log.info("关键字去空格后：{}", queryDto.getKeyword());
        }

        // 排序参数校验和处理
        validateAndProcessSortParams(queryDto);

        // 创建分页对象，从queryDto获取分页参数
        Page<DataElementPageInfoVo> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        List<DataElementPageInfoVo> data = alldataelementinfoMapper.getAllDataElementPage(page, queryDto);

        // 设置状态描述
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(vo -> {
                vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
            });
        }

        log.info("数据元列表查询完成，返回{}条记录", data.size());
        return page.setRecords(data);
    }

    @Override
    public void exportDataElementList(DataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("开始导出数据元列表，查询条件：{}", queryDto);

        // 关键字去空格处理
        if (StringUtils.isNotBlank(queryDto.getKeyword())) {
            queryDto.setKeyword(queryDto.getKeyword().trim());
            log.info("导出时关键字去空格后：{}", queryDto.getKeyword());
        }

        // 排序参数校验和处理
        validateAndProcessSortParams(queryDto);

        // 不分页，获取所有符合条件的数据
        queryDto.setPageNum(1L);
        queryDto.setPageSize(Long.MAX_VALUE);

        // 直接调用Mapper方法获取数据
        Page<DataElementPageInfoVo> page = new Page<>(1, Integer.MAX_VALUE);
        List<DataElementPageInfoVo> dataList = alldataelementinfoMapper.getAllDataElementPage(page, queryDto);

        if (CollectionUtils.isEmpty(dataList)) {
            log.warn("没有查询到符合条件的数据，无法导出");
            throw new IllegalArgumentException("没有可导出的数据!");
        }

        log.info("查询到{}条数据需要导出", dataList.size());

        // 设置状态描述（因为直接调用Mapper，需要手动设置）
        dataList.forEach(vo -> {
            vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        });

        // 批量查询采集单位信息
        List<String> dataIds = dataList.stream()
                .map(DataElementPageInfoVo::getDataid)
                .collect(Collectors.toList());

        // 查询每个数据元的采集单位
        Map<String, String> collectDeptMap = getCollectDeptNames(dataIds);

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

            // 设置采集单位名称（从domain_data_element表查询获得）
            String collectDeptName = collectDeptMap.get(vo.getDataid());
            exportDto.setCollectDeptName(collectDeptName);

            exportDto.setSendDate(vo.getSendDate());
            exportDto.setConfirmDate(vo.getConfirmDate());
            exportDto.setDataid(vo.getDataid());

            exportList.add(exportDto);
        }

        // 调用通用导出服务
        try {
            commonService.exportExcelData(exportList, response, "数据元列表", DataElementPageExportDto.class);
            log.info("数据元列表导出完成，共导出{}条记录", exportList.size());
        } catch (IOException e) {
            log.error("导出数据元列表失败", e);
            throw new RuntimeException("导出数据元列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadConfirmResultVo uploadconfirmunitfile(MultipartFile file) {
        log.info("开始处理上传文件: {}", file.getOriginalFilename());

        // 文件校验
        validateFile(file);

        // 读取Excel文件
        List<ExcelRowDto> excelData = commonService.importExcelData(file, ExcelRowDto.class);

        if (CollectionUtils.isEmpty(excelData)) {
            throw new IllegalArgumentException("Excel文件内容为空!");
        }

        // 处理统计
        int totalCount = excelData.size();
        int successCount = 0;
        int failureCount = 0;
        List<FailureDetailVo> failureDetails = new ArrayList<>();

        // 逐行处理Excel数据
        for (int i = 0; i < excelData.size(); i++) {
            ExcelRowDto row = excelData.get(i);
            try {
                ProcessResult result = processRow(row, i + 1);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failureCount++;
                    FailureDetailVo failure = new FailureDetailVo();
                    failure.setSerialNumber(String.valueOf(i + 1));
                    failure.setName(row.getElementName());           // name字段
                    failure.setUnit_code(row.getUnitCode());         // unit_code字段
                    failure.setUnit_name(result.getUnitName());      // unit_name字段
                    failure.setFailReason(result.getFailureReason()); // failReason字段
                    failureDetails.add(failure);
                }
            } catch (Exception e) {
                log.error("处理第{}行数据失败", i + 1, e);
                failureCount++;
                FailureDetailVo failure = new FailureDetailVo();
                failure.setSerialNumber(String.valueOf(i + 1));
                failure.setName(row.getElementName());
                failure.setUnit_code(row.getUnitCode());
                failure.setFailReason("处理异常: " + e.getMessage());
                failureDetails.add(failure);
            }
        }

        // 构建返回结果
        UploadConfirmResultVo result = new UploadConfirmResultVo();
        result.setTotal(totalCount);
        result.setSucessQty(successCount);
        result.setFailQty(failureCount);
        result.setFailDetails(failureDetails);

        log.info("文件处理完成，总记录数：{}，成功：{}，失败：{}", totalCount, successCount, failureCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualConfirmUnit(ManualConfirmUnitDto confirmDto) {
        log.info("开始执行手动定源，参数：{}", confirmDto);

        // 校验数据元是否存在
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(confirmDto.getDataid());
        if (baseDataElement == null) {
            throw new IllegalArgumentException("数据元不存在!");
        }

        // 校验数源单位是否存在
        OrganizationUnit organizationUnit = organizationUnitMapper.selectById(confirmDto.getSourceUnitId());
        if (organizationUnit == null) {
            throw new IllegalArgumentException("数源单位不存在!");
        }

        // 获取当前用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 更新数据元信息
        baseDataElement.setSourceUnitCode(organizationUnit.getUnitCode());
        baseDataElement.setSourceUnitName(organizationUnit.getUnitName());
        baseDataElement.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
        baseDataElement.setConfirmDate(new Date());
        baseDataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElement.setLastModifyDate(new Date());

        // 保存更新
        int updateCount = baseDataElementMapper.updateById(baseDataElement);
        if (updateCount == 0) {
            throw new RuntimeException("手动定源失败，更新数据元信息失败");
        }

        log.info("手动定源完成，数据元ID：{}，数源单位：{}({})",
                confirmDto.getDataid(), organizationUnit.getUnitName(), organizationUnit.getUnitCode());
    }

    /**
     * 批量查询采集单位名称
     *
     * @param dataIds 数据元ID列表
     * @return 数据元ID -> 采集单位名称的映射
     */
    private Map<String, String> getCollectDeptNames(List<String> dataIds) {
        if (CollectionUtils.isEmpty(dataIds)) {
            return Collections.emptyMap();
        }

        Map<String, String> collectDeptMap = dataIds.stream()
                .collect(Collectors.toMap(
                        dataId -> dataId,
                        dataId -> {
                            try {
                                // 根据基准数据元ID查询领域数据元列表
                                List<DomainDataElement> domainElements =
                                        claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(dataId);

                                if (CollectionUtils.isEmpty(domainElements)) {
                                    return ""; // 没有找到采集单位
                                }

                                // 提取所有采集单位名称并用"|"分隔合并
                                return domainElements.stream()
                                        .map(DomainDataElement::getSourceUnitName)
                                        .filter(StringUtils::isNotBlank)
                                        .distinct() // 去重
                                        .collect(Collectors.joining("|"));

                            } catch (Exception e) {
                                log.warn("查询数据元{}的采集单位失败: {}", dataId, e.getMessage());
                                return "";
                            }
                        }
                ));

        log.info("批量查询采集单位完成，共查询{}个数据元", collectDeptMap.size());
        return collectDeptMap;
    }

    /**
     * 校验和处理排序参数
     *
     * @param queryDto 查询条件DTO
     */
    private void validateAndProcessSortParams(DataElementPageQueryDto queryDto) {
        String sortField = queryDto.getSortField();
        String sortOrder = queryDto.getSortOrder();

        // 如果排序字段为空，不需要处理
        if (StringUtils.isBlank(sortField)) {
            return;
        }

        // 使用枚举校验排序字段
        AlldataelementSortFieldEnums sortFieldEnum = AlldataelementSortFieldEnums.getByField(sortField);
        if (sortFieldEnum == null) {
            log.warn("不支持的排序字段：{}，重置为默认排序", sortField);
            queryDto.setSortField(null);
            queryDto.setSortOrder(null);
            return;
        }

        // 校验排序方向
        if (StringUtils.isNotBlank(sortOrder)) {
            sortOrder = sortOrder.toLowerCase();
            if (!"asc".equals(sortOrder) && !"desc".equals(sortOrder)) {
                log.warn("不支持的排序方向：{}，重置为默认排序", sortOrder);
                queryDto.setSortField(null);
                queryDto.setSortOrder(null);
                return;
            }
            queryDto.setSortOrder(sortOrder);
        } else {
            // 如果没有指定排序方向，默认为升序
            queryDto.setSortOrder("asc");
        }

        log.info("排序参数校验通过，排序字段：{}({})，排序方向：{}",
                queryDto.getSortField(), sortFieldEnum.getDescription(), queryDto.getSortOrder());
    }

    /**
     * 处理Excel行数据
     *
     * @param row      Excel行数据
     * @param rowIndex 行号
     * @return 处理结果
     */
    private ProcessResult processRow(ExcelRowDto row, int rowIndex) {
        try {
            // 数据校验
            if (StringUtils.isBlank(row.getElementName())) {
                return ProcessResult.failure("数据元名称不能为空");
            }

            if (StringUtils.isBlank(row.getUnitCode())) {
                return ProcessResult.failure("数源单位统一社会信用代码不能为空");
            }

            // 查找数据元
            BaseDataElement baseDataElement = baseDataElementMapper.selectFirstByName(row.getElementName());
            if (baseDataElement == null) {
                return ProcessResult.failure("数据元不存在");
            }

            // 查找数源单位
            OrganizationUnit organizationUnit = organizationUnitMapper.selectFirstByUnitCode(row.getUnitCode());
            if (organizationUnit == null) {
                return ProcessResult.failure("数源单位不存在");
            }

            // 获取当前用户信息
            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

            // 更新数据元信息
            baseDataElement.setSourceUnitCode(organizationUnit.getUnitCode());
            baseDataElement.setSourceUnitName(organizationUnit.getUnitName());
            baseDataElement.setStatus(StatusEnums.DESIGNATED_SOURCE.getCode());
            baseDataElement.setConfirmDate(new Date());
            baseDataElement.setLastModifyAccount(userInfo.getAccount());
            baseDataElement.setLastModifyDate(new Date());

            // 保存更新
            int updateCount = baseDataElementMapper.updateById(baseDataElement);
            if (updateCount == 0) {
                return ProcessResult.failure("更新数据元失败");
            }

            return ProcessResult.success();

        } catch (Exception e) {
            log.error("处理第{}行数据异常", rowIndex, e);
            return ProcessResult.failure("处理异常: " + e.getMessage());
        }
    }

    /**
     * 文件校验
     *
     * @param file 上传的文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName) ||
                (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            throw new IllegalArgumentException("文件格式不正确，请上传Excel文件");
        }

        // 文件大小校验（10MB限制）
        long maxFileSize = 10 * 1024 * 1024;
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
    }

    /**
     * 处理结果内部类
     */
    @Data
    public static class ProcessResult {

        /**
         * 是否成功
         */
        private boolean success;

        /**
         * 失败原因
         */
        private String failureReason;

        /**
         * 单位名称
         */
        private String unitName;

        private ProcessResult(boolean success, String failureReason) {
            this.success = success;
            this.failureReason = failureReason;
        }

        private ProcessResult(boolean success, String failureReason, String unitName) {
            this.success = success;
            this.failureReason = failureReason;
            this.unitName = unitName;
        }

        public static ProcessResult success() {
            return new ProcessResult(true, null);
        }

        public static ProcessResult failure(String reason) {
            return new ProcessResult(false, reason);
        }

        public static ProcessResult failure(String reason, String unitName) {
            return new ProcessResult(false, reason, unitName);
        }
    }
}
package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.AlldataelementinfoMapper;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.ExcelRowDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// 在AlldataelementinfoServiceImpl.java文件顶部添加以下导入语句：

import com.inspur.dsp.direct.entity.dto.DataElementPageExportDto;
import java.util.ArrayList;

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

    @Override
    public Page<DataElementPageInfoVo> getAllDataElementPage(DataElementPageQueryDto queryDto) {
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

    @Override
    public UploadConfirmResultVo uploadconfirmunitfile(MultipartFile file) {
        log.info("开始处理上传文件: {}", file.getOriginalFilename());

        // 文件校验
        validateFile(file);

        // 读取Excel文件
        List<ExcelRowDto> excelData = commonService.importExcelData(file, ExcelRowDto.class);

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
                    failureDetails.add(result.getFailureDetail());
                }
            } catch (Exception e) {
                log.error("处理第{}行数据时发生异常: {}", i + 1, e.getMessage(), e);
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
     * 处理单行数据 TODO 后续是否要修改成，所有情况都校验一遍，返回所有校验失败的结果。
     */
    private ProcessResult processRow(ExcelRowDto row, int rowNumber) {
        log.debug("处理第{}行数据: {}", rowNumber, row);

        // 数据校验
        if (!StringUtils.hasText(row.getElementName())) {
            return ProcessResult.failure(row, "数据元名称不能为空");
        }

        if (!StringUtils.hasText(row.getUnitCode())) {
            return ProcessResult.failure(row, "数源单位统一社会信用代码不能为空");
        }

        // 验证部门是否存在 - 使用commonService方法
        OrganizationUnit organizationUnit = commonService.getOrgInfoByOrgCode(row.getUnitCode());
        if (organizationUnit == null) {
            return ProcessResult.failure(row, "数源单位不存在");
        }

        // 根据名称查询数据元
        LambdaQueryWrapper<BaseDataElement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseDataElement::getName, row.getElementName());
        BaseDataElement element = baseDataElementMapper.selectOne(wrapper);

        if (element == null) {
            return ProcessResult.failure(row, "数据元不存在");
        }

        // 检查数据元状态
        if (StatusEnums.DESIGNATED_SOURCE.getCode().equals(element.getStatus())) {
            return ProcessResult.failure(row, "数据元已定源");
        }

        // 调用统一的更新数据源状态方法
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

        //直接调用Mapper查询，不使用分页
        List<DataElementPageInfoVo> dataList = alldataelementinfoMapper.getAllDataElementPage(null, queryDto);


        if (CollectionUtils.isEmpty(dataList)) {
            throw new IllegalArgumentException("没有可导出的数据!");
        }

        log.info("查询到{}条数据需要导出", dataList.size());

        // 设置状态描述（因为直接调用Mapper，需要手动设置）
        dataList.forEach(vo -> {
            vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        });

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
            exportDto.setCollectDeptName(vo.getCollectDeptName());
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


}
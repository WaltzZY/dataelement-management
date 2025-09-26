package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.Processed.ProcessedGetDataPendingAndProcessedSourceMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.GetProcessedDataElementDTO;
import com.inspur.dsp.direct.entity.excel.ProcessedExcel;
import com.inspur.dsp.direct.entity.vo.GetProcessedDataElementVO;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.PapsSortFieldEnums;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.ProcessedService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 已处理数据元服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProcessedServiceImpl implements ProcessedService {

    private final ProcessedGetDataPendingAndProcessedSourceMapper getDataPendingAndProcessedSourceMapper;

    private final CommonService commonService;

    /**
     * 获取已处理的数据元列表
     * 核心流程：
     * 1. 构建GetProcessedDataElementVO，列是从base_data_element、confirmation_task联合查询得到
     * 2. 查询条件：base_data_element.dataid = confirmation_task.base_dataelement_dataid
     * 3. 过滤条件：confirmation_task.status在指定状态集合中且processing_unit_code为当前部门统一社会信用代码
     */

    @Override
    public Page<GetProcessedDataElementVO> getProcessedDataElement(GetProcessedDataElementDTO dto) {

        // 获取当前登录用户
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String orgCode = userInfo.getOrgCode();

        String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());

        Page<GetProcessedDataElementVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        if (dto.getSendDateBegin() != null) {
            dto.setSendDateBegin(dto.getSendDateBegin().toLocalDate().atStartOfDay());
        }
        if (dto.getSendDateEnd() != null) {
            dto.setSendDateEnd(dto.getSendDateEnd().toLocalDate().plusDays(1).atStartOfDay());
        }
        if (dto.getProcessDateBegin() != null) {
            dto.setProcessDateBegin(dto.getProcessDateBegin().toLocalDate().atStartOfDay());
        }
        if (dto.getProcessDateEnd() != null) {
            dto.setProcessDateEnd(dto.getProcessDateEnd().toLocalDate().plusDays(1).atStartOfDay());
        }

        Page<GetProcessedDataElementVO> processedDataElement = getDataPendingAndProcessedSourceMapper.getProcessedDataElement(page, dto, orgCode,orderBySql);
        for (GetProcessedDataElementVO vo : processedDataElement.getRecords()) {
            vo.setStatusDesc(ConfirmationTaskEnums.getDescByCode(vo.getStatus()));
        }
        return processedDataElement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportData(GetProcessedDataElementDTO dto, HttpServletResponse response) {
        try {
            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
            String orgCode = userInfo.getOrgCode();

            String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());

            List<GetProcessedDataElementVO> processedDataElement = getDataPendingAndProcessedSourceMapper.getProcessedDataElement( dto, orgCode,orderBySql);

            List<ProcessedExcel> processedExcelList = processedDataElement.stream().map(vo -> {
                return ProcessedExcel.builder()
                        .name(vo.getName())
                        .definition(vo.getDefinition())
                        .type(vo.getDatatype())
                        .status(ConfirmationTaskEnums.getDescByCode(vo.getStatus()))
                        .sendDate(vo.getSendDate())
                        .processDate(vo.getProcessingDate())
                        .build();
            }).collect(Collectors.toList());
            // 使用EasyExcel导出
            commonService.exportExcelData(processedExcelList, response, "查看已处理-查看已处理数据元列表", ProcessedExcel.class);

        }catch (Exception e) {  throw new RuntimeException("导出数据失败");}
    }
}
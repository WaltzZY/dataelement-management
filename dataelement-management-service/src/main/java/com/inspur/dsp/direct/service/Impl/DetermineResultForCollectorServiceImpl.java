package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.DetermineResultForCollectorExportDTO;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.DetermineResultForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 采集方查看已定源结果服务实现类
 */
@Slf4j
@Service
public class DetermineResultForCollectorServiceImpl implements DetermineResultForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

    @Autowired
    private CommonService commonService;

    /**
     * 获取指定已定源结果列表数据
     */
    @Override
    public Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        // 调用Mapper方法查询基准数据元列表
        Page<BaseDataElement> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());

        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_unit_code", orgCode);
        queryWrapper.eq("status", "designated_source");

        String sendDateBegin = baseDataElementSearchDTO.getSendDateBegin();
        String sendDateEnd = baseDataElementSearchDTO.getSendDateEnd();
        if (StringUtils.isNotBlank(sendDateBegin) && StringUtils.isNotBlank(sendDateEnd)) {
            queryWrapper.between("send_date", sendDateBegin, sendDateEnd);
        }

        Page<BaseDataElement> baseDataElementPage = baseDataElementMapper.selectPage(page, queryWrapper);
        List<BaseDataElement> records = baseDataElementPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            page.setRecords(Collections.emptyList());
        }
        for (BaseDataElement baseDataElement : records) {
            String status = baseDataElement.getStatus();
            String statusChinese = StatusUtil.getStatusChinese(status);
            baseDataElement.setStatusChinese(statusChinese);
        }
        return page.setRecords(records);
    }


    /**
     * 导出指定已定源结果数据
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @param response                 HTTP响应对象
     */
    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {

        // 调用Mapper方法查询基准数据元列表
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_unit_code", orgCode);
        queryWrapper.eq("status", "designated_source");

        String sendDateBegin = baseDataElementSearchDTO.getSendDateBegin();
        String sendDateEnd = baseDataElementSearchDTO.getSendDateEnd();
        if (StringUtils.isNotBlank(sendDateBegin) && StringUtils.isNotBlank(sendDateEnd)) {
            queryWrapper.between("send_date", sendDateBegin, sendDateEnd);
        }

        List<BaseDataElement> baseDataElements = baseDataElementMapper.selectList(queryWrapper);
        if (baseDataElements.isEmpty()) {
            log.error("当前导出数据为空!");
            return;
        }
        List<DetermineResultForCollectorExportDTO> exportDTOList = new ArrayList<>();
        for (BaseDataElement baseDataElement : baseDataElements) {
            DetermineResultForCollectorExportDTO determineResultForCollectorExportDTO = new DetermineResultForCollectorExportDTO();
            determineResultForCollectorExportDTO.setCollectunitqty(baseDataElement.getCollectunitqty());
            determineResultForCollectorExportDTO.setName(baseDataElement.getName());
            determineResultForCollectorExportDTO.setDatatype(baseDataElement.getDatatype());
            determineResultForCollectorExportDTO.setSendDate(baseDataElement.getSendDate());
            determineResultForCollectorExportDTO.setDefinition(baseDataElement.getDefinition());
            exportDTOList.add(determineResultForCollectorExportDTO);
        }
        // 调用CommonService.exportExcelData
        try {
            commonService.exportExcelData(exportDTOList, response, "采集方-本单元作为数源单位的数据元列表", DetermineResultForCollectorExportDTO.class);
        } catch (IOException e) {
            log.error("导出数据[待协商数据]失败", e);
            throw new RuntimeException("导出数据[待协商数据]失败");
        }


    }

}
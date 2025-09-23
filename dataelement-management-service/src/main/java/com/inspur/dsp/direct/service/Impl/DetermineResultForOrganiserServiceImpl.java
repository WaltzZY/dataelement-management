package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.DetermineResultForOrganiserExportDTO;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.DetermineResultForOrganiserService;
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
 * 组织方查看已定源结果服务实现类
 *
 * @author system
 * @date 2025-01-25
 */
@Slf4j
@Service
public class DetermineResultForOrganiserServiceImpl implements DetermineResultForOrganiserService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private CommonService commonService;

    @Override
    public Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        try {
            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
            // 获取登录人账户
            // String orgCode = userInfo.getOrgCode();
            QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
            String sendDateBegin = baseDataElementSearchDTO.getSendDateBegin();
            String sendDateEnd = baseDataElementSearchDTO.getSendDateEnd();
            if (StringUtils.isNotBlank(sendDateBegin) && StringUtils.isNotBlank(sendDateEnd)) {
                queryWrapper.between("send_date", sendDateBegin, sendDateEnd);
            }

            List<String> sourceUnitCodeList = baseDataElementSearchDTO.getSourceUnitCodeList();
            if (sourceUnitCodeList != null && !sourceUnitCodeList.isEmpty()) {
                queryWrapper.in("source_unit_code", sourceUnitCodeList);
            }

            String keyword = baseDataElementSearchDTO.getKeyword();
            String value = baseDataElementSearchDTO.getValue();
            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.like(keyword, value);
            }
            // queryWrapper.eq("source_unit_code", orgCode);
            queryWrapper.eq("status", "designated_source");
            Page<BaseDataElement> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());
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
            return page;
        } catch (Exception e) {
            throw new RuntimeException("查询已定源结果列表失败", e);
        }
    }


    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {
        try {
            // 获取登录人账户
            // String orgCode = userInfo.getOrgCode();
            QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
            String sendDateBegin = baseDataElementSearchDTO.getSendDateBegin();
            String sendDateEnd = baseDataElementSearchDTO.getSendDateEnd();
            if (StringUtils.isNotBlank(sendDateBegin) && StringUtils.isNotBlank(sendDateEnd)) {
                queryWrapper.between("send_date", sendDateBegin, sendDateEnd);
            }

            List<String> sourceUnitCodeList = baseDataElementSearchDTO.getSourceUnitCodeList();
            if (sourceUnitCodeList != null && !sourceUnitCodeList.isEmpty()) {
                queryWrapper.in("source_unit_code", sourceUnitCodeList);
            }

            String keyword = baseDataElementSearchDTO.getKeyword();
            String value = baseDataElementSearchDTO.getValue();
            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.like(keyword, value);
            }
            // queryWrapper.eq("source_unit_code", orgCode);
            queryWrapper.eq("status", "designated_source");
            List<BaseDataElement> baseDataElements = baseDataElementMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(baseDataElements)) {
                return;
            }
            List<DetermineResultForOrganiserExportDTO> exportDTOList = new ArrayList<>();
            for (BaseDataElement baseDataElement : baseDataElements) {
                String statusChinese = StatusUtil.getStatusChinese(baseDataElement.getStatus());
                DetermineResultForOrganiserExportDTO dto = new DetermineResultForOrganiserExportDTO();
                dto.setName(baseDataElement.getName());
                dto.setStatus(statusChinese);
                dto.setDefinition(baseDataElement.getDefinition());
                dto.setCollectunitqty(baseDataElement.getCollectunitqty());
                dto.setPublicDate(baseDataElement.getPublishDate());
                dto.setSendDate(baseDataElement.getSendDate());
                dto.setSourceUnitName(baseDataElement.getSourceUnitName());
                exportDTOList.add(dto);
            }
            try {
                commonService.exportExcelData(exportDTOList, response, "组织方-已定源数据", DetermineResultForOrganiserExportDTO.class);
            } catch (IOException e) {
                log.error("导出数据[组织方-已定源数据]失败", e);
                throw new RuntimeException("导出数据[组织方-已定源数据]失败");
            }

        } catch (Exception e) {
            throw new RuntimeException("查询组织方-已定源数据列表失败", e);
        }
    }
}
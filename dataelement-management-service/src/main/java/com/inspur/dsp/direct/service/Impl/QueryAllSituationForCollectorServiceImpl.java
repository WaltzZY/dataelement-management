package com.inspur.dsp.direct.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.QueryAllSituationForCollectorExportDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.enums.NePageSortFieldEnums;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.QueryAllSituationForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 采集方查询整体情况服务实现类
 *
 * @author system
 */
@Slf4j
@Service
public class QueryAllSituationForCollectorServiceImpl implements QueryAllSituationForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private CommonService commonService;

    /**
     * 分页查询列表数据
     */
    @Override
    public Page<DataElementWithTaskVo> getAllSituationList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        String sortSql = NePageSortFieldEnums.getOrderByField(baseDataElementSearchDTO.getSortField(), baseDataElementSearchDTO.getSortOrder());
        baseDataElementSearchDTO.setSortSql(sortSql);
        log.warn("getAllSituationList函数接收到的参数为: {}", JSONObject.toJSONString(baseDataElementSearchDTO));
        Page<DataElementWithTaskVo> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        baseDataElementSearchDTO.setOrgCode(orgCode);
        List<DataElementWithTaskVo> baseDataElementList = baseDataElementMapper.getDetermineResultListWithOrganiser(page, baseDataElementSearchDTO);
        // 校验结果
        if (CollectionUtils.isEmpty(baseDataElementList)) {
            page.setRecords(Collections.emptyList());
        }
        for (DataElementWithTaskVo dataElementWithTaskVo : baseDataElementList) {
            String ctStatus = dataElementWithTaskVo.getCtStatus();
            String bdeStatus = dataElementWithTaskVo.getBdeStatus();
            String displayStatus = "";
            if ("pending".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("pending_claimed".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("confirmed".equals(ctStatus) && "pending_approval".equals(bdeStatus)) {
                displayStatus = "confirmed";
            } else if ("rejected".equals(ctStatus) && "pending_negotiation".equals(bdeStatus)) {
                displayStatus = "rejected";
            } else if ("claimed".equals(ctStatus) && ("claimed_ing".equals(bdeStatus) || "pending_approval".equals(bdeStatus) || "pending_negotiation".equals(bdeStatus))) {
                displayStatus = "claimed";
            } else if ("not_claimed".equals(ctStatus) && ("claimed_ing".equals(bdeStatus) || "pending_approval".equals(bdeStatus) || "pending_negotiation".equals(bdeStatus))) {
                displayStatus = "not_claimed";
            } else if ("negotiating".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("designated_source".equals(ctStatus)) {
                displayStatus = ctStatus;
            }
            String statusChinese = StatusUtil.getStatusChinese(displayStatus);
            dataElementWithTaskVo.setDisplaystatus(statusChinese);
        }
        return page.setRecords(baseDataElementList);
    }


    /**
     * 下载数据
     */
    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {
        String sortSql = NePageSortFieldEnums.getOrderByField(baseDataElementSearchDTO.getSortField(), baseDataElementSearchDTO.getSortOrder());
        baseDataElementSearchDTO.setSortSql(sortSql);
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        baseDataElementSearchDTO.setOrgCode(orgCode);
        List<DataElementWithTaskVo> dataElementWithTaskVoList = baseDataElementMapper.getDetermineResultListWithOrganiser(baseDataElementSearchDTO);
        // 校验结果
        if (CollectionUtils.isEmpty(dataElementWithTaskVoList)) {
            log.error("导出错误!");
            return;
        }
        List<QueryAllSituationForCollectorExportDTO> exportDTOList = new ArrayList<>();
        for (int i = 0; i < dataElementWithTaskVoList.size(); i++) {
            DataElementWithTaskVo dataElementWithTaskVo = dataElementWithTaskVoList.get(i);
            QueryAllSituationForCollectorExportDTO exportDTO = new QueryAllSituationForCollectorExportDTO();
            exportDTO.setId(i + 1);
            exportDTO.setDefinition(dataElementWithTaskVo.getDefinition());
            exportDTO.setName(dataElementWithTaskVo.getName());
            String ctStatus = dataElementWithTaskVo.getCtStatus();
            String bdeStatus = dataElementWithTaskVo.getBdeStatus();
            String displayStatus = "";
            if ("pending".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("pending_claimed".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("confirmed".equals(ctStatus) && "pending_approval".equals(bdeStatus)) {
                displayStatus = "confirmed";
            } else if ("rejected".equals(ctStatus) && "pending_negotiation".equals(bdeStatus)) {
                displayStatus = "rejected";
            } else if ("claimed".equals(ctStatus) && ("claimed_ing".equals(bdeStatus) || "pending_approval".equals(bdeStatus) || "pending_negotiation".equals(bdeStatus))) {
                displayStatus = "claimed";
            } else if ("not_claimed".equals(ctStatus) && ("claimed_ing".equals(bdeStatus) || "pending_approval".equals(bdeStatus) || "pending_negotiation".equals(bdeStatus))) {
                displayStatus = "not_claimed";
            } else if ("negotiating".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else if ("designated_source".equals(ctStatus)) {
                displayStatus = ctStatus;
            } else {
                displayStatus = "nothing";
            }
            String statusChinese = StatusUtil.getStatusChinese(displayStatus);
            exportDTO.setStatus(statusChinese);
            exportDTO.setSendDate(dataElementWithTaskVo.getSendDate());
            exportDTO.setReceiveDate(dataElementWithTaskVo.getReceiveDate());
            exportDTO.setProcessDate(dataElementWithTaskVo.getProcessingDate());
            exportDTO.setDataType(dataElementWithTaskVo.getDataType());
            exportDTOList.add(exportDTO);
        }
        try {
            commonService.exportExcelData(exportDTOList, response, "采集方-查询整体情况服务实现类", QueryAllSituationForCollectorExportDTO.class);
        } catch (IOException e) {
            log.error("导出数据[采集方-查询整体情况服务实现类]失败", e);
            throw new RuntimeException("导出数据[采集方-查询整体情况服务实现类]失败");
        }

    }
}
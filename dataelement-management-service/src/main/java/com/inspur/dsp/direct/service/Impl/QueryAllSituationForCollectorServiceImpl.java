package com.inspur.dsp.direct.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.QueryAllSituationForCollectorExportDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.NePageSortFieldEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
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
            String displayStatus = getDisplayStatus(bdeStatus, ctStatus);
            dataElementWithTaskVo.setDisplaystatus(displayStatus);
        }
        return page.setRecords(baseDataElementList);
    }

    /**
     * 获取显示状态
     *
     * @param bdeStatus
     * @param ctStatus
     * @return
     */
    private static String getDisplayStatus(String bdeStatus, String ctStatus) {
        String displayStatus = "";
        // 判断 bdeStatus  negotiating 或  designated_source  displayStatus =  bdeStatus
        if (StatusEnums.NEGOTIATING.getCode().equals(bdeStatus)) {
            displayStatus = StatusEnums.NEGOTIATING.getDesc();
        } else if (StatusEnums.DESIGNATED_SOURCE.getCode().equals(bdeStatus)) {
            displayStatus = StatusEnums.DESIGNATED_SOURCE.getDesc();
        } else if (ConfirmationTaskEnums.PENDING_CLAIMED.getCode().equals(ctStatus)) {
            displayStatus = ConfirmationTaskEnums.PENDING_CLAIMED.getDesc();
        } else {
            // ctStatus = 待确认  displayStatus = 待确认
            if (ConfirmationTaskEnums.PENDING.getCode().equals(ctStatus)) {
                displayStatus = ConfirmationTaskEnums.PENDING.getDesc();
            }
            // ctStatus = 待认领  displayStatus = 待认领
            if (ConfirmationTaskEnums.CLAIMED.getCode().equals(ctStatus)) {
                displayStatus = ConfirmationTaskEnums.CLAIMED.getDesc();
            }
            // ctStatus = 已确认 and  bdeStatus = 待核定  displayStatus = 已确认
            if (ConfirmationTaskEnums.CONFIRMED.getCode().equals(ctStatus) && StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)) {
                displayStatus = ConfirmationTaskEnums.CONFIRMED.getDesc();
            }
            // ctStatus = 已拒绝 and  bdeStatus = 待协商  displayStatus = 已拒绝
            if (ConfirmationTaskEnums.REJECTED.getCode().equals(ctStatus) && StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus)) {
                displayStatus = ConfirmationTaskEnums.REJECTED.getDesc();
            }
            // ctStatus = 已认领 and  bdeStatus = 认领中, 待核定, 待协商  displayStatus = 已认领
            if (ConfirmationTaskEnums.CLAIMED.getCode().equals(ctStatus)
                    && (StatusEnums.CLAIMED.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus))
            ) {
                displayStatus = ConfirmationTaskEnums.CLAIMED.getDesc();
            }
            // ctStatus = 不认领 and  bdeStatus = 认领中, 待核定, 待协商  displayStatus = 不认领
            if (ConfirmationTaskEnums.NOT_CLAIMED.getCode().equals(ctStatus)
                    && (StatusEnums.CLAIMED.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus))) {
                displayStatus = ConfirmationTaskEnums.NOT_CLAIMED.getDesc();
            }
        }
        return displayStatus;
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
            exportDTO.setName(dataElementWithTaskVo.getName());
            exportDTO.setDefinition(dataElementWithTaskVo.getDefinition());
            exportDTO.setDataType(dataElementWithTaskVo.getDataType());
            String ctStatus = dataElementWithTaskVo.getCtStatus();
            String bdeStatus = dataElementWithTaskVo.getBdeStatus();
            String displayStatus = ctStatus;
            if ("negotiating".equals(bdeStatus) || "designated_source".equals(bdeStatus)) {
                displayStatus = bdeStatus;
            }
            String statusChinese = StatusUtil.getStatusChinese(displayStatus);
            exportDTO.setStatus(statusChinese);
            exportDTO.setReceiveDate(dataElementWithTaskVo.getSendDate());
            exportDTO.setProcessDate(dataElementWithTaskVo.getProcessingDate());
            exportDTO.setSendDate(dataElementWithTaskVo.getConfirmDate());

            exportDTOList.add(exportDTO);
        }
        try {
            commonService.exportExcelData(exportDTOList, response, "本单位定源情况", QueryAllSituationForCollectorExportDTO.class);
        } catch (IOException e) {
            log.error("导出数据[本单位定源情况]失败", e);
            throw new RuntimeException("导出数据[本单位定源情况]失败");
        }

    }
}
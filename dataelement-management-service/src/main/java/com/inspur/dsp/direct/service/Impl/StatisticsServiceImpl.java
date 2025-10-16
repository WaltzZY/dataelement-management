// StatisticsServiceImpl.java
package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.dao.StatisticsMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.vo.HomeStatusNumVO;
import com.inspur.dsp.direct.service.StatisticsService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public HomeStatusNumVO getHomeStatusNum() {
        HomeStatusNumVO homeStatusNumVO = new HomeStatusNumVO();

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String orgCode = userInfo.getOrgCode();

        try {
            // 组织方相关统计
            homeStatusNumVO.setPendingSourceQty(statisticsMapper.getCommonStatusNumForOrganiser("pending_source"));
            homeStatusNumVO.setPendingApprovalQty(statisticsMapper.getCommonStatusNumForOrganiser("pending_approval"));
            homeStatusNumVO.setPendingNegotiationQty(statisticsMapper.getCommonStatusNumForOrganiser("pending_negotiation"));
            homeStatusNumVO.setNegotiatingQty(statisticsMapper.getCommonStatusNumForOrganiser("negotiating"));
            homeStatusNumVO.setDesignatedSourceQty(statisticsMapper.getCommonStatusNumForOrganiser("designated_source"));
            homeStatusNumVO.setConfirmingQty(statisticsMapper.getCommonStatusNumForOrganiser("confirming"));
            homeStatusNumVO.setClaimedingQty(statisticsMapper.getCommonStatusNumForOrganiser("claimed_ing"));

            homeStatusNumVO.setPendingSourceConfirmQty(statisticsMapper.getPendingSourceConfirmQty("pending_source"));
            homeStatusNumVO.setPendingSourceClaimQty(statisticsMapper.getPendingSourceClaimQty("pending_source"));

            // 采集方相关统计
            homeStatusNumVO.setPendingQty(statisticsMapper.getCommonStatusNumForCollector(orgCode, "pending"));
            homeStatusNumVO.setPendingClaimedQty(statisticsMapper.getCommonStatusNumForCollector(orgCode, "pending_claimed"));
            homeStatusNumVO.setMyDesignatedSourceQty(statisticsMapper.getCommonStatusNumForCollector(orgCode, "designated_source"));

            homeStatusNumVO.setToBeProcessedQty(statisticsMapper.getToBeProcessedQty(orgCode));
            homeStatusNumVO.setProcessedQty(statisticsMapper.getProcessedQty(orgCode));

            homeStatusNumVO.setConfirmedQty(statisticsMapper.getCommonStatusNumForCollectorWithTask(orgCode, "confirmed"));
            homeStatusNumVO.setRejectedQty(statisticsMapper.getCommonStatusNumForCollectorWithTask(orgCode, "rejected"));
            homeStatusNumVO.setClaimedQty(statisticsMapper.getCommonStatusNumForCollectorWithTask(orgCode, "claimed"));
            homeStatusNumVO.setNotClaimedQty(statisticsMapper.getCommonStatusNumForCollectorWithTask(orgCode, "not_claimed"));

        } catch (Exception e) {
            throw new RuntimeException("数据查询失败", e);
        }

        return homeStatusNumVO;
    }
}
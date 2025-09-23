package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.service.DetermineResultForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 采集方查看已定源结果服务实现类
 */
@Service
public class DetermineResultForCollectorServiceImpl implements DetermineResultForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

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


}
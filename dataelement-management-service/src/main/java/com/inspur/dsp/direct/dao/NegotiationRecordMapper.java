package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import com.inspur.dsp.direct.dbentity.NegotiationRecordDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface NegotiationRecordMapper extends BaseMapper<NegotiationRecord> {
    default NegotiationRecord selectFirstByBaseDataelementDataid(String baseDataelementDataid) {
        LambdaQueryWrapper<NegotiationRecord> myQuery = Wrappers.lambdaQuery(NegotiationRecord.class);
        myQuery.eq(NegotiationRecord::getBaseDataelementDataid, baseDataelementDataid);
        return selectOne(myQuery);
    }

    /**
     * 重新selectById方法,将协商相关的详情也查询出来
     */
    NegotiationRecord selectById(String id);

    /**
     * 插入数据同时插入详情
     *
     * @param negotiationRecord
     * @return
     */
    default int insertRecordAndDetail(NegotiationRecord negotiationRecord) {
        // 插入协商主表数据
        int insert = insert(negotiationRecord);
        if (!CollectionUtils.isEmpty(negotiationRecord.getNegotiationRecordDetailList())){
            // 批量插入协商详情数据
            insert += insertSelectiveDetail(negotiationRecord.getNegotiationRecordDetailList());
        }
        return insert;
    }

    int insertSelectiveDetail(@Param("negotiationRecordDetailList") Collection<NegotiationRecordDetail> negotiationRecordDetailList);
}
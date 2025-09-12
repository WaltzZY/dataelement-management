package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NegotiationRecordMapper extends BaseMapper<NegotiationRecord> {
    default NegotiationRecord selectFirstByBaseDataelementDataid(String baseDataelementDataid) {
        LambdaQueryWrapper<NegotiationRecord> myQuery = Wrappers.lambdaQuery(NegotiationRecord.class);
        myQuery.eq(NegotiationRecord::getBaseDataelementDataid, baseDataelementDataid);
        return selectOne(myQuery);
    }
}
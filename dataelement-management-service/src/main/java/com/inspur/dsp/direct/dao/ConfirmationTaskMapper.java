package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfirmationTaskMapper extends BaseMapper<ConfirmationTask> {

    default ConfirmationTask selectFirstByBaseDataelementDataid(String baseDataelementDataid) {
        LambdaQueryWrapper<ConfirmationTask> myQuery = Wrappers.lambdaQuery(ConfirmationTask.class);
        myQuery.eq(ConfirmationTask::getBaseDataelementDataid, baseDataelementDataid);
        return selectOne(myQuery);
    }


}
package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementClassIficationMethod;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementClassIficationMethodDao extends BaseMapper<DataElementClassIficationMethod> {
    int updateBatch(List<DataElementClassIficationMethod> list);

    int updateBatchSelective(List<DataElementClassIficationMethod> list);

    int batchInsert(@Param("list") List<DataElementClassIficationMethod> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
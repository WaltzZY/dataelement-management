package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementBelongCategoryDao extends BaseMapper<DataElementBelongCategory> {
    int updateBatch(List<DataElementBelongCategory> list);

    int updateBatchSelective(List<DataElementBelongCategory> list);

    int batchInsert(@Param("list") List<DataElementBelongCategory> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
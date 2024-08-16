package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementCategory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementCategoryDao extends BaseMapper<DataElementCategory> {
    int updateBatch(List<DataElementCategory> list);

    int updateBatchSelective(List<DataElementCategory> list);

    int batchInsert(@Param("list") List<DataElementCategory> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
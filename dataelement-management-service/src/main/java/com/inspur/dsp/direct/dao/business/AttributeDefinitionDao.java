package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.AttributeDefinition;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttributeDefinitionDao extends BaseMapper<AttributeDefinition> {
    int updateBatch(List<AttributeDefinition> list);

    int updateBatchSelective(List<AttributeDefinition> list);

    int batchInsert(@Param("list") List<AttributeDefinition> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
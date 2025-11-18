package com.inspur.dsp.direct.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.FlowDefinition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowDefinitionMapper extends BaseMapper<FlowDefinition> {

    FlowDefinition selectFirstByFlowcode(@Param("flowcode")String flowcode);

}
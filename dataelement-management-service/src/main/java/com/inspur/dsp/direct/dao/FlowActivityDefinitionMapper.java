package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.FlowActivityDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowActivityDefinitionMapper extends BaseMapper<FlowActivityDefinition> {

    List<FlowActivityDefinition> selectAllByFlowidOrderByActivityorder(@Param("flowid")String flowid);

    List<FlowActivityDefinition> selectAll();
}
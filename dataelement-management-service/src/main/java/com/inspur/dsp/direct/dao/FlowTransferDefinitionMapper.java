package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.FlowTransferDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowTransferDefinitionMapper extends BaseMapper<FlowTransferDefinition> {

    List<FlowTransferDefinition> selectAllByFlowid(@Param("flowid")String flowid);

}
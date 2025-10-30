package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.DataElementAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataElementAttributeMapper extends BaseMapper<DataElementAttribute> {

    /**
     * 根据数据元ID查询属性列表(定标模块专用)
     * @param baseDataelementDataid 数据元ID
     * @return 属性列表
     */
    List<DataElementAttribute> selectByDataElementId(@Param("baseDataelementDataid") String baseDataelementDataid);

    /**
     * 根据数据元ID删除所有属性(定标模块专用)
     * @param baseDataelementDataid 数据元ID
     * @return 删除行数
     */
    int deleteByDataElementId(@Param("baseDataelementDataid") String baseDataelementDataid);
}
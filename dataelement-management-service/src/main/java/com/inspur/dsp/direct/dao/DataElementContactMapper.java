package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.DataElementContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementContactMapper extends BaseMapper<DataElementContact> {

    /**
     * 根据数据元ID查询联系人信息(定标模块专用)
     * @param baseDataelementDataid 数据元ID
     * @return 联系人信息
     */
    DataElementContact selectByDataElementId(@Param("baseDataelementDataid") String baseDataelementDataid);
}
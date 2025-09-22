package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.entity.bo.DomainSourceUnitInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DomainDataElementMapper extends BaseMapper<DomainDataElement> {

    List<DomainSourceUnitInfo> selectSourceUnitInfoByBaseDataid(@Param("baseDataIds") Collection<String> baseDataIds);

    DomainDataElement selectFirstByBaseDataelementDataid(@Param("baseDataelementDataid") String baseDataelementDataid);

    List<DomainDataElement> selectAllByBaseDataelementDataid(@Param("baseDataelementDataid")String baseDataelementDataid);

    List<DomainDataElement> selectListByDataId(@Param("dataId") String dataId);

}
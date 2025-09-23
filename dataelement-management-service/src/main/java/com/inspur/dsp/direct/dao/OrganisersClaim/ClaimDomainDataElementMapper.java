package com.inspur.dsp.direct.dao.OrganisersClaim;

import com.inspur.dsp.direct.dbentity.DomainDataElement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClaimDomainDataElementMapper {

    /**
     * 根据BaseDataElementDataId查询domain_data_element
     * @param dataid 数据元ID
     * @return DomainDataElement列表
     */
    List<DomainDataElement> selectDomainDataElementByBaseDataElementDataId(@Param("dataid") String dataid);
}
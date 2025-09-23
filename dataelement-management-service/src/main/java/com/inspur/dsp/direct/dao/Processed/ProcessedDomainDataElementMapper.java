package com.inspur.dsp.direct.dao.Processed;

import com.inspur.dsp.direct.dbentity.DomainDataElement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 域数据元Mapper
 */
@Mapper
public interface ProcessedDomainDataElementMapper {

    /**
     * 根据BaseDataElementDataId查询domain_data_element表
     * @param dataid 数据元ID
     * @return 符合条件的域数据元列表
     */
    List<DomainDataElement> selectDomainDataElementByBaseDataElementDataId(@Param("dataid") String dataid);
}

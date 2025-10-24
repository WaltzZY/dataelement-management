package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.DataElementCatalogRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataElementCatalogRelationMapper extends BaseMapper<DataElementCatalogRelation> {

    List<DataElementCatalogRelation> getAssociatedDataSourceCatalog(
            @Param("sourceUnitCode") String sourceUnitCode,
            @Param("baseDataId") String baseDataId);

    List<DataElementCatalogRelation> getAssociatedCollectorCatalog(
            @Param("sourceUnitCode") String sourceUnitCode,
            @Param("baseDataId") String baseDataId);

    Boolean cancelCatalogAssociation(@Param("dataid") String dataid);

    DataElementCatalogRelation findRelationExist(
            @Param("infoItemId") String infoItemId,
            @Param("dataElementId") String dataElementId);

    Boolean deleteCatalogAssociation(@Param("dataid") String dataid);

    int insert(DataElementCatalogRelation relation);

}
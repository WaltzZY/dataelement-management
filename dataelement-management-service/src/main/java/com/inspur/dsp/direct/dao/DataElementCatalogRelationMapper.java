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

    /**
     * 查询数据元的关联目录列表(定标模块专用)
     *
     * @param dataElementId 数据元ID
     * @param sourceOrgCode 数源单位编码(可选)
     * @return 关联目录列表
     */
    List<DataElementCatalogRelation> selectAssociatedCatalogs(
            @Param("dataElementId") String dataElementId,
            @Param("sourceOrgCode") String sourceOrgCode);

    /**
     * 检查目录关联关系是否存在(定标模块专用)
     *
     * @param dataElementId 数据元ID
     * @param catalogitemid 目录数据项ID
     * @return 关联关系记录
     */
    DataElementCatalogRelation selectRelationByDataElementAndItem(
            @Param("dataElementId") String dataElementId,
            @Param("catalogitemid") String catalogitemid);
}
package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.entity.vo.CatalogAssociatedDTO;
import com.inspur.dsp.direct.entity.vo.DataElementCatalogRelationVO;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;

import java.util.List;

public interface AssociatedCatalogService {
    
    List<DataElementCatalogRelationVO> getAssociatedDataSourceCatalog(String sourceUnitCode, String baseDataId);
    
    List<DataElementCatalogRelationVO> getAssociatedCollectorCatalog(String sourceUnitCode, String baseDataId);
    
    String cancelCatalogAssociation(String dataid);
    
    List<AssociatedDataItemListVO> getAssociatedDataItemList(String keyword, List<String> orgCodeList);
    
    List<AssociatedDataItemListVO> getAssociatedDataItemForCollectorList(String keyword, List<String> orgCodeList);
    
    List<AssociatedDataItemListVO> getAssociatedDataItemWithAllList(String keyword, String dataid);
    
    String saveCatalogAssociation(List<CatalogAssociatedDTO> catalogList);
}
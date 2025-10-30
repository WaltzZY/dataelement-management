package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

/**
 * 目录关联关系DTO
 */
@Data
public class CatalogRelationDto {

    private String catalogId;
    
    private String catalogitemid;
    
    private String catalogName;
    
    private String catalogDescription;
    
    private String dataItemName;
    
    private String dataType;
    
    private String sourceOrgCode;
    
    private String sourceOrgName;
}
package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 关联目录VO
 */
@Data
public class AssociatedCatalogVo {

    private String relationid;
    
    private String catalogId;
    
    private String catalogName;
    
    private String catalogDescription;
    
    private String catalogitemid;
    
    private String dataItemName;
    
    private String dataType;
    
    private String sourceOrgCode;
    
    private String sourceOrgName;
}
package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 目录数据项VO
 */
@Data
public class CatalogItemVo {

    private String catalogitemid;
    
    private String dataItemName;
    
    private String dataType;
    
    private String catalogId;
    
    private String catalogName;
    
    private String catalogDescription;
    
    private String sourceOrgCode;
    
    private String sourceOrgName;
    
    private String dataItemCode;
}
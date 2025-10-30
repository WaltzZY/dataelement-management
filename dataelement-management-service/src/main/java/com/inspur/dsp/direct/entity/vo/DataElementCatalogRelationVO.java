package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataElementCatalogRelationVO implements Serializable {
    private String dataid;
    private String dataElementId;
    private String catalogId;
    private String catalogName;
    private String infoItemId;
    private String infoItemName;
    private String catalogUnitCode;
    private String catalogUnitName;
    private String detailUrl;
    private Integer flag;

}
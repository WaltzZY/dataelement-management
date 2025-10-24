package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedDataItemListVO implements Serializable {
    private String dataid;
    private String dataElementId;
    private String catalogId;
    private String catalogName;
    private String catalogDesc;
    private String infoItemId;
    private String infoItemName;
    private String infoItemDatatype;
    private String catalogUnitCode;
    private String catalogUnitName;
    private Integer flag;
}
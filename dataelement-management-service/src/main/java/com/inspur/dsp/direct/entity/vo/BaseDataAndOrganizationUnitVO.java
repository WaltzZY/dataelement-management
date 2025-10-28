package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDataAndOrganizationUnitVO implements Serializable {
    
    private String dataid;
    private String sourceUnitCode;
    private String sourceUnitName;
    private String unitCode;
    private String unitName;
    private String contactName;
    private String contactPhone;
}
package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

/**
 * 流程记录DTO
 */
@Data
public class ProcessRecordDto {

    private String baseDataelementDataid;
    
    private String operation;
    
    private String sourceStatus;
    
    private String destStatus;
    
    private String operatorAccount;
    
    private String operatorName;
    
    private String operatorUnitCode;
    
    private String operatorUnitName;

    private String Usersuggestion;
}
package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SourceEventRecordVo {
    private String recordId;
    private String baseDataelementDataid;
    private String eventType;
    private String sendUnitCode;
    private String sendUnitName;
    private String eventSourceUnitCode;
    private String eventSourceUnitName;
    private String eventTargetUnitCode;
    private String eventTargetUnitName;
    private String eventDescription;
    private String eventStatus;
    private Date eventTime;
    private String operatorAccount;
    private String operatorName;
    private Date createDate;
}
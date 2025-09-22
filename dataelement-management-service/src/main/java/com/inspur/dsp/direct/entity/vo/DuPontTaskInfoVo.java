package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DuPontTaskInfoVo {
    private String taskId;
    private String processingUnitName;
    private String relatedDeName;
    private String taskStatus;
    private String taskStatusDesc;
    private String tasktype;
    private Date processingDate;
    private String processingOpinion;
}
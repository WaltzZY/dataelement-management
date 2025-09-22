package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FlowNodeDTO {
    private Integer seqNo;
    private String nodeName;
    private String nodeHandleUserName;
    private Date passDate;
    private String nodeResult;
    private String nodeFeedback;
    private Integer nodeShowStatus;
}
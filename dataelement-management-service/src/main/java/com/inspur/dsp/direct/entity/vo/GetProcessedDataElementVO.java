package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取已处理数据元VO
 */
@Data
public class GetProcessedDataElementVO {
    // 基础数据元字段
    private String dataid;
    private String dataElementId;
    private String status;
    private String name;
    private String definition;
    private String datatype;
    private String dataFormat;
    private String valueDomain;
    private String sourceUnitCode;
    private String sourceUnitName;
    private LocalDateTime publishDate;
    private LocalDateTime sendDate;
    private LocalDateTime confirmDate;
    private Integer collectunitqty;
    private String remarks;
    private LocalDateTime createDate;
    private String createAccount;
    private LocalDateTime lastModifyDate;
    private String lastModifyAccount;

    // 任务相关字段
    private String taskId;
    private String baseDataelementDataid;
    private String tasktype;
    private String sendUnitCode;
    private String sendUnitName;
    private String senderAccount;
    private String senderName;
    private String processingUnitCode;
    private String processingUnitName;
    private LocalDateTime processingDate;
    private String processingResult;
    private String processingOpinio;
    private String processorAccount;
    private String processorName;
    private LocalDateTime approvalDate;
    private String approvalAccount;
    private String approvalName;

    private String statusDesc;

}
package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmDate;
    private Integer collectunitqty;
    private String remarks;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    private String createAccount;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processingDate;
    private String processingResult;
    private String processingOpinio;
    private String processorAccount;
    private String processorName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalDate;
    private String approvalAccount;
    private String approvalName;

    private String statusDesc;

}
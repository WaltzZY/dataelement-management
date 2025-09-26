package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimDataElementVO {
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

//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendDate;

//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmDate;
    private Integer collectunitqty;
    private String remarks;

//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    private String createAccount;

//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifyDate;
    private String lastModifyAccount;
    private String collectunitnames;
    private String statusDesc;
}

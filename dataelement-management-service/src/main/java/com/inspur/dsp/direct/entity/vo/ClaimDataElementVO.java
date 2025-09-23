package com.inspur.dsp.direct.entity.vo;

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
    private LocalDateTime publishDate;
    private LocalDateTime sendDate;
    private LocalDateTime confirmDate;
    private Integer collectunitqty;
    private String remarks;
    private LocalDateTime createDate;
    private String createAccount;
    private LocalDateTime lastModifyDate;
    private String lastModifyAccount;
    private String collectunitnames;
}

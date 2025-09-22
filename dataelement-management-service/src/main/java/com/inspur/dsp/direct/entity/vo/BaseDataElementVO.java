package com.inspur.dsp.direct.entity.vo;

import com.inspur.dsp.direct.entity.dto.DomainDataElementDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BaseDataElementVO {
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
    private Date publishDate;
    private Date sendDate;
    private Date confirmDate;
    private String remarks;
    private Date createDate;
    private String createAccount;
    private Date lastModifyDate;
    private String lastModifyAccount;
    private SourceEventRecordVo sourceEventRecordVO;
    private List<DomainDataElementDTO> dataElementDTOList;
}
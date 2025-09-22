package com.inspur.dsp.direct.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基准数据元表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDataElementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Integer collectunitqty;

    private String remarks;

    private Date createDate;

    private String createAccount;

    private Date lastModifyDate;

    private String lastModifyAccount;

    private SourceEventRecordDTO sourceEventRecordDTO;

    private List<DomainDataElementDTO> dataElementDTOList;

    private List<ConfirmationTaskDTO> confirmationTaskDTOList;

}
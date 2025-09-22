package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class BaseDataElementSearchDTO {
    private Integer pageSize = 10;
    private Integer pageNum = 1;
    private String dataId;
    private String keyword = "name";
    private String value;
    private String status;
    private String orgCode;
    private String sendDate;
    private String sendDateBegin;
    private String sendDateEnd;
    private String receiveDate;
    private String receiveDateBegin;
    private String receiveDateEnd;
    private String processDate;
    private String processDateBegin;
    private String processDateEnd;
    private List<String> statusList;
    private List<String> taskStatusList;
    private List<String> baseStatusList;
}
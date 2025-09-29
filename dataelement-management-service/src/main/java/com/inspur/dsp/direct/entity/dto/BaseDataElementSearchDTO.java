package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String sourceUnitName;
    private List<String> sourceUnitCodeList;
    private String orgCode;
    private String sendDate;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sendDateBegin;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sendDateEnd;
    private String receiveDate;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String receiveDateBegin;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String receiveDateEnd;
    private String processDate;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String processDateBegin;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String processDateEnd;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String confirmDateBegin;
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String confirmDateEnd;
    private List<String> statusList;
    private List<String> taskStatusList;
    private List<String> baseStatusList;
    /** 排序字段 */
    private String sortField;

    /** 排序方式 */
    private String sortOrder;
    /** 排序sql */
    private String sortSql;

}
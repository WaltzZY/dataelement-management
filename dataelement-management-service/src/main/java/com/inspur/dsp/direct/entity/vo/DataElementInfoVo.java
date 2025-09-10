package com.inspur.dsp.direct.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataElementInfoVo {

    /**
     * 采集单位code
     */
    private String collectUnitCode;
    /**
     * 采集单位名称
     */
    private String collectUnitName;
    /**
     * 数据元编码
     */
    private String dataElementId;
    /**
     * 数据格式
     */
    private String dataFormat;
    /**
     * 数据行唯一标识
     */
    private String dataid;
    /**
     * 数据类型
     */
    private String datatype;
    /**
     * 数据元定义描述
     */
    private String definition;
    /**
     * 数据元名称
     */
    private String name;
    /**
     * 数据元发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 定源流程集合
     */
    private List<SourceRequestList> sourceRequestList;
    /**
     * 数源单位统一社会信用代码
     */
    private String sourceUnitCode;
    /**
     * 数源单位名称
     */
    private String sourceUnitName;
    /**
     * 数据元状态
     */
    private String status;
    /**
     * 数据元状态描述
     */
    private String statusDesc;
    /**
     * 值域
     */
    private String valueDomain;
}

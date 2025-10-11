package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class DataElementPageInfoVo {
    /**
     * 采集单位名称
     */
    private String collectDeptName;
    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;
    /**
     * 数据行唯一标识，ID 编号
     */
    private String dataid;
    /**
     * 数据元定义描述
     */
    private String definition;
    /**
     * 数据元名称
     */
    private String name;
    /**
     * 定源时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDate;
    /**
     * 数源单位code
     */
    private String sourceUnitCode;
    /**
     * 数源单位名称
     */
    private String sourceUnitName;
    /**
     * 状态code
     */
    private String status;
    /**
     * 状态说明
     */
    private String statusDesc;
    /** 采集单位数量 */
    private Integer collectunitqty;

    /** 任务处理单位名称 */
    private String paUnitName;

    /**
     * 定数时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date generatedatetime;
}

package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 协商数据元VO - 用于展示待协商的基准数据元信息
 */
@Data
public class NegotiationDataElementVO {
    /** 数据行唯一标识 */
    private String dataid;

    /** 数据元状态 */
    private String status;

    /** 数据元名称 */
    private String name;

    /** 数据元定义描述 */
    private String definition;

    /** 数据元状态描述 */
    private String statusDesc;

    /** 数源单位统一社会信用代码 */
    private String sourceUnitCode;

    /** 数源单位名称 */
    private String sourceUnitName;

    /** 发起定源时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /** 确定数源单位时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDate;

    /** 采集单位确认或认领的情况 */
    private List<CollectUnitsAndStatusVO> collectUnitsAndStatus;
}
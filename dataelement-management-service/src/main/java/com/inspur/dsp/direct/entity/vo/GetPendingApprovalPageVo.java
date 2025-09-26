package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分页查询待核定数据VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPendingApprovalPageVo {

    /**
     * 数据元id
     */
    private String dataid;

    /**
     * 数据元名称
     */
    private String name;

    /**
     * 数据元定义描述
     */
    private String definition;

    /**
     * 数据元状态
     */
    private String status;

    /**
     * 数据元状态描述
     */
    private String statusDesc;

    /**
     * 待核定数源单位code
     */
    private String paUnitCode;

    /**
     * 待核定数源单位名称
     */
    private String paUnitName;

    /**
     * 数源单位名称
     */
    private String sourceUnitName;

    /**
     * 数源单位code
     */
    private String sourceUnitCode;

    /**
     * 定源时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDate;

    /**
     * 采集单位数量
     */
    private int collectunitqty;

    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;
}
package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectDataInfoVo {

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
    private Date publishDate;
    /**
     * 拒绝原因
     */
    private String refuseReason;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 发出时间
     */
    private Date sendDate;
    /**
     * 发出人姓名
     */
    private String senderName;
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
     * 数据元状态
     */
    private String statusDesc;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 值域
     */
    private String valueDomain;
}

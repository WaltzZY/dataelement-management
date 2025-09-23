package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class DataElementWithTaskVo {

    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;
    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date receiveDate;
    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date processingDate;
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
     * 数据类型
     */
    private String dataType;

    /**
     * 数据类型
     */
    private String taskId;

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
     * statusChinese
     */
    private String statusChinese;
}

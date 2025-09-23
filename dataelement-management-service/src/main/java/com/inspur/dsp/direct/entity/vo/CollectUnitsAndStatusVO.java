package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 采集单位确认状态VO - 用于展示采集单位确认或认领的情况
 */
@Data
public class CollectUnitsAndStatusVO {
    /** 状态 */
    private Boolean status;

    /** 单位统一社会信用代码 */
    private String unitCode;

    /** 单位名称 */
    private String unitName;

    /**
     * 相关数据元名称
     */
    private String dataElementName;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date processingDate;
    /**
     * 拒绝原因
     */
    private String processingOpinion;
    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 任务状态描述
     */
    private String taskStatusDesc;

}

package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 审核标准模块数据元查询结果VO
 * 
 * @author system
 * @since 2025
 */
@Data
public class AuditDataElementVo {
    
    /**
     * 数据元唯一标识
     */
    private String dataid;
    
    /**
     * 数据元名称
     */
    private String name;
    
    /**
     * 数据元编码
     */
    private String dataelementCode;
    
    /**
     * 定义
     */
    private String definition;
    
    /**
     * 数据类型
     */
    private String datatype;
    
    /**
     * 数源单位统一社会信用代码
     */
    private String sourceunitCode;
    
    /**
     * 数源单位名称
     */
    private String sourceunitName;
    
    /**
     * 数源单位名称(审核模块使用)
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
     * 提交时间
     */
    private Date submitTime;
    
    /**
     * 最后提交日期
     */
    private Date lastSubmitDate;
    
    /**
     * 最后审批日期
     */
    private Date lastApproveDate;
    
    /**
     * 最后提交复审日期
     */
    private Date lastSubmitReexaminationDate;
    
    /**
     * 最后提交发布日期
     */
    private Date lastSubmitReleasedDate;
    
    /**
     * 最后发起修订日期
     */
    private Date lastInitiateRevisedDate;
    
    /**
     * 发布日期
     */
    private Date publishDate;
}
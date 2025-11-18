package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    
    /**
     * 最后提交日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastSubmitDate;
    
    /**
     * 最后审批日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastApproveDate;
    
    /**
     * 最后提交复审日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastSubmitReexaminationDate;
    
    /**
     * 最后提交发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastSubmitReleasedDate;
    
    /**
     * 最后发起修订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastInitiateRevisedDate;
    
    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;
    
    /**
     * 审核驳回时间（从流程记录中获取）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rejectTime;
    
    /**
     * 报送时间（对应 lastSubmitReleasedDate）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportTime;
}
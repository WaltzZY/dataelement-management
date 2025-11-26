package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 定标阶段数据元列表查询结果VO
 * 
 * @author system
 * @since 2025
 */
@Data
public class StandardDataElementPageInfoVo {


    /**
     * 数据元唯一标识
     */
    private String dataid;
    
    /**
     * 数据元编码
     */
    private String dataelementCode;
    
    /**
     * 数据元名称
     */
    private String name;
    
    /**
     * 数据元定义描述
     */
    private String definition;
    
    /**
     * 数据类型
     */
    private String datatype;
    
    /**
     * 数据元状态
     */
    private String status;
    
    /**
     * 数据元状态描述
     */
    private String statusDesc;
    
    /**
     * 定源时间
     */
    private Date confirmDate;
    
    /**
     * 发布时间
     */
    private Date publishDate;
    
    /**
     * 发起修订时间，表revision_comment中，revision_createdate
     */
    private Date revisionCreateDate;
    
    /**
     * 数源单位名称
     */
    private String sourceunitName;
    
    /**
     * 提交时间
     */
    private Date submitTime;
    
    /**
     * 审核时间
     */
    private Date auditTime;
    
    /**
     * 发起修订时间
     */
    private Date initiateRevisionTime;
    
    /**
     * 报送时间
     */
    private Date reportTime;
    
    /**
     * 发布时间（与publishDate相同，为了统一命名）
     */
    private Date publishTime;
    
    /**
     * 修订提交时间
     */
    private Date revisionSubmitTime;
}
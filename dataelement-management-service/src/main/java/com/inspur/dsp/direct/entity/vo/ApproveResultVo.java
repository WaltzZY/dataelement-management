package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 审核结果VO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ApproveResultVo {
    
    /**
     * 审核成功数量
     */
    private Integer successCount;
    
    /**
     * 审核失败数量
     */
    private Integer errorCount;
    
    /**
     * 审核总数量
     */
    private Integer totalCount;
    
    /**
     * 审核操作类型（审核通过/驳回）
     */
    private String operationType;
    
    /**
     * 审核结果描述
     */
    private String resultMessage;
    
    /**
     * 失败详情
     */
    private String errorDetails;
    
    /**
     * 审核是否完全成功
     */
    private Boolean isSuccess;
}
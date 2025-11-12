package com.inspur.dsp.direct.entity.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 审核环节OB
 * 
 * @author system
 * @since 2025
 */
@Data
public class ApproveInfoOB {
    
    /**
     * 数据元唯一标识
     */
    @NotBlank(message = "数据元ID不能为空")
    private String dataid;
    
    /**
     * 用户操作类型
     * 可选值：审核通过、驳回
     */
    @NotBlank(message = "用户操作类型不能为空")
    private String useroperation;
    
    /**
     * 用户意见建议
     */
    private String usersuggestion;
}
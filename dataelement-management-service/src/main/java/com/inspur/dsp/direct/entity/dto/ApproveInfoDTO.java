package com.inspur.dsp.direct.entity.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 审核环节DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ApproveInfoDTO {
    
    /**
     * 数据元ID列表
     */
    @NotEmpty(message = "数据元ID列表不能为空")
    private List<String> list;
    
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
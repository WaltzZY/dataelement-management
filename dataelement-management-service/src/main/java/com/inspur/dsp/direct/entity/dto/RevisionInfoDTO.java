package com.inspur.dsp.direct.entity.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 发起修订操作DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class RevisionInfoDTO {
    
    /**
     * 数据元ID列表,支持批量修订
     */
    @NotEmpty(message = "数据元ID列表不能为空")
    private List<String> list;
    
    /**
     * 用户操作类型
     * 可选值: "发起修订"、"报送"
     */
    @NotBlank(message = "用户操作类型不能为空")
    private String useroperation;
    
    /**
     * 用户意见建议,修订理由
     */
    private String usersuggestion;
}
package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 复审操作DTO
 */
@Data
public class ReExaminationDataElementDTO {
    
    @NotEmpty(message = "数据元ID列表不能为空")
    private List<String> dataid;
    
    @NotBlank(message = "操作类型不能为空")
    private String useroperation;
    
    private String usersuggestion;
}
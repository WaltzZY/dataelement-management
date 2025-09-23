package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量发起协商
 */
@Data
public class BatchNegotiationDto {
    /**
     * 基准数据元数据元ID
     */
    @NotEmpty(message = "数据元ID不能为空")
    private List<String> dataids;
    /**
     * 协商内容
     */
    @NotBlank(message = "协商内容不能为空")
    private String negotiationContent;
}

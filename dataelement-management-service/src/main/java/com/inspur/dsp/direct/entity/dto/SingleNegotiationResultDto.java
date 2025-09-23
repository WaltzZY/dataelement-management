package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SingleNegotiationResultDto {

    // 基准数据元id
    @NotBlank(message = "数据元ID不能为空")
    private String dataid;
    // 统一社会信用代码
    @NotBlank(message = "统一社会信用代码不能为空")
    private String orgCode;
}

package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 属性信息DTO
 */
@Data
public class AttributeDto {

    @NotBlank(message = "属性名称不能为空")
    private String attributeName;

    @NotBlank(message = "属性值不能为空")
    private String attributeValue;
}
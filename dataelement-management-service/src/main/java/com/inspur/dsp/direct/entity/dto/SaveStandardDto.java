package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 保存编制标准请求DTO
 */
@Data
public class SaveStandardDto {

    @NotBlank(message = "数据元ID不能为空")
    private String dataid;

    @NotBlank(message = "数据元编码不能为空") 
    private String dataelementcode;

    private String definition;

    @NotBlank(message = "数据类型不能为空")
    private String datatype;

    private String dataFormat;

    private String valueDomain;

    private String remarks;

    @Valid
    private ContactInfoDto contactInfo;

    @Valid
    private List<AttributeDto> attributes;
}
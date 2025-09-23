package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SingleNegotiationDto {

    @NotBlank(message = "数据元ID不能为空")
    private String dataid;

    @NotEmpty(message = "协商单位不能为空")
    private List<String> unitcodes;

    @NotBlank(message = "协商内容不能为空")
    private String negotiationContent;
}

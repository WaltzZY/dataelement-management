package com.inspur.dsp.direct.entity.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 手动定源
 */
@Data
public class ManualSourceDto {

    /**
     * 数据元id
     */
    @NotBlank(message = "数据元id不能为空")
    private String dataid;
    /**
     * 数源单位统一社会信用代码
     */
    @NotBlank(message = "数源单位统一社会信用代码不能为空")
    private String sourceUnitCode;
    /**
     * 数源单位名称
     */
    @NotBlank(message = "数源单位名称不能为空")
    private String sourceUnitName;
}

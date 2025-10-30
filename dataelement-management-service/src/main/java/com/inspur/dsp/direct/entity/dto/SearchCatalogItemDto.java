package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 查询目录数据项请求DTO
 */
@Data
public class SearchCatalogItemDto {
    private String keyword;

    @NotBlank(message = "数源单位编码不能为空")
    private String sourceOrgCode;

    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;
}
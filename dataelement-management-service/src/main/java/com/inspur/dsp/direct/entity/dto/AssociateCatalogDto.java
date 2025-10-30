package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 关联目录请求DTO
 */
@Data
public class AssociateCatalogDto {

    @NotBlank(message = "数据元ID不能为空")
    private String baseDataelementDataid;

    @NotEmpty(message = "关联关系列表不能为空")
    @Valid
    private List<CatalogRelationDto> relations;
}
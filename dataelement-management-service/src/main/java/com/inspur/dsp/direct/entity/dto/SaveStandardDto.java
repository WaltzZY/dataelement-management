package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 保存编制标准请求DTO
 */
@Data
public class SaveStandardDto {

    private String dataid;

    private String dataelementcode;

    private String definition;

    private String datatype;

    private String dataFormat;

    private String valueDomain;

    private String remarks;

    private ContactInfoDto contactInfo;

    private List<AttributeDto> attributes;

    /**
     * 关联目录列表（仅用于save接口保存选中的关联目录）
     */
    private List<CatalogRelationDto> associatedCatalogs;
}
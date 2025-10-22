package com.inspur.dsp.direct.httpentity.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryCatalogByColumnNameDto {

    /**
     * 目录信息项名称
     */
    private String name;
    /**
     * 单位code集合
     */
    private List<String> orgCodes;
}

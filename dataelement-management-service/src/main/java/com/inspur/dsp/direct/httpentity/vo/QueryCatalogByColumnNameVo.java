package com.inspur.dsp.direct.httpentity.vo;

import lombok.Data;

@Data
public class QueryCatalogByColumnNameVo {

    /**
     * 目录id
     */
    private String catalogId;
    /**
     * 目录name
     */
    private String catalogName;
    /**
     * 摘要
     */
    private String catalogDesc;
    /**
     * 信息项id
     */
    private String infoItemId;
    /**
     * 信息项名称
     */
    private String infoItemName;
    /**
     * 信息项类型
     */
    private String infoItemDatatype;
    /**
     * 目录单位统一社会信用代码
     */
    private String catalogUnitCode;
    /**
     * 目录单位名称
     */
    private String catalogUnitName;
    /**
     * 目录详情url
     */
    private String catalogDetailUrl;
}

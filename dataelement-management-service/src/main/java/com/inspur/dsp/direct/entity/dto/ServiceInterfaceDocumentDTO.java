package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

/**
 * 请求参数
 */
@Data
public class ServiceInterfaceDocumentDTO {

    /**
     * 关键词
     */
    private String keyWords;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 条数
     */
    private Integer pageSize;
}

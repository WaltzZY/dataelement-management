package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 响应参数
 */
@Data
public class DataElementStandardVO {

    /**
     * 标准文件id
     */
    private String standardId;
    /**
     * 标准编号
     */
    private String standardNo;
    /**
     * 标准名称
     */
    private String standardName;
    /**
     * 标准文件描述
     */
    private String standardDesc;
}

package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 响应参数
 */
@Data
public class ServiceInterfaceDocumentDetailVO {

    /**
     * 文档id
     */
    private String id;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 接口标题、中文名称
     */
    private String interfaceTitle;
    /**
     * 接口地址
     */
    private String interfaceAddress;
    /**
     * 接口说明文本
     */
    private String interfaceExplanation;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 修改时间
     */
    private String modifyDate;
}

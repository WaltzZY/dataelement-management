package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

@Data
public class GetCollectDataVo {

    /**
     * 数据元编码
     */
    private String dataElementId;
    /**
     * 数据行唯一标识
     */
    private String dataid;
    /**
     * 数据类型
     */
    private String datatype;
    /**
     * 数据元定义描述
     */
    private String definition;
    /**
     * 处理时间
     */
    private String processingDate;
    /**
     * 数据元名称
     */
    private String name;
    /**
     * 接收时间
     */
    private String receiveTime;
    /**
     * 数据元状态
     */
    private String status;
}

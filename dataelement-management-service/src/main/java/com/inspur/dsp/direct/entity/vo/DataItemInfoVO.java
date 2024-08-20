package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class DataItemInfoVO {

    /**
     * 目录id
     */
    private String cateId;
    /**
     * 目录名称
     */
    private String cateName;
    /**
     * 数据资源id
     */
    private String dataResourceId;
    /**
     * 数据资源name
     */
    private String dataResourceName;
    /**
     * 数据项列表
     */
    private List<String> item;
    /**
     * 提供方code
     */
    private String orgCode;
    /**
     * 提供方name
     */
    private String orgName;

}

package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 定标阶段数据元列表查询结果VO
 *
 * @author system
 * @since 2025
 */
@Data
public class StandardDataElementPageInfoVo {

    /**
     * 数据元唯一标识
     */
    private String dataid;

    /**
     * 数据元编码
     */
    private String dataelementCode;

    /**
     * 数据元名称
     */
    private String name;

    /**
     * 数据元定义描述
     */
    private String definition;

    /**
     * 数据类型
     */
    private String datatype;

    /**
     * 数据元状态
     */
    private String status;

    /**
     * 数据元状态描述
     */
    private String statusDesc;

    /**
     * 定源时间
     */
    private Date confirmDate;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 发起修订时间，表revision_comment中，revision_createdate
     */
    private Date revisionCreateDate;

    /**
     * 数据源单位编码
     */
    private String sourceunitCode;
}
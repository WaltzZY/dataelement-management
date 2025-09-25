package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据元列表查询条件DTO
 * 按照修改后的要求，包含排序字段
 *
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
public class DataElementPageQueryDto {

    /**
     * 页码
     */
    private Long pageNum;

    /**
     * 每页条数
     */
    private Long pageSize;

    /**
     * base_dataelement表status状态，主状态
     */
    private List<String> statusList;

    /**
     * 输入的查询条件
     */
    private String keyword;

    /**
     * 发起时间查询起始时间
     */
    private Date sendDateBegin;

    /**
     * 发起时间查询截止时间
     */
    private Date sendDateEnd;

    /**
     * 数源单位统一社会信用代码列表
     */
    private List<String> sourceUnitCode;

    /**
     * 定源时间查询起始时间
     */
    private Date confirmDateBegin;

    /**
     * 定源时间查询截止时间
     */
    private Date confirmDateEnd;

    /**
     * 排序字段
     * 支持的值：
     * - collectunitqty: 采集单位数量
     * - status: 状态
     * - sourceUnitName: 数源单位
     * - sendDate: 发起时间
     * - confirmDate: 定源时间
     */
    private String sortField;

    /**
     * 排序方向
     * asc: 升序, desc: 降序
     */
    private String sortOrder;
}
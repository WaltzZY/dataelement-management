package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 协商参数DTO - 用于接收查询协商列表的查询参数
 */
@Data
public class NegotiationParmDTO {
    /** 状态列表 */
    private List<String> statusList;

    /** 发起时间查询起始时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateBegin;

    /** 发起时间查询截止时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateEnd;

    /** 输入的查询条件关键字 */
    private String keyword;

    /** 数源单位统一社会信用代码列表 */
    private List<String> sourceUnitCodeList;

    /** 定源时间查询起始时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateBegin;

    /** 定源时间查询截止时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateEnd;

    /** 页码，默认值1 */
    private Long pageNum = 1L;

    /** 每页条数，默认值10 */
    private Long pageSize = 10L;

    /** 排序字段 */
    private String sortField;

    /** 排序方式 */
    private String sortOrder;

    /**
     * 导出标识,仅用作导出区别导出模版
     */
    private String exportFlag;
}
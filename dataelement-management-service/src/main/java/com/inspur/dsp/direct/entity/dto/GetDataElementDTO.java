package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetDataElementDTO {
    // Getters and Setters
    private List<String> status;                // 状态为待定源或认领中

    private LocalDateTime sendDateBegin;  // 状态为认领中时，查询时间范围的起始时间
    private LocalDateTime sendDateEnd;    // 状态为认领中时，查询时间范围的结束时间
//    private LocalDateTime senddateBegin;  // 状态为认领中时，查询时间范围的起始时间
//    private LocalDateTime senddateEnd;    // 状态为认领中时，查询时间范围的结束时间

    private String keyword;               // 查询关键字（模糊匹配）

    private long pageNum;
    private long pageSize;

    private String sortField;   // 排序字段
    private String sortOrder;   // 排序顺序

    // 构造方法
    public GetDataElementDTO() {}
}
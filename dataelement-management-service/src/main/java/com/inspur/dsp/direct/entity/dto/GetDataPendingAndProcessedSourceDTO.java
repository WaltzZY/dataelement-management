package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取待处理和已处理数据源DTO
 */

@Data
public class GetDataPendingAndProcessedSourceDTO {

//    /** 用于区分查询采集方已处理列表还是待处理列表 */
//    private String processstatus;

    /** 查询已经处理的状态为已认领或不认领记录 */
    private List<String> status;

    /** 接收时间的起始时间 */
    private LocalDateTime sendDateBegin;
//    private LocalDateTime recievestartTime;

    /** 接收时间的结束时间 */
    private LocalDateTime sendDateEnd;
//    private LocalDateTime recieveendTime;

    /** 处理时间的起始时间 */
    private LocalDateTime processDateBegin;
//    private LocalDateTime processstartTime;

    /** 处理时间的结束时间 */
    private LocalDateTime processDateEnd;
//    private LocalDateTime proccessendTime;

    /** 查询关键字（模糊匹配） */
    private String keyword;

    private long pageNum;
    private long pageSize;

    private String sortField;   // 排序字段
    private String sortOrder;   // 排序顺序

    // 构造函数
    public GetDataPendingAndProcessedSourceDTO() {}
}

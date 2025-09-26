package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取已处理数据元DTO
 */
@Data
public class GetProcessedDataElementDTO {
    private List<String> status; // 查看具体状态的已处理数据元（已确认、已拒绝、已认领、不认领）

    /** 接收时间的起始时间 */
    private LocalDateTime sendDateBegin;

    /** 接收时间的结束时间 */
    private LocalDateTime sendDateEnd;

    /** 处理时间的起始时间 */
    private LocalDateTime processDateBegin;

    /** 处理时间的结束时间 */
    private LocalDateTime processDateEnd;

//    private LocalDateTime recievestartTime; // 接收时间的起始时间
//    private LocalDateTime recieveendTime; // 接收时间的结束时间
//    private LocalDateTime processstartTime; // 处理时间的起始时间
//    private LocalDateTime proccessendTime; // 处理时间的结束时间


    private String keyword; // 查询关键字（模糊匹配）

    private long pageNum;
    private long pageSize;

    private String sortField;   // 排序字段
    private String sortOrder;   // 排序顺序

    // 构造函数
    public GetProcessedDataElementDTO() {}
}

package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollectDataElementPageDto {
    /**
     * 审核状态，用于切换“待处理”，“已处理”
     */
    private String auditStatus;
    /**
     * 处理时间结束
     */
    private String processDateEnd;
    /**
     * 处理时间开始
     */
    private String processDateBegin;
    /**
     * 页码
     */
    private long pageNum;
    /**
     * 每页条数
     */
    private long pageSize;
    /**
     * 接收时间结束
     */
    private String receiveTimeEnd;
    /**
     * 接收时间开始
     */
    private String receiveTimeStart;
    /**
     * 基准数据元名称、定义
     */
    private String keyword;
    /**
     * 状态集合，状态
     */
    private List<String> statusList;
    /**
     * 排序字段：数据类型：datatype，状态：status，接收时间：receiveTime
     */
    private String sortField;
    /**
     * 排序方式：AES，DESC
     */
    private String sortOrder;
}

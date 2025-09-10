package com.inspur.dsp.direct.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetDataElementPageDto {

    /**
     * 采集单位code集合
     */
    private List<String> deptCodes;
    /**
     * 发起时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date initiationTimeEnd;
    /**
     * 发起时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date initiationTimeStart;
    /**
     * 页码
     */
    private long pageNum;
    /**
     * 每页条数
     */
    private long pageSize;
    /**
     * 模糊搜索：基准数据元名称、定义、数源单位、采集单位
     */
    private String search;
    /**
     * 定源时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date sourceTimeEnd;
    /**
     * 定源时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date sourceTimeStart;
    /**
     * 数据元状态集合
     */
    private List<String> statusList;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序方式
     */
    private String sortOrder;
}

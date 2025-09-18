package com.inspur.dsp.direct.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 分页查询待核定数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPendingApprovalPageDto {

    /**
     * 发起时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date sendDateStart;

    /**
     * 发起时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date sendDateEnd;

    /**
     * 定源时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date confirmDateStart;

    /**
     * 定源时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date confirmDateEnd;

    /**
     * 数源单位code集合
     */
    private List<String> unitCodes;

    /**
     * 基准数据元名称/定义/数源单位名称模糊
     */
    private String search;

    /**
     * 页面状态：待核定传递"pending_approval"，已定源传递"confirmed"
     */
    @NotEmpty(message = "页面状态不能为空")
    private String tabStatus;

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 每页条数
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式
     */
    private String sortOrder;
}
package com.inspur.dsp.direct.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 导出数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportPaDataDto {

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
     * 基准数据元名称/定义/数源单位名称模糊
     */
    private String search;

    /**
     * 页面状态：待核定传递"pending_approval"，已定源传递"confirmed"
     */
    private String tabStatus;

    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序顺序
     */
    private String sortOrder;
}
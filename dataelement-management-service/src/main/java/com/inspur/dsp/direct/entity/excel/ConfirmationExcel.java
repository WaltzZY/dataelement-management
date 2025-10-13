package com.inspur.dsp.direct.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分页查询待核定数据VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationExcel {
    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    private Integer index;
    /**
     * 数据元名称
     */
    @ExcelProperty(value = "数据元名称", index = 1)
    private String name;

    /**
     * 数据元定义描述
     */
    @ExcelProperty(value = "定义", index = 2)
    private String definition;


    /**
     * 采集单位
     */
    @ExcelProperty(value = "采集单位", index = 3)
    private String collectUnit;

    /**
     * 数据元状态描述
     */
    @ExcelProperty(value = "状态", index = 4)
    private String statusDesc;

    /**
     * 发起时间
     */
    @ExcelProperty(value = "发起时间", index = 5)
    private Date sendDate;

    /**
     * 数源单位名称
     */
    @ExcelProperty(value = "数源单位名称", index = 6)
    private String sourceUnitName;

    /**
     * 定源时间
     */
    @ExcelProperty(value = "定源时间", index = 7)
    private Date confirmDate;

}
package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导出已完成协商DTO - 用于导出已完成协商数据
 */
@Data
public class ExportDoneNegoDTO {
    /** 序号 */
    @ExcelProperty(index = 0, value = "序号")
    private String seqNo;

    /** 数据元名称 */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String name;

    /** 数据元描述 */
    @ExcelProperty(index = 2, value = "数据元定义")
    private String definition;

    /** 采集单位 */
    @ExcelProperty(index = 3, value = "采集单位")
    private String collectUnit;

    /** 状态 */
    @ExcelProperty(index = 4, value = "状态")
    private String status;

    /** 发起时间 */
    @ExcelProperty(index = 5, value = "发起时间")
    private Date sendDate;

    /** 数源单位 */
    @ExcelProperty(index = 6, value = "数源单位")
    private String sourceUnitName;

    /** 定源时间 */
    @ExcelProperty(index = 7, value = "定源时间")
    private Date confirmDate;
}
package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导出导入失败清单DTO - 用于导出录入协商结果失败的数据
 */
@Data
public class ExportNegotiationFailDTO {
    /** 序号 */
    @ExcelProperty(index = 0, value = "序号")
    private String seqNo;

    /** 数据元名称 */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String dataElementName;

    /** 数源单位 */
    @ExcelProperty(index = 2, value = "数源单位")
    private String sourceUnitName;

    /** 数源单位统一社会信用代码 */
    @ExcelProperty(index = 3, value = "数源单位统一社会信用代码")
    private String sourceUnitCode;

    /** 失败原因 */
    @ExcelProperty(index = 4, value = "失败原因")
    private String failReason;
}
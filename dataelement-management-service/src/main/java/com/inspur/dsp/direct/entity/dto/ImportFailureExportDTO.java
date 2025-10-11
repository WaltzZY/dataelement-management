package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入失败清单导出DTO
 * 用于导出上传文件处理失败的记录到Excel
 * 
 * @author Claude Code
 * @since 2025-01-25
 */
@Data
public class ImportFailureExportDTO {

    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String serialNumber;

    /**
     * 数据元名称
     */
    @ExcelProperty("数据元名称")
    private String name;

    /**
     * 数源单位统一社会信用代码
     */
    @ExcelProperty("数源单位统一社会信用代码")
    private String unitCode;

    /**
     * 数源单位名称
     */
    @ExcelProperty("数源单位名称")
    private String unitName;

    /**
     * 失败原因
     */
    @ExcelProperty("失败原因")
    private String failReason;
}
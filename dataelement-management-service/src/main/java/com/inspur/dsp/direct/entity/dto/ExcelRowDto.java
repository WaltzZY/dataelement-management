package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel行数据DTO
 * 用于解析上传的Excel文件内容
 * 
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
public class ExcelRowDto {

    /**
     * 序号
     */
    @ExcelProperty(index = 0, value = "序号")
    private String serialNumber;

    /**
     * 数据元名称
     */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String elementName;

    /**
     * 数源单位统一社会信用代码
     */
    @ExcelProperty(index = 2, value = "数源单位统一社会信用代码")
    private String unitCode;
}
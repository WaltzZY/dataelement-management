package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 定数结果Excel行数据DTO
 * 用于解析定数结果导入的Excel文件内容
 * 包含基准数据元和领域数据元信息
 * 
 * @author Claude Code
 * @since 2025-10-20
 */
@Data
public class DetermineResultExcelRowDto {

    /**
     * 序号
     */
    @ExcelProperty(index = 0, value = "序号")
    private String serialNumber;

    /**
     * 基准数据元名称
     */
    @ExcelProperty(index = 1, value = "基准数据元名称")
    private String baseElementName;

    /**
     * 基准数据元定义
     */
    @ExcelProperty(index = 2, value = "基准数据元定义")
    private String baseElementDefinition;

    /**
     * 数据类型
     */
    @ExcelProperty(index = 3, value = "数据类型")
    private String dataType;

    /**
     * 领域数据元名称
     */
    @ExcelProperty(index = 4, value = "领域数据元名称")
    private String domainElementName;

    /**
     * 采集单位统一社会信用代码
     */
    @ExcelProperty(index = 5, value = "采集单位统一社会信用代码")
    private String unitCode;

    /**
     * 领域数据元定义
     */
    @ExcelProperty(index = 6, value = "领域数据元定义")
    private String domainElementDefinition;
}
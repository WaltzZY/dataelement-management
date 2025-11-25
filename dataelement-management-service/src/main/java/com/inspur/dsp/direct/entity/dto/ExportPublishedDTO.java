package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 已发布数据元列表导出DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportPublishedDTO {
    
    /**
     * 数据元名称
     */
    @ExcelProperty("数据元名称")
    private String dataElementName;
    
    /**
     * 数据元编码
     */
    @ExcelProperty("数据元编码")
    private String dataElementCode;
    
    /**
     * 定义
     */
    @ExcelProperty("定义")
    private String definition;
    
    /**
     * 数据类型
     */
    @ExcelProperty("数据类型")
    private String datatype;
    
    /**
     * 数源单位
     */
    @ExcelProperty("数源单位")
    private String sourceUnitName;
    
    /**
     * 状态
     */
    @ExcelProperty("状态")
    private String statusDesc;
    
    /**
     * 报送时间
     */
    @ExcelProperty("报送时间")
    private Date reportTime;
}
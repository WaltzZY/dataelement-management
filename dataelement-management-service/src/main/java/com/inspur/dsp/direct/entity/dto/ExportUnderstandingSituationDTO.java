package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 我要了解情况列表导出DTO (019页面)
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportUnderstandingSituationDTO {
    
    /**
     * 基准数据元名称
     */
    @ExcelProperty("基准数据元名称")
    private String name;
    
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
     * 状态
     */
    @ExcelProperty("状态")
    private String statusDesc;
    
    /**
     * 定源时间
     */
    @ExcelProperty("定源时间")
    private Date confirmDate;
    
    /**
     * 发布时间
     */
    @ExcelProperty("发布时间")
    private Date publishTime;
}
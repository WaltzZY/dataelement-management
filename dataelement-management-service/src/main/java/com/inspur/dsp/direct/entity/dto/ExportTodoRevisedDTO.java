package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 待修订列表导出DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportTodoRevisedDTO {
    
    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String serialNumber;
    
    /**
     * 数据元名称
     */
    @ExcelProperty("名称")
    private String name;
    
    /**
     * 数据元编码
     */
    @ExcelProperty("标识符")
    private String dataelementCode;
    
    /**
     * 数据元定义
     */
    @ExcelProperty("定义")
    private String definition;
    
    /**
     * 数据类型
     */
    @ExcelProperty("数据类型")
    private String datatype;
    
    /**
     * 定源时间
     */
    @ExcelProperty("定源时间")
    private Date confirmDate;
    
    /**
     * 发起修订时间
     */
    @ExcelProperty("发起修订时间")
    private Date revisionCreateDate;
    
    /**
     * 状态描述
     */
    @ExcelProperty("状态")
    private String statusDesc;
}
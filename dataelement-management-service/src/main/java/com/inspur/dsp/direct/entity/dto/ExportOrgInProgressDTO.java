package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 组织方在办列表导出DTO (022页面)
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportOrgInProgressDTO {
    
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
     * 提交时间
     */
    @ExcelProperty("提交时间")
    private Date submitTime;
    
    /**
     * 修订提交时间
     */
    @ExcelProperty("修订提交时间")
    private Date revisionSubmitTime;
}
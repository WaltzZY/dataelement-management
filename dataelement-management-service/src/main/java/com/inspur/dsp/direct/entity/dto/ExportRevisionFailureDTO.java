package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 修订失败条目导出DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportRevisionFailureDTO {
    
    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String serialNumber;
    
    /**
     * 数据元名称
     */
    @ExcelProperty("数据元名称")
    private String dataElementName;
    
    /**
     * 数源单位
     */
    @ExcelProperty("数源单位")
    private String sourceUnitName;
    
    /**
     * 修订意见
     */
    @ExcelProperty("修订意见")
    private String revisionSuggestion;
    
    /**
     * 失败原因
     */
    @ExcelProperty("失败原因")
    private String failureReason;
}
package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.util.Date;

/**
 * 导出待发布状态列表DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportTodoReleasedDTO {
    
    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String serialNumber;
    
    /**
     * 数据元名称
     */
    @ExcelProperty("基准数据元名称")
    private String name;
    
    /**
     * 数据元编码
     */
    @ExcelProperty("数据元编码")
    private String dataElementId;
    
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
     * 数源单位
     */
    @ExcelProperty("数源单位")
    private String sourceUnitName;
    
    /**
     * 状态描述
     */
    @ExcelProperty("状态")
    private String statusDesc;
    
    /**
     * 报送时间
     */
    @ExcelProperty("报送时间")
    private Date lastSubmitReleasedDate;
}
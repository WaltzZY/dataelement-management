package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.util.Date;

/**
 * 导出组织方已处理状态列表DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ExportDoneOrganizerDTO {
    
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
     * 审核时间
     */
    @ExcelProperty("审核时间")
    private Date lastApproveDate;
    
    /**
     * 发起修订时间
     */
    @ExcelProperty("发起修订时间")
    private Date lastInitiateRevisedDate;
    
    /**
     * 发布时间
     */
    @ExcelProperty("发布时间")
    private Date publishDate;
}
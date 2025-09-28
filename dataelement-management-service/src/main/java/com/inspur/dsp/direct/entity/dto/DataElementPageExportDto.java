package com.inspur.dsp.direct.entity.dto;



import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 数据元分页列表导出DTO
 * 用于导出getAllDataElementPage查询结果
 *
 * @author Claude Code
 * @since 2025-09-24
 */
@Data
public class DataElementPageExportDto {

    /**
     * 序号
     */
    @ExcelProperty(index = 0, value = "序号")
    private String serialNumber;

    /**
     * 数据元名称
     */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String name;

    /**
     * 数据元定义描述
     */
    @ExcelProperty(index = 2, value = "数据元定义")
    private String definition;

    /**
     * 状态描述
     */
    @ExcelProperty(index = 3, value = "状态")
    private String statusDesc;

    /**
     * 数源单位名称
     */
    @ExcelProperty(index = 4, value = "数源单位")
    private String sourceUnitName;

    /**
     * 采集单位名称
     */
    @ExcelProperty(index = 5, value = "采集单位")
    private String collectDeptName;

    /**
     * 发起时间
     */
    @ExcelProperty(index = 6, value = "发起时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /**
     * 定源时间
     */
    @ExcelProperty(index = 7, value = "定源时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDate;

}
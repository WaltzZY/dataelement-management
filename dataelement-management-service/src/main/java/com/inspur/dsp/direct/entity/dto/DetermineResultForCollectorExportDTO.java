package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 基准数据元表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetermineResultForCollectorExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(index = 0, value = "序号")
    private Integer id;
    @ExcelProperty(index = 1, value = "数据元名称")
    private String name;
    @ExcelProperty(index = 2, value = "定义")
    private String definition;
    @ExcelProperty(index = 3, value = "采集单位")
    private String sourceUnitName;
    @ExcelProperty(index = 4, value = "数据类型")
    private String datatype;
    @ExcelProperty(index = 5, value = "定源时间")
    private Date sendDate;



}
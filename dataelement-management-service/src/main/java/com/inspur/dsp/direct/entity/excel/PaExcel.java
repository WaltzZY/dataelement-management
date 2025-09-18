package com.inspur.dsp.direct.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 导出待核定基准数据元信息,与页面展示相同
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaExcel {

    /**
     * 数据元名称
     */
    @ExcelProperty(value = "数据元名称", index = 0)
    private String name;

    /**
     * 数据元定义描述
     */
    @ExcelProperty(value = "定义", index = 1)
    private String definition;

    /**
     * 数据元状态描述
     */
    @ExcelProperty(value = "状态", index = 2)
    private String statusDesc;

    /**
     * 待核定数源单位名称
     */
    @ExcelProperty(value = "待核定数源单位", index = 3)
    private String paUnitName;

    /**
     * 发起时间
     */
    @ExcelProperty(value = "发起时间", index = 4)
    private Date sendDate;
}
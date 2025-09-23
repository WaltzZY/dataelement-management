package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入协商结果DTO - 导入时，文件中列名和目标对象的转换MAP结构
 */
@Data
public class ImportNegotiationResutDTO {

    /**
     * 序号
     */
    @ExcelProperty(index = 0)
    private String seqNo;

    /** 数据元名称 */
    @ExcelProperty(index = 1)
    private String dataElementName;

    /** 数源单位code */
    @ExcelProperty(index = 2)
    private String unitCode;
}
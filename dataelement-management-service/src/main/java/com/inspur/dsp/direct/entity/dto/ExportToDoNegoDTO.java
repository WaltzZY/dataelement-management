package com.inspur.dsp.direct.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导出待协商DTO - 用于导出待协商数据
 */
@Data
public class ExportToDoNegoDTO {
    /** 序号 */
    @ExcelProperty(index = 0, value = "序号")
    private String seqNo;

    /** 数据元名称 */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String name;

    /** 同意成为数源单位的采集单位 */
    @ExcelProperty(index = 2, value = "同意成为数源单位的采集单位")
    private String agreeUnit;

    /** 拒绝成为数源单位的采集单位 */
    @ExcelProperty(index = 3, value = "拒绝成为数源单位的采集单位")
    private String refuseUnit;

    /** 状态 */
    @ExcelProperty(index = 4, value = "状态")
    private String status;

    /** 发起时间 */
    @ExcelProperty(index = 5, value = "发起时间")
    private Date sendDate;
}
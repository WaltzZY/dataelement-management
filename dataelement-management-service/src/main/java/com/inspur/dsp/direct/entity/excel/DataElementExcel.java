package com.inspur.dsp.direct.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.enums.StatusEnums;
import lombok.Data;

import java.util.Date;

@Data
public class DataElementExcel {

    /**
     * 数据元名称
     */
    @ExcelProperty(index = 0, value = "数据元名称")
    private String name;
    /**
     * 数据元定义描述
     */
    @ExcelProperty(index = 1, value = "定义")
    private String definition;
    /**
     * 采集单位名称
     */
    @ExcelProperty(index = 2, value = "采集单位")
    private String collectDeptName;
    /**
     * 状态说明
     */
    @ExcelProperty(index = 3, value = "状态")
    private String statusDesc;
    /**
     * 数源单位名称
     */
    @ExcelProperty(index = 4, value = "数源单位")
    private String sourceUnitName;
    /**
     * 发起时间
     */
    @ExcelProperty(index = 5, value = "发起时间")
    private Date sendDate;
    /**
     * 定源时间
     */
    @ExcelProperty(index = 6, value = "定源时间")
    private Date publishDate;

    public static DataElementExcel toExcel(DataElementPageInfoVo vo) {
        DataElementExcel excel = new DataElementExcel();
        excel.setName(vo.getName());
        excel.setDefinition(vo.getDefinition());
        excel.setCollectDeptName(vo.getCollectDeptName());
        excel.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        excel.setSourceUnitName(vo.getSourceUnitName());
        excel.setSendDate(vo.getSendDate());
        excel.setPublishDate(vo.getPublishDate());
        return excel;
    }
}

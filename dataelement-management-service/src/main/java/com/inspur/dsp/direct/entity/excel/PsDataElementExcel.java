package com.inspur.dsp.direct.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.enums.StatusEnums;
import lombok.Data;

@Data
public class PsDataElementExcel {

    /**
     * 序号
     */
    @ExcelProperty(index = 0, value = "序号")
    private Integer index;
    /**
     * 数据元名称
     */
    @ExcelProperty(index = 1, value = "数据元名称")
    private String name;
    /**
     * 数据元定义描述
     */
    @ExcelProperty(index = 2, value = "定义")
    private String definition;
    /**
     * 采集单位名称
     */
    @ExcelProperty(index = 3, value = "采集单位")
    private String collectDeptName;
    /**
     * 状态说明
     */
    @ExcelProperty(index = 4, value = "状态")
    private String statusDesc;

    public static PsDataElementExcel toExcel(DataElementPageInfoVo vo, Integer index) {
        PsDataElementExcel excel = new PsDataElementExcel();
        excel.setIndex(index);
        excel.setName(vo.getName());
        excel.setDefinition(vo.getDefinition());
        excel.setCollectDeptName(vo.getCollectDeptName());
        excel.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        return excel;
    }
}

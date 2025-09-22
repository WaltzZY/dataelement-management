package com.inspur.dsp.direct.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import lombok.Data;

/**
 * 采集方待处理数据
 */
@Data
public class PendingCollectDataExcel {

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
     * 数据类型
     */
    @ExcelProperty(index = 2, value = "数据类型")
    private String datatype;
    /**
     * 数据元状态描述
     */
    @ExcelProperty(index = 3, value = "状态")
    private String statusDesc;
    /**
     * 接收时间
     */
    @ExcelProperty(index = 4, value = "接收时间")
    private String receiveTime;

    public static PendingCollectDataExcel toExcel(GetCollectDataVo vo) {
        PendingCollectDataExcel pendingCollectDataExcel = new PendingCollectDataExcel();
        pendingCollectDataExcel.setName(vo.getName());
        pendingCollectDataExcel.setDefinition(vo.getDefinition());
        pendingCollectDataExcel.setDatatype(vo.getDatatype());
        pendingCollectDataExcel.setStatusDesc(ConfirmationTaskEnums.getDescByCode(vo.getStatus()));
        pendingCollectDataExcel.setReceiveTime(vo.getReceiveTime());
        return pendingCollectDataExcel;
    }
}

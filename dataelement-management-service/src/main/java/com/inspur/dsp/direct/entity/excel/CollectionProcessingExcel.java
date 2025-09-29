package com.inspur.dsp.direct.entity.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CollectionProcessingExcel {

    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    private String seq;

    /**
     * 数据元名称
     */
    @ExcelProperty(value = "数据元名称", index = 1)
    private String name;

    /**
     * 数据元定义描述
     */
    @ExcelProperty(value = "定义", index = 2)
    private String definition;

    /**
     * 数据类型
     */
    @ExcelProperty(value = "数据类型", index = 3)
    private String type;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 4)
    private String status;

    /**
     * 发起时间
     */
    @ExcelProperty(value = "发起时间", index = 5)
    private LocalDateTime sendDate;
}

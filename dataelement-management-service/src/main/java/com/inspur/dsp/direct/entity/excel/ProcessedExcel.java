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
public class ProcessedExcel {
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
     * 数据类型
     */
    @ExcelProperty(value = "数据类型", index = 2)
    private String type;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 3)
    private String status;

    /**
     * 发起时间
     */
    @ExcelProperty(value = "接受时间", index = 4)
    private LocalDateTime sendDate;

    /**
     * 处理时间
     */
    @ExcelProperty(value = "处理时间", index = 5)
    private LocalDateTime processDate;
}

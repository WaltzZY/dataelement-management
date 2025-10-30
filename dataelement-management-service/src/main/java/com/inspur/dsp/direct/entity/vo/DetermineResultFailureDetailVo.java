package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 定数结果导入失败记录详情VO
 * 封装定数结果导入失败记录的详细信息
 * 
 * @author Claude Code
 * @since 2025-10-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetermineResultFailureDetailVo {

    /**
     * 序号
     */
    private String serialNumber;

    /**
     * 基准数据元名称
     */
    private String baseElementName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 基准数据元定义
     */
    private String baseElementDefinition;

    /**
     * 失败原因
     */
    private String failReason;
}
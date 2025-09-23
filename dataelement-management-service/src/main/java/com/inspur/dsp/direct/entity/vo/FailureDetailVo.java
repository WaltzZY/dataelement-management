package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 失败记录详情VO
 * 封装上传文件处理失败记录的详细信息
 * 
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailureDetailVo {

    /**
     * 序号
     */
    private String serialNumber;

    /**
     * 数据元名称
     */
    private String elementName;

    /**
     * 数源单位统一社会信用代码
     */
    private String unitCode;

    /**
     * 数源单位名称
     */
    private String unitName;

    /**
     * 失败原因
     */
    private String failureReason;
}
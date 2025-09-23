package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

/**
 * 导入协商失败明细DTO - 协商中导入协商结果后的未成功信息
 */
@Data
public class ImportNegotiationFailDetailDTO {
    /** 数据元dataid */
    private String dataid;

    /** 数据元名称 */
    private String name;

    /** 单位编码 */
    private String unit_code;

    /** 单位名称 */
    private String unit_name;

    /** 失败原因 */
    private String failReason;
}
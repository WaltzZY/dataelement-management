package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 采集单位确认状态VO - 用于展示采集单位确认或认领的情况
 */
@Data
public class CollectUnitsAndStatusVO {
    /** 状态 */
    private Boolean status;

    /** 单位统一社会信用代码 */
    private String unitCode;

    /** 单位名称 */
    private String unitName;
}

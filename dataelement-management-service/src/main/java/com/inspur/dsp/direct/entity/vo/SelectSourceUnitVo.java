package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

@Data
public class SelectSourceUnitVo {

    /**
     * 负责人name
     */
    private String curatorName;
    /**
     * 父级单位id
     */
    private String parentUnitId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 单位code
     */
    private String unitCode;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 单位名称
     */
    private String unitName;
}

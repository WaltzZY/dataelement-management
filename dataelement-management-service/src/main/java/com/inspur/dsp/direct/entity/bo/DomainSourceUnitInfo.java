package com.inspur.dsp.direct.entity.bo;

import lombok.Data;

/**
 * 领域数据元,提供单位信息
 */
@Data
public class DomainSourceUnitInfo {

    /**
     * 领域数据元唯一标识
     */
    private String dataid;

    /**
     * 基准数据元数据行标识
     */
    private String baseDataelementDataid;

    /**
     * 提供单位统一社会信用代码
     */
    private String sourceUnitCode;

    /**
     * 提供单位名称
     */
    private String sourceUnitName;
}

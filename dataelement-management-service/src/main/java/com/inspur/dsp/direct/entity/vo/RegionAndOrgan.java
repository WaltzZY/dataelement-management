package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RegionAndOrgan {

    /**
     * 国家部委列表
     */
    private List<OrganMinistriesAndCommissions> organ;
    /**
     * 行政区划列表
     */
    private List<RegionProvinceVO> region;
}

package com.inspur.dsp.direct.entity.vo;

import com.inspur.dsp.direct.entity.bo.bsp.OrganInfo;
import lombok.Data;

@Data
public class RegionProvinceVO {

    /**
     * 区域code
     */
    private String code;
    /**
     * 区域名称
     */
    private String name;

    public static RegionProvinceVO toRegionProvinceVO(OrganInfo organInfo){
        RegionProvinceVO regionProvinceVO = new RegionProvinceVO();
        regionProvinceVO.setCode(organInfo.getCODE());
        regionProvinceVO.setName(organInfo.getNAME());
        return regionProvinceVO;
    }
}

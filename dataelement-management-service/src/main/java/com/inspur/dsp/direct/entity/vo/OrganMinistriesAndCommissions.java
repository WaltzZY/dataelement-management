package com.inspur.dsp.direct.entity.vo;

import com.inspur.dsp.direct.entity.bo.bsp.OrganInfo;
import lombok.Data;

/**
 * 国家部委
 */
@Data
public class OrganMinistriesAndCommissions {

    /**
     * 部委code
     */
    private String code;
    /**
     * 部委name
     */
    private String name;


    public static OrganMinistriesAndCommissions toOmac(OrganInfo organInfo){
        OrganMinistriesAndCommissions organMinistriesAndCommissions = new OrganMinistriesAndCommissions();
        organMinistriesAndCommissions.setCode(organInfo.getCODE());
        organMinistriesAndCommissions.setName(organInfo.getNAME());
        return organMinistriesAndCommissions;
    }
}

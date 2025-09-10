package com.inspur.dsp.direct.enums;

/**
 * 组织: organ
 * 区划: region
 */
public enum OrganRegionEnums {

    ORGAN("organ", "组织"),
    REGION("region", "区划"),

    ;
    private final String code;
    private final String desc;

    OrganRegionEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}

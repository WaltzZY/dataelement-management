package com.inspur.dsp.direct.enums;


/**
 * 流程配置主表状态枚举
 */
public enum FlowStatusEnums {

    // 启用，Enable
    ENABLE("Enable", "启用"),
    // 禁用，Disable
    DISABLE("Disable", "禁用");

    ;
    private final String value;
    private final String name;

    FlowStatusEnums(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getName(String value) {
        for (FlowStatusEnums c : FlowStatusEnums.values()) {
            if (c.value.equals(value)) {
                return c.name;
            }
        }
        return null;
    }
}

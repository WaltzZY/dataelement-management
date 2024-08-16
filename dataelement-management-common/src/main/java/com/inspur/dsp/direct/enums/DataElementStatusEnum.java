package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum DataElementStatusEnum {

    // 数据元状态:01 待定源，02 待定标，03 已发布，04 已废弃
    WAIT_SOURCE("01", "待定源"),
    WAIT_STANDARD("02", "待定标"),
    PUBLISHED("03", "已发布"),
    DISCARDED("04", "已废弃"),

    ;
    private final String code;
    private final String name;

    DataElementStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (DataElementStatusEnum dataElementStatusEnum : DataElementStatusEnum.values()) {
            if (dataElementStatusEnum.getCode().equals(code)) {
                return dataElementStatusEnum.getName();
            }
        }
        return null;
    }
}

package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 数据元状态枚举类
 * 待定源 pending_source
 * 确认中 confirming
 * 待核定 pending_approval
 * 已拒绝 rejected
 * 协商中 negotiating
 * 已定源 confirmed
 */
public enum StatusEnums {
    PENDING_SOURCE("pending_source", "待定源"),
    CONFIRMING("confirming", "确认中"),
    PENDING_APPROVAL("pending_approval", "待核定"),
    REJECTED("rejected", "已拒绝"),
    NEGOTIATING("negotiating", "协商中"),
    CONFIRMED("confirmed", "已定源");

    ;

    private final String code;
    private final String desc;

    StatusEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (StatusEnums statusEnums : StatusEnums.values()) {
            if (statusEnums.getCode().equals(code)) {
                return statusEnums.getDesc();
            }
        }
        return Constants.EMPTY_STRING;
    }
}

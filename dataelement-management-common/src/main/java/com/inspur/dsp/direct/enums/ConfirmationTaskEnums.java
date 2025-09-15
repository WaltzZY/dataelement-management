package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 确认任务枚举
 * 状态:
 * 待确认 PENDING
 * 确认 CONFIRMED
 * 拒绝 REJECTED
 */
public enum ConfirmationTaskEnums {

    PENDING("pending", "待确认"),
    CONFIRMED("confirmed", "确认"),
    REJECTED("rejected", "拒绝");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

    ConfirmationTaskEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (ConfirmationTaskEnums value : ConfirmationTaskEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return Constants.EMPTY_STRING;
    }
}

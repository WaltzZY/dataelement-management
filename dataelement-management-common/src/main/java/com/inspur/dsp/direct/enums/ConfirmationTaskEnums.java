package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 确认任务枚举
 * 状态:
 * 待确认 PENDING
 * 确认 CONFIRMED
 * 拒绝 REJECTED
 * <p>
 * 认领任务表状态:
 * pending_claimed - 待认领
 * claimed - 认领
 * not_claimed - 不认领
 */
public enum ConfirmationTaskEnums {

    // 确认状态
    PENDING("pending", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    REJECTED("rejected", "已拒绝"),

    // 认领状态
    PENDING_CLAIMED("pending_claimed", "待认领"),
    CLAIMED("claimed", "已认领"),
    NOT_CLAIMED("not_claimed", "不认领");;

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

package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 认领任务表状态:
 * pending_claimed - 待认领
 * claimed - 认领
 * not_claimed - 不认领
 */
public enum ClaimTaskStatusEnums {

    PENDING_CLAIMED("pending_claimed", "待认领"),
    CLAIMED("claimed", "认领"),
    NOT_CLAIMED("not_claimed", "不认领");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ClaimTaskStatusEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (ClaimTaskStatusEnums value : ClaimTaskStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return Constants.EMPTY_STRING;
    }
}

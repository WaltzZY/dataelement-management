package com.inspur.dsp.direct.entity.enums;

import lombok.Getter;

public enum DataElementStatus {
    PENDING_SOURCE("pending_source","待定源" ),
    CONFIRMING("confirming","确认中" ),
    CLAIMED_ING("claimed_ing","认领中" ),
    PENDING_APPROVAL("pending_approval","待核定" ),
    PENDING_NEGOTIATION("pending_negotiation","待协商" ),
    NEGOTIATING("negotiating","协商中" ),
    DESIGNATED_SOURCE("designated_source","已定源" ),
    REJECTED("rejected","已拒绝" ),
    PENDING_CLAIMED("pending_claimed","待认领" ),
    CLAIMED("claimed","已认领" ),
    NOT_CLAIMED("not_claimed","不认领" ),
    PENDING("pending","待确认" ),
    CONFIRMED("confirmed","已确认" );


    @Getter
    private final String status;
    @Getter
    private final String remark;

    DataElementStatus(String status, String remark) {
        this.status = status;
        this.remark = remark;
    }

    public static DataElementStatus fromString(String status) {
        for (DataElementStatus s : DataElementStatus.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("不支持的数据元状态: " + status);
    }
}
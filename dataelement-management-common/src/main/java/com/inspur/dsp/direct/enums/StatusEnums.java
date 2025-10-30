package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 数据元状态枚举类
 * 待定源 pending_source
 * 确认中 confirming
 * 认领中 claimed_ing
 * 待核定 pending_approval
 * 待协商 pending_negotiation
 * 协商中 negotiating
 * 已定源 designated_source
 */
public enum StatusEnums {
    // 定源阶段状态枚举
    PENDING_SOURCE("pending_source", "待定源"),
    CONFIRMING("confirming", "确认中"),
    CLAIMED("claimed_ing", "认领中"),
    PENDING_APPROVAL("pending_approval", "待核定"),
    PENDING_NEGOTIATION("pending_negotiation", "待协商"),
    NEGOTIATING("negotiating", "协商中"),
    DESIGNATED_SOURCE("designated_source", "已定源"),

    // 定标阶段状态枚举
    // 待定标: TodoDetermined
    TODODETERMINED("TodoDetermined", "待定标"),
    // 待审核: PendingReview
    PENDINGREVIEW("PendingReview", "待审核"),
    // 征求意见: SolicitingOpinions
    SOLICITINGOPINIONS("SolicitingOpinions", "征求意见"),
    // 待修订: TodoRevised
    TODOREVISED("TodoRevised", "待修订"),
    // 待复审: PendingReExamination
    PENDINGREEXAMINATION("PendingReExamination", "待复审"),
    // 待发布: Todoreleased
    TODORELEASED("Todoreleased", "待发布"),
    // 已发布: Published
    PUBLISHED("Published", "已发布"),

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

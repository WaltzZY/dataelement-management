package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 数据元状态枚举类 (定标阶段)
 */
public enum CalibrationStatusEnums {
    // 定标阶段状态枚举
    // 待定标: TodoDetermined
    TODODETERMINED("designated_source", "待定标"),
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

    CalibrationStatusEnums(String code, String desc) {
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
        for (CalibrationStatusEnums statusEnums : CalibrationStatusEnums.values()) {
            if (statusEnums.getCode().equals(code)) {
                return statusEnums.getDesc();
            }
        }
        return Constants.EMPTY_STRING;
    }
}

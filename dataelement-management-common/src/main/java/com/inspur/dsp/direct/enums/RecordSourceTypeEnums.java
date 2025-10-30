package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;

/**
 * 定源类型
 * 确认型核定 - confirmation_approval
 * 认领型核定 - claim_approval
 * 协商结果录入 - negotiation_result_entry
 * 手动定源 - manual_source
 * 导入定源 - import_source
 */
public enum RecordSourceTypeEnums {

    CONFIRMATION_APPROVAL("confirmation_approval", "确认型核定"),
    CLAIM_APPROVAL("claim_approval", "认领型核定"),
    NEGOTIATION_RESULT_ENTRY("negotiation_result_entry", "协商结果录入"),
    MANUAL_SOURCE("manual_source", "手动定源"),
    IMPORT_SOURCE("import_source", "导入定源"),
    IMPORT_DETERMINE_RESULT("import_determine_result", "导入定数");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    RecordSourceTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (RecordSourceTypeEnums item : RecordSourceTypeEnums.values()) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return Constants.EMPTY_STRING;
    }
}

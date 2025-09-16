package com.inspur.dsp.direct.enums;


/**
 * 任务类型:
 * 确认任务 confirmation_task
 * 认领任务 claim_task
 */
public enum TaskTypeEnums {

    CONFIRMATION_TASK("confirmation_task", "确认任务"),
    CLAIM_TASK("claim_task", "认领任务"),


    ;
    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    TaskTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (TaskTypeEnums taskType : TaskTypeEnums.values()) {
            if (taskType.getCode().equals(code)) {
                return taskType.getDesc();
            }
        }
        return null;
    }
}

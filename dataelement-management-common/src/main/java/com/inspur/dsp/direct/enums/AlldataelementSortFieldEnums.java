package com.inspur.dsp.direct.enums;

import lombok.Getter;

/**
 * 数据元列表排序字段枚举
 *
 * @author Claude Code
 * @since 2025-09-25
 */
@Getter
public enum AlldataelementSortFieldEnums {

    /**
     * 采集单位数量
     */
    COLLECT_UNIT_QTY("collectunitqty", "collectunitqty", "采集单位数量"),

    /**
     * 状态
     */
    STATUS("status", "status", "状态"),

    /**
     * 数源单位名称
     */
    SOURCE_UNIT_NAME("sourceUnitName", "source_unit_name", "数源单位"),

    /**
     * 发起时间
     */
    SEND_DATE("sendDate", "send_date", "发起时间"),

    /**
     * 定源时间
     */
    CONFIRM_DATE("confirmDate", "confirm_date", "定源时间");

    /**
     * 前端传递的字段名
     */
    private final String field;

    /**
     * 对应的数据库字段名
     */
    private final String dbField;

    /**
     * 字段描述
     */
    private final String description;

    AlldataelementSortFieldEnums(String field, String dbField, String description) {
        this.field = field;
        this.dbField = dbField;
        this.description = description;
    }

    /**
     * 根据字段名获取枚举
     *
     * @param field 字段名
     * @return 对应的枚举，如果不存在返回null
     */
    public static AlldataelementSortFieldEnums getByField(String field) {
        if (field == null || field.trim().isEmpty()) {
            return null;
        }

        for (AlldataelementSortFieldEnums value : values()) {
            if (value.field.equals(field)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 校验排序字段是否有效
     *
     * @param field 排序字段
     * @return 是否有效
     */
    public static boolean isValidSortField(String field) {
        return getByField(field) != null;
    }

    /**
     * 获取所有支持的排序字段
     *
     * @return 排序字段列表
     */
    public static String[] getAllSortFields() {
        AlldataelementSortFieldEnums[] enums = values();
        String[] fields = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            fields[i] = enums[i].field;
        }
        return fields;
    }
}
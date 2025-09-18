package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum PaPageSortFieldEnums {

    // 待核定数源单位：paUnitCode，
    PA_UNIT_CODE("paUnitCode", "ct.processing_unit_code"),
    // 发起时间: sendDate
    SEND_DATE("sendDate", "bdl.send_date"),
    // 数源单位：sourceUnitCode
    SOURCE_UNIT_CODE("sourceUnitCode", "bdl.sourceUnitCode"),
    // 处理时间
    CONFIRM_DATE("confirmDate", "bdl.confirm_date"),


    ;
    private final String field;
    private final String orderBySql;

    PaPageSortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    public static String getSortFieldSql(String sortField) {
        for (PaPageSortFieldEnums value : values()) {
            if (value.field.equals(sortField)) {
                return value.orderBySql;
            }
        }
        return null;
    }

    /**
     * 获取要拼接的排序sql
     */
    public static String getOrderBySql(String sortField, String sortOrder) {
        // 如果sortField为空，则返回空, 如果sortOrder不为 aes 或者 desc 则返回空
        if (sortField == null || !(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
            return null;
        }
        return " " + getSortFieldSql(sortField) + " " + sortOrder;
    }

}

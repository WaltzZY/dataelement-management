package com.inspur.dsp.direct.enums;

import lombok.Getter;

/**
 * getDataElement 方法排序枚举
 */
@Getter
public enum DeSortFieldEnums {

    // 采集单位数量
    COLLECTUNITQTY("collectUnitqty", "collectunitqty"),

    // 状态
    STATUS("status", "status"),

    // 发起时间
    SENDDATE("sendDate", "send_date"),


    ;
    private final String field;
    private final String orderBySql;

    DeSortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    public static String getSortFieldSql(String sortField) {
        for (DeSortFieldEnums value : values()) {
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

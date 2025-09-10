package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum SortFieldEnums {

    // 采集单位  collectUnitCode
    COLLECT_UNIT_CODE("collectUnitCode", "dde.source_unit_code"),
    // 状态  status
    STATUS("status", "bde.status"),
    // 数源单位  sourceUnitCode
    SOURCE_UNIT_CODE("sourceUnitCode", "bde.source_unit_code"),
    // 发起时间
    SEND_DATE("sendDate", "bde.send_date"),
    // 定源时间
    PUBLISH_DATE("publishDate", "bde.publish_date"),


    ;
    private final String field;
    private final String orderBySql;

    SortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    public static String getSortFieldSql(String sortField) {
        for (SortFieldEnums value : values()) {
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

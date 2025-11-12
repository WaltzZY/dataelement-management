package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum CollSortFieldEnums {

    // 数据类型：datatype，
    DATA_TYPE("dataType", "bde.datatype"),
    // 状态：status，
    STATUS("status", "bde.status"),
    // 接收时间：receiveTime
    RECEIVE_TIME("sendDate", "bde.send_date"),
    // 处理时间
    PROCESS_TIME("processingDate", "ct.processing_date"),


    ;
    private final String field;
    private final String orderBySql;

    CollSortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    public static String getSortFieldSql(String sortField) {
        for (CollSortFieldEnums value : values()) {
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

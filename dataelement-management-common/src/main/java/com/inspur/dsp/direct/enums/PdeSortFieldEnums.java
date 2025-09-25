package com.inspur.dsp.direct.enums;

import lombok.Getter;

/**
 * getProcessedDataElement 方法排序枚举
 */
@Getter
public enum PdeSortFieldEnums {

    // 数据类型  collectUnitCode
    DATATYPE("datatype", "bde.datatype"),
    // 状态  status
    STATUS("status", "ct.status"),
    // 接收时间  recieveTime
    RECIEVE_TIME("recieveTime", "ct.send_date"),
    // 处理时间
    PROCESSING_DATE("processingDate", "ct.processing_date"),

    ;
    private final String field;
    private final String orderBySql;

    PdeSortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    public static String getSortFieldSql(String sortField) {
        for (PdeSortFieldEnums value : values()) {
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

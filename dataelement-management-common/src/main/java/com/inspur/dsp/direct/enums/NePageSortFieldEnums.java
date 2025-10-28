package com.inspur.dsp.direct.enums;

import com.inspur.dsp.common.utils.StrUtil;
import lombok.Getter;

@Getter
public enum NePageSortFieldEnums {

    // 发起时间: sendDate
    SEND_DATE("sendDate", "bde.send_date"),
    // 定源时间: confirmDate
    CONFIRM_DATE("confirmDate", "bde.confirm_date"),


    ;
    private final String field;
    private final String orderBySql;

    NePageSortFieldEnums(String field, String orderBySql) {
        this.field = field;
        this.orderBySql = orderBySql;
    }

    private static String getSortFieldSql(String sortField) {
        for (NePageSortFieldEnums value : values()) {
            if (value.field.equals(sortField)) {
                return value.orderBySql;
            }
        }
        return null;
    }

    private static String getSortField(String sortField) {
        try {
            return StrUtil.toUnderlineCase(sortField);
        } catch (Exception e) {
            return null;
        }

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

    /**
     * 获取要拼接的排序sql
     */
    public static String getOrderByField(String sortField, String sortOrder) {
        // 如果sortField为空，则返回空, 如果sortOrder不为 aes 或者 desc 则返回空
        if (sortField == null || !(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
            return null;
        }
        return " " + getSortField(sortField) + " " + sortOrder;
    }

    public static void main(String[] args) {

        String camelCase = StrUtil.toUnderlineCase("processingDate");
        System.out.println(camelCase);

    }

}

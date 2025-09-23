package com.inspur.dsp.direct.common;

import com.inspur.dsp.direct.entity.enums.DataElementStatus;

import java.util.ArrayList;
import java.util.List;

public class StatusUtil {

    public static String getStatusChinese(String status) {
        DataElementStatus dataElementStatus;
        dataElementStatus = DataElementStatus.fromString(status);
        return dataElementStatus.getRemark();
    }

    /**
     * 根据状态列表构建过滤条件
     *
     * @param statusList 状态列表
     * @return 包含主表条件集合和从表条件集合的数组
     */
    public static List<String>[] buildStatusConditions(List<String> statusList) {
        List<String> mainConditions = new ArrayList<>();
        List<String> subConditions = new ArrayList<>();

        if (statusList != null) {
            for (String status : statusList) {
                switch (status) {
                    case "pending":
                    case "pending_claimed":
                        // 使用从表的status进行查询
                        subConditions.add(status);
                        break;
                    case "confirmed":
                        // 从表的过滤字段值为"待核定"，主表的过滤字段值为"已确认"
                        subConditions.add("pending_approval");
                        mainConditions.add("confirmed");
                        break;
                    case "rejected":
                        // 从表的过滤字段值为"待协商"，主表的过滤字段值为"已拒绝"
                        subConditions.add("pending_negotiation");
                        mainConditions.add("rejected");
                        break;

                    case "claimed":
                        // 主表的过滤字段值为"认领中或者待核定或者待协商"，从表的过滤字段值为"已认领"
                        mainConditions.add("claiming");
                        mainConditions.add("pending_approval");
                        mainConditions.add("pending_negotiation");
                        subConditions.add("claimed");
                        break;

                    case "not_claimed":
                        // 主表的过滤字段值为"认领中或者待核定或者待协商"，从表的过滤字段值为"不认领"
                        mainConditions.add("claiming");
                        mainConditions.add("pending_approval");
                        mainConditions.add("pending_negotiation");
                        subConditions.add("not_claimed");
                        break;
                    case "negotiating":
                    case "designated_source":
                        // 使用主表的status进行查询
                        mainConditions.add(status);
                        break;
                }
            }
        }
        // 返回包含两个集合的数组
        List<String>[] result = new ArrayList[2];
        result[0] = mainConditions;
        result[1] = subConditions;
        return result;
    }
}

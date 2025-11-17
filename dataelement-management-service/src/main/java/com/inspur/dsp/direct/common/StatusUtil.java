package com.inspur.dsp.direct.common;

import com.inspur.dsp.direct.entity.enums.DataElementStatus;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.StatusEnums;

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
                    case "pending_source":
                    case "confirming":
                        // 使用从表的status进行查询
                        subConditions.add(status);
                        break;
                    case "confirmed":
                        // 从表的过滤字段值为"待核定"，主表的过滤字段值为"已确认"
                        mainConditions.add("pending_approval");
                        subConditions.add("confirmed");
                        break;
                    case "rejected":
                        // 从表的过滤字段值为"待协商"，主表的过滤字段值为"已拒绝"
                        mainConditions.add("pending_negotiation");
                        subConditions.add("rejected");
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

    /**
     * 获取显示状态
     *
     * @param bdeStatus
     * @param ctStatus
     * @return
     */
    public static String getDisplayStatus(String bdeStatus, String ctStatus) {
        String displayStatus = "";
        // 判断 bdeStatus  negotiating 或  designated_source  displayStatus =  bdeStatus
        if (StatusEnums.NEGOTIATING.getCode().equals(bdeStatus)) {
            displayStatus = StatusEnums.NEGOTIATING.getDesc();
        } else if (StatusEnums.DESIGNATED_SOURCE.getCode().equals(bdeStatus)) {
            displayStatus = StatusEnums.DESIGNATED_SOURCE.getDesc();
        } else if (ConfirmationTaskEnums.PENDING_CLAIMED.getCode().equals(ctStatus)) {
            displayStatus = ConfirmationTaskEnums.PENDING_CLAIMED.getDesc();
        } else {
            // ctStatus = 待确认  displayStatus = 待确认
            if (ConfirmationTaskEnums.PENDING.getCode().equals(ctStatus)) {
                displayStatus = ConfirmationTaskEnums.PENDING.getDesc();
            }
            // ctStatus = 待认领  displayStatus = 待认领
            if (ConfirmationTaskEnums.CLAIMED.getCode().equals(ctStatus)) {
                displayStatus = ConfirmationTaskEnums.CLAIMED.getDesc();
            }
            // ctStatus = 已确认 and  bdeStatus = 待核定  displayStatus = 已确认
            if (ConfirmationTaskEnums.CONFIRMED.getCode().equals(ctStatus) && StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)) {
                displayStatus = ConfirmationTaskEnums.CONFIRMED.getDesc();
            }
            // ctStatus = 已拒绝 and  bdeStatus = 待协商  displayStatus = 已拒绝
            if (ConfirmationTaskEnums.REJECTED.getCode().equals(ctStatus) && StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus)) {
                displayStatus = ConfirmationTaskEnums.REJECTED.getDesc();
            }
            // ctStatus = 已认领 and  bdeStatus = 认领中, 待核定, 待协商  displayStatus = 已认领
            if (ConfirmationTaskEnums.CLAIMED.getCode().equals(ctStatus)
                    && (StatusEnums.CLAIMED.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus))
            ) {
                displayStatus = ConfirmationTaskEnums.CLAIMED.getDesc();
            }
            // ctStatus = 不认领 and  bdeStatus = 认领中, 待核定, 待协商  displayStatus = 不认领
            if (ConfirmationTaskEnums.NOT_CLAIMED.getCode().equals(ctStatus)
                    && (StatusEnums.CLAIMED.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_APPROVAL.getCode().equals(bdeStatus)
                    || StatusEnums.PENDING_NEGOTIATION.getCode().equals(bdeStatus))) {
                displayStatus = ConfirmationTaskEnums.NOT_CLAIMED.getDesc();
            }
        }
        return displayStatus;
    }

}

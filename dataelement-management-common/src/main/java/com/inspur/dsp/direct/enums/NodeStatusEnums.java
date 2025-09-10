package com.inspur.dsp.direct.enums;


import lombok.Getter;

@Getter
public enum NodeStatusEnums {

    // 节点状态，状态, 0-灰色, 1-蓝色, 2-绿色
    WAIT_HANDLE("0", "灰色"),
    HANDLING("1", "蓝色"),
    HANDLE_COMPLETE("2", "绿色")

    ;

    private final String status;
    private final String statusDesc;

    NodeStatusEnums(String status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public static String getStatusDesc(String status) {
        for (NodeStatusEnums nodeStatusEnums : NodeStatusEnums.values()) {
            if (nodeStatusEnums.status.equals(status)) {
                return nodeStatusEnums.statusDesc;
            }
        }
        return null;
    }
}

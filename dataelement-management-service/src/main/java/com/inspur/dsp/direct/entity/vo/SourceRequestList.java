package com.inspur.dsp.direct.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SourceRequestList {

    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点反馈
     */
    private String nodeFeedback;
    /**
     * 节点处理人名称
     */
    private String nodeHandleUserName;
    /**
     * 节点处理结果
     */
    private String nodeResult;
    /**
     * 节点通过时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh", shape = JsonFormat.Shape.STRING)
    private Date passDate;
    /**
     * 节点展示状态，状态, 0-待处理, 1-处理中, 2-处理完成
     */
    private String nodeShowStatus;
}

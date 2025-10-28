package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import lombok.Data;

import java.util.Date;

@Data
public class GetConfirmationTaskVo {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 基准数据元唯一标识
     */
    private String baseDataelementDataid;

    /**
     * 任务类型(确认任务/认领任务)
     */
    private String tasktype;

    /**
     * 发出单位统一社会信用代码
     */
    private String sendUnitCode;

    /**
     * 发出单位名称
     */
    private String sendUnitName;

    /**
     * 发出日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /**
     * 发出人用户账号
     */
    private String senderAccount;

    /**
     * 发出人姓名
     */
    private String senderName;

    /**
     * 确认任务状态(待确认、已确认、已拒绝);认领任务状态(待认领,已认领,不认领)
     */
    private String status;

    /**
     * 状态desc
     */
    private String statusDesc;

    /**
     * 任务处理单位统一社会信用代码
     */
    private String processingUnitCode;

    /**
     * 任务处理单位名称
     */
    private String processingUnitName;

    /**
     * 处理日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date processingDate;

    /**
     * 处理结果
     */
    private String processingResult;

    /**
     * 处理意见
     */
    private String processingOpinion;

    /**
     * 处理人用户账号
     */
    private String processorAccount;

    /**
     * 处理人姓名
     */
    private String processorName;

    /**
     * 基准数据元状态
     */
    private String baseDataelementStatus;
    /**
     * 基准数据元状态desc
     */
    private String baseDataelementStatusDesc;

    public static GetConfirmationTaskVo toVo(ConfirmationTask task) {
        GetConfirmationTaskVo getConfirmationTaskVo = new GetConfirmationTaskVo();
        getConfirmationTaskVo.setTaskId(task.getTaskId());
        getConfirmationTaskVo.setBaseDataelementDataid(task.getBaseDataelementDataid());
        getConfirmationTaskVo.setTasktype(task.getTasktype());
        getConfirmationTaskVo.setSendUnitCode(task.getSendUnitCode());
        getConfirmationTaskVo.setSendUnitName(task.getSendUnitName());
        getConfirmationTaskVo.setSendDate(task.getSendDate());
        getConfirmationTaskVo.setSenderAccount(task.getSenderAccount());
        getConfirmationTaskVo.setSenderName(task.getSenderName());
        getConfirmationTaskVo.setStatus(task.getStatus());
        getConfirmationTaskVo.setProcessingUnitCode(task.getProcessingUnitCode());
        getConfirmationTaskVo.setProcessingUnitName(task.getProcessingUnitName());
        getConfirmationTaskVo.setProcessingDate(task.getProcessingDate());
        getConfirmationTaskVo.setProcessingResult(task.getProcessingResult());
        getConfirmationTaskVo.setProcessingOpinion(task.getProcessingOpinion());
        getConfirmationTaskVo.setProcessorAccount(task.getProcessorAccount());
        getConfirmationTaskVo.setProcessorName(task.getProcessorName());
        return getConfirmationTaskVo;
    }
}

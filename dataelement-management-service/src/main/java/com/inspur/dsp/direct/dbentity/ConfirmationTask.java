package com.inspur.dsp.direct.dbentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 确认任务表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "confirmation_task")
public class ConfirmationTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "task_id", type = IdType.INPUT)
    @Size(max = 36, message = "任务ID最大长度要小于 36")
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 基准数据元唯一标识
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36, message = "基准数据元唯一标识最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 发出日期
     */
    @TableField(value = "send_date")
    private Date sendDate;

    /**
     * 发出人用户账号
     */
    @TableField(value = "sender_account")
    @Size(max = 30, message = "发出人用户账号最大长度要小于 30")
    private String senderAccount;

    /**
     * 发出人姓名
     */
    @TableField(value = "sender_name")
    @Size(max = 30, message = "发出人姓名最大长度要小于 30")
    private String senderName;

    /**
     * 状态(待确认、确认、拒绝)
     */
    @TableField(value = "`status`")
    @Size(max = 20, message = "状态(待确认、确认、拒绝)最大长度要小于 20")
    private String status;

    /**
     * 任务处理单位统一社会信用代码
     */
    @TableField(value = "processing_unit_code")
    @Size(max = 18, message = "任务处理单位统一社会信用代码最大长度要小于 18")
    private String processingUnitCode;

    /**
     * 任务处理单位名称
     */
    @TableField(value = "processing_unit_name")
    @Size(max = 200, message = "任务处理单位名称最大长度要小于 200")
    private String processingUnitName;

    /**
     * 处理日期
     */
    @TableField(value = "processing_date")
    private Date processingDate;

    /**
     * 处理结果
     */
    @TableField(value = "processing_result")
    @Size(max = 20, message = "处理结果最大长度要小于 20")
    private String processingResult;

    /**
     * 处理意见
     */
    @TableField(value = "processing_opinion")
    @Size(max = 200, message = "处理意见最大长度要小于 200")
    private String processingOpinion;

    /**
     * 处理人用户账号
     */
    @TableField(value = "processor_account")
    @Size(max = 30, message = "处理人用户账号最大长度要小于 30")
    private String processorAccount;

    /**
     * 处理人姓名
     */
    @TableField(value = "processor_name")
    @Size(max = 30, message = "处理人姓名最大长度要小于 30")
    private String processorName;
}
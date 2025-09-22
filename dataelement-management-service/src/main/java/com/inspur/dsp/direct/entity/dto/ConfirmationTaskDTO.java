package com.inspur.dsp.direct.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 确认任务表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    private String baseName;
}
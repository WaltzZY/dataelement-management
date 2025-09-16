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
 * 定源事件记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "source_event_record")
public class SourceEventRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.INPUT)
    @Size(max = 36, message = "记录ID最大长度要小于 36")
    @NotBlank(message = "记录ID不能为空")
    private String recordId;

    /**
     * 基准数据元唯一标识
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36, message = "基准数据元唯一标识最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 事件类型(发起定源、确认成为数源单位、拒绝成为数源单位、核定数源单位、发起协商、结束协商)
     */
    @TableField(value = "event_type")
    @Size(max = 50, message = "事件类型(发起定源、确认成为数源单位、拒绝成为数源单位、核定数源单位、发起协商、结束协商)最大长度要小于 50")
    private String eventType;

    /**
     * 发出单位统一社会信用代码
     */
    @TableField(value = "send_unit_code")
    @Size(max = 18, message = "发出单位统一社会信用代码最大长度要小于 18")
    private String sendUnitCode;

    /**
     * 发出单位名称
     */
    @TableField(value = "send_unit_name")
    @Size(max = 200, message = "发出单位名称最大长度要小于 200")
    private String sendUnitName;

    /**
     * 事件源单位统一社会信用代码
     */
    @TableField(value = "event_source_unit_code")
    @Size(max = 18, message = "事件源单位统一社会信用代码最大长度要小于 18")
    private String eventSourceUnitCode;

    /**
     * 事件源单位名称
     */
    @TableField(value = "event_source_unit_name")
    @Size(max = 200, message = "事件源单位名称最大长度要小于 200")
    private String eventSourceUnitName;

    /**
     * 事件目标单位统一社会信用代码
     */
    @TableField(value = "event_target_unit_code")
    @Size(max = 18, message = "事件目标单位统一社会信用代码最大长度要小于 18")
    private String eventTargetUnitCode;

    /**
     * 事件目标单位名称
     */
    @TableField(value = "event_target_unit_name")
    @Size(max = 200, message = "事件目标单位名称最大长度要小于 200")
    private String eventTargetUnitName;

    /**
     * 事件描述
     */
    @TableField(value = "event_description")
    private String eventDescription;

    /**
     * 事件状态
     */
    @TableField(value = "event_status")
    @Size(max = 20, message = "事件状态最大长度要小于 20")
    private String eventStatus;

    /**
     * 事件时间
     */
    @TableField(value = "event_time")
    private Date eventTime;

    /**
     * 操作人账号
     */
    @TableField(value = "operator_account")
    @Size(max = 30, message = "操作人账号最大长度要小于 30")
    private String operatorAccount;

    /**
     * 操作人姓名
     */
    @TableField(value = "operator_name")
    @Size(max = 30, message = "操作人姓名最大长度要小于 30")
    private String operatorName;

    /**
     * 创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;
}
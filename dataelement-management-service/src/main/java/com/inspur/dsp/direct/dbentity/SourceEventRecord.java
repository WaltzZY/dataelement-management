package com.inspur.dsp.direct.dbentity;

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
     * 数据元的唯一标识
     */
    @TableField(value = "data_element_id")
    @Size(max = 36, message = "数据元的唯一标识最大长度要小于 36")
    private String dataElementId;

    /**
     * 数据元名称
     */
    @TableField(value = "data_element_name")
    @Size(max = 100, message = "数据元名称最大长度要小于 100")
    private String dataElementName;

    /**
     * 定源类型(确认型核定/认领型核定/协商结果录入/手动定源/导入定源)
     */
    @TableField(value = "source_type")
    @Size(max = 30, message = "定源类型(确认型核定/认领型核定/协商结果录入/手动定源/导入定源)最大长度要小于 30")
    private String sourceType;

    /**
     * 定源时间
     */
    @TableField(value = "source_date")
    private Date sourceDate;

    /**
     * 定源操作人账号
     */
    @TableField(value = "operator_account")
    @Size(max = 30, message = "定源操作人账号最大长度要小于 30")
    private String operatorAccount;

    /**
     * 定源联系人电话
     */
    @TableField(value = "contact_phone")
    @Size(max = 20, message = "定源联系人电话最大长度要小于 20")
    private String contactPhone;

    /**
     * 定源联系人姓名
     */
    @TableField(value = "contact_name")
    @Size(max = 30, message = "定源联系人姓名最大长度要小于 30")
    private String contactName;

    /**
     * 数源单位统一社会信用代码
     */
    @TableField(value = "source_unit_code")
    @Size(max = 30, message = "数源单位统一社会信用代码最大长度要小于 30")
    private String sourceUnitCode;

    /**
     * 数源单位名称
     */
    @TableField(value = "source_unit_name")
    @Size(max = 200, message = "数源单位名称最大长度要小于 200")
    private String sourceUnitName;

    /**
     * 确定数据单位的单位统一社会信用代码
     */
    @TableField(value = "send_unit_code")
    @Size(max = 30, message = "确定数据单位的单位统一社会信用代码最大长度要小于 30")
    private String sendUnitCode;

    /**
     * 确定数据单位的单位数源单位名称
     */
    @TableField(value = "send_unit_name")
    @Size(max = 200, message = "确定数据单位的单位数源单位名称最大长度要小于 200")
    private String sendUnitName;
}
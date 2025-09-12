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
 * 协商记录明细表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "negotiation_record_detail")
public class NegotiationRecordDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 协商参与者记录数据ID
     */
    @TableId(value = "record_detail_id", type = IdType.INPUT)
    @Size(max = 36,message = "协商参与者记录数据ID最大长度要小于 36")
    @NotBlank(message = "协商参与者记录数据ID不能为空")
    private String recordDetailId;

    /**
     * 协商记录数据ID
     */
    @TableField(value = "record_id")
    @Size(max = 36,message = "协商记录数据ID最大长度要小于 36")
    private String recordId;

    /**
     * 协商单位统一社会信用代码
     */
    @TableField(value = "negotiation_unit_code")
    @Size(max = 255,message = "协商单位统一社会信用代码最大长度要小于 255")
    private String negotiationUnitCode;

    /**
     * 协商单位名称
     */
    @TableField(value = "negotiation_unit_name")
    @Size(max = 255,message = "协商单位名称最大长度要小于 255")
    private String negotiationUnitName;

    /**
     * 协商结果
     */
    @TableField(value = "negotiation_result")
    private String negotiationResult;

    /**
     * 记录协商结果用户账号
     */
    @TableField(value = "recorder_account")
    @Size(max = 30,message = "记录协商结果用户账号最大长度要小于 30")
    private String recorderAccount;

    /**
     * 记录协商结果用户姓名
     */
    @TableField(value = "recorder_name")
    @Size(max = 30,message = "记录协商结果用户姓名最大长度要小于 30")
    private String recorderName;

    /**
     * 协商结果记录日期
     */
    @TableField(value = "result_date")
    private Date resultDate;
}
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
 * 协商记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "negotiation_record")
public class NegotiationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 协商记录数据ID
     */
    @TableId(value = "record_id", type = IdType.INPUT)
    @Size(max = 36, message = "协商记录数据ID最大长度要小于 36")
    @NotBlank(message = "协商记录数据ID不能为空")
    private String recordId;

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
     * 协商事宜说明
     */
    @TableField(value = "negotiation_content")
    private String negotiationContent;
}
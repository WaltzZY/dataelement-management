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
 * 流程记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process_record")
public class ProcessRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 行唯一标识
     */
    @TableId(value = "processid", type = IdType.INPUT)
    @Size(max = 36, message = "行唯一标识最大长度要小于 36")
    @NotBlank(message = "行唯一标识不能为空")
    private String processid;

    /**
     * 基准数据元id
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36, message = "基准数据元id最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 流程id
     */
    @TableField(value = "flowid")
    @Size(max = 36, message = "流程id最大长度要小于 36")
    private String flowid;

    /**
     * 处理人id
     */
    @TableField(value = "processuserid")
    @Size(max = 30, message = "处理人id最大长度要小于 30")
    private String processuserid;

    /**
     * 处理人姓名
     */
    @TableField(value = "processusername")
    @Size(max = 50, message = "处理人姓名最大长度要小于 50")
    private String processusername;

    /**
     * 处理人单位code
     */
    @TableField(value = "processunitcode")
    @Size(max = 30, message = "处理人单位code最大长度要小于 30")
    private String processunitcode;

    /**
     * 处理人单位名称
     */
    @TableField(value = "processunitname")
    @Size(max = 200, message = "处理人单位名称最大长度要小于 200")
    private String processunitname;

    /**
     * 用户操作
     */
    @TableField(value = "useroperation")
    @Size(max = 100, message = "用户操作最大长度要小于 100")
    private String useroperation;

    /**
     * 处理时间
     */
    @TableField(value = "processdatetime")
    private Date processdatetime;

    /**
     * 源环节
     */
    @TableField(value = "sourceactivityname")
    @Size(max = 100, message = "源环节最大长度要小于 100")
    private String sourceactivityname;

    /**
     * 目标环节
     */
    @TableField(value = "destactivityname")
    @Size(max = 100, message = "目标环节最大长度要小于 100")
    private String destactivityname;

    /**
     * 创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_account")
    @Size(max = 30, message = "创建人账号最大长度要小于 30")
    private String createAccount;

    /**
     * 最后修改日期
     */
    @TableField(value = "last_modify_date")
    private Date lastModifyDate;

    /**
     * 最后修改人
     */
    @TableField(value = "last_modify_account")
    @Size(max = 30, message = "最后修改人最大长度要小于 30")
    private String lastModifyAccount;

    /**
     * 用户建议、意见
     */
    @TableField(value = "usersuggestion")
    @Size(max = 500, message = "用户建议、意见最大长度要小于 500")
    private String usersuggestion;
}
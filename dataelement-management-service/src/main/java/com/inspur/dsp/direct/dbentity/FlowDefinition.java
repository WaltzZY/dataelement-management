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
 * 流程配置主表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "flow_definition")
public class FlowDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 行唯一标识
     */
    @TableId(value = "flowid", type = IdType.INPUT)
    @Size(max = 36,message = "行唯一标识最大长度要小于 36")
    @NotBlank(message = "行唯一标识不能为空")
    private String flowid;

    /**
     * 流程名称
     */
    @TableField(value = "flowname")
    @Size(max = 100,message = "流程名称最大长度要小于 100")
    private String flowname;

    /**
     * 流程描述
     */
    @TableField(value = "flowdescription")
    @Size(max = 300,message = "流程描述最大长度要小于 300")
    private String flowdescription;

    /**
     * 状态: 启用(Enable), 禁用(Disable)
     */
    @TableField(value = "`status`")
    @Size(max = 20,message = "状态: 启用(Enable), 禁用(Disable)最大长度要小于 20")
    private String status;

    /**
     * 创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_account")
    @Size(max = 30,message = "创建人账号最大长度要小于 30")
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
    @Size(max = 30,message = "最后修改人最大长度要小于 30")
    private String lastModifyAccount;
}
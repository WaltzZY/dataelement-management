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
 * 流程迁移配置表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "flow_transfer_definition")
public class FlowTransferDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 行唯一标识
     */
    @TableId(value = "transferid", type = IdType.INPUT)
    @Size(max = 36,message = "行唯一标识最大长度要小于 36")
    @NotBlank(message = "行唯一标识不能为空")
    private String transferid;

    /**
     * 迁移源环节名称
     */
    @TableField(value = "sourceactivityname")
    @Size(max = 100,message = "迁移源环节名称最大长度要小于 100")
    private String sourceactivityname;

    /**
     * 迁移条件
     */
    @TableField(value = "transfercondition")
    @Size(max = 100,message = "迁移条件最大长度要小于 100")
    private String transfercondition;

    /**
     * 迁移目标环节名称
     */
    @TableField(value = "destactivityname")
    @Size(max = 100,message = "迁移目标环节名称最大长度要小于 100")
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
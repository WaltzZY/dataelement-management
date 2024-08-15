package com.inspur.dsp.direct.dbentity.business;

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
 * 服务接口修改记录信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "service_interface_modify_log")
public class ServiceInterfaceModifyLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "id最大长度要小于 36")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 接口ID
     */
    @TableField(value = "interface_id")
    @Size(max = 36,message = "接口ID最大长度要小于 36")
    private String interfaceId;

    /**
     * 接口说明文本(接口说明文本，mk格式)
     */
    @TableField(value = "interface_explanation")
    private String interfaceExplanation;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_user_id")
    @Size(max = 30,message = "创建人账号最大长度要小于 30")
    private String createUserId;

    /**
     * 修改时间
     */
    @TableField(value = "modify_date")
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id")
    @Size(max = 30,message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}
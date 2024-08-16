package com.inspur.dsp.direct.dbentity.business;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 属性定义表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "attribute_definition")
public class AttributeDefinition implements Serializable {
    /**
     * 属性ID
     */
    @TableId(value = "attribute_id", type = IdType.INPUT)
    @Size(max = 36,message = "属性ID最大长度要小于 36")
    @NotBlank(message = "属性ID不能为空")
    private String attributeId;

    /**
     * 属性名称
     */
    @TableField(value = "attribute_name")
    @Size(max = 200,message = "属性名称最大长度要小于 200")
    private String attributeName;

    /**
     * 属性定义
     */
    @TableField(value = "attribute_definition")
    @Size(max = 2000,message = "属性定义最大长度要小于 2000")
    private String attributeDefinition;

    /**
     * 创建时间
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Size(max = 30,message = "创建人账号最大长度要小于 30")
    private String createUserId;

    /**
     * 修改时间
     */
    @TableField(value = "modify_date", fill = FieldFill.INSERT)
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id", fill = FieldFill.INSERT)
    @Size(max = 30,message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}
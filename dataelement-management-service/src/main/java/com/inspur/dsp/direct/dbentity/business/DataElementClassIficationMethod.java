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
 * 基准数据元分类方式表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_class_ification_method")
public class DataElementClassIficationMethod implements Serializable {
    /**
     * 分类方式代码
     */
    @TableId(value = "class_ification_method_code", type = IdType.INPUT)
    @Size(max = 10, message = "分类方式代码最大长度要小于 10")
    @NotBlank(message = "分类方式代码不能为空")
    private String classIficationMethodCode;

    /**
     * 分类方式名称
     */
    @TableField(value = "class_ification_method_name")
    @Size(max = 10, message = "分类方式名称最大长度要小于 10")
    private String classIficationMethodName;

    /**
     * 分类方式描述
     */
    @TableField(value = "class_ification_method_desc")
    @Size(max = 200, message = "分类方式描述最大长度要小于 200")
    private String classIficationMethodDesc;

    /**
     * 排序ID
     */
    @TableField(value = "sortid")
    private Integer sortid;

    /**
     * 创建时间
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Size(max = 30, message = "创建人账号最大长度要小于 30")
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
    @Size(max = 30, message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}
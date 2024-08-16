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
 * 基准数据元所属类别表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_belong_category")
public class DataElementBelongCategory implements Serializable {
    /**
     * 数据元所属分类ID
     */
    @TableId(value = "belongrel_id", type = IdType.INPUT)
    @Size(max = 36, message = "数据元所属分类ID最大长度要小于 36")
    @NotBlank(message = "数据元所属分类ID不能为空")
    private String belongrelId;

    /**
     * 数据元ID(对应与
     DataElement_Base的DataElementid)
     */
    @TableField(value = "data_element_id")
    @Size(max = 36, message = "数据元ID(对应与DataElement_Base的DataElementid)最大长度要小于 36")
    private String dataElementId;

    /**
     * 分类代码(对应于表dataelement_category的)
     */
    @TableField(value = "data_element_category_code")
    @Size(max = 10, message = "分类代码(对应于表dataelement_category的)最大长度要小于 10")
    private String dataElementCategoryCode;

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
    @TableField(value = "modify_date", fill = FieldFill.UPDATE)
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id", fill = FieldFill.UPDATE)
    @Size(max = 30, message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}
package com.inspur.dsp.direct.dbentity.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基准数据元分类代码表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_category")
public class DataElementCategory implements Serializable {
    /**
     * 类目唯一标识
     */
    @TableId(value = "data_element_category_id", type = IdType.INPUT)
    @Size(max = 36,message = "类目唯一标识最大长度要小于 36")
    @NotBlank(message = "类目唯一标识不能为空")
    private String dataElementCategoryId;

    /**
     * 分类代码
     */
    @TableField(value = "data_element_category_code")
    @Size(max = 10,message = "分类代码最大长度要小于 10")
    private String dataElementCategoryCode;

    /**
     * 父级分类代码
     */
    @TableField(value = "parent_category_code")
    @Size(max = 10,message = "父级分类代码最大长度要小于 10")
    private String parentCategoryCode;

    /**
     * 分类方式代码(分类方式)
     */
    @TableField(value = "data_element_class_ification_method_code")
    @Size(max = 10,message = "分类方式代码(分类方式)最大长度要小于 10")
    private String dataElementClassIficationMethodCode;

    /**
     * 类目（分类名称）
     */
    @TableField(value = "data_element_category_name")
    @Size(max = 100,message = "类目（分类名称）最大长度要小于 100")
    private String dataElementCategoryName;

    /**
     * 排序ID
     */
    @TableField(value = "sortid")
    private Integer sortid;

    private static final long serialVersionUID = 1L;
}
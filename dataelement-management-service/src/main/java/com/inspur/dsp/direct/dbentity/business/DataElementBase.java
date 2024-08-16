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
 * 基准数据元表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_base")
public class DataElementBase implements Serializable {
    /**
     * 数据元ID(数据元在数据表中的唯一标识)
     */
    @TableId(value = "data_element_id", type = IdType.INPUT)
    @Size(max = 36,message = "数据元ID(数据元在数据表中的唯一标识)最大长度要小于 36")
    @NotBlank(message = "数据元ID(数据元在数据表中的唯一标识)不能为空")
    private String dataElementId;

    /**
     * 数据元标识符(数据元唯一标识符，参照数据元标识符命名规则。)
     */
    @TableField(value = "data_element_code")
    @Size(max = 10,message = "数据元标识符(数据元唯一标识符，参照数据元标识符命名规则。)最大长度要小于 10")
    private String dataElementCode;

    /**
     * 中文名称
     */
    @TableField(value = "data_element_name")
    @Size(max = 200,message = "中文名称最大长度要小于 200")
    private String dataElementName;

    /**
     * 英文名称
     */
    @TableField(value = "data_element_en_name")
    @Size(max = 200,message = "英文名称最大长度要小于 200")
    private String dataElementEnName;

    /**
     * 定义
     */
    @TableField(value = "data_element_definition")
    @Size(max = 2000,message = "定义最大长度要小于 2000")
    private String dataElementDefinition;

    /**
     * 数据类型
     */
    @TableField(value = "data_element_data_type")
    @Size(max = 20,message = "数据类型最大长度要小于 20")
    private String dataElementDataType;

    /**
     * 数据格式
     */
    @TableField(value = "data_element_data_format")
    @Size(max = 30,message = "数据格式最大长度要小于 30")
    private String dataElementDataFormat;

    /**
     * 值域范围代码(对于有限定值域范围的，是值域代码)
     */
    @TableField(value = "data_element_valuerange_code")
    @Size(max = 50,message = "值域范围代码(对于有限定值域范围的，是值域代码)最大长度要小于 50")
    private String dataElementValuerangeCode;

    /**
     * 值域描述
     */
    @TableField(value = "data_element_valuerange_desc")
    @Size(max = 300,message = "值域描述最大长度要小于 300")
    private String dataElementValuerangeDesc;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Size(max = 2000,message = "备注最大长度要小于 2000")
    private String remark;

    /**
     * 数源单位
     */
    @TableField(value = "data_source_org_code")
    @Size(max = 18,message = "数源单位最大长度要小于 18")
    private String dataSourceOrgCode;

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

    /**
     * 数据元状态:01 待定源，02 待定标，03 已发布，04 已废弃
     */
    @TableField(value = "data_element_status")
    @Size(max = 2,message = "数据元状态:01 待定源，02 待定标，03 已发布，04 已废弃最大长度要小于 2")
    private String dataElementStatus;

    private static final long serialVersionUID = 1L;
}
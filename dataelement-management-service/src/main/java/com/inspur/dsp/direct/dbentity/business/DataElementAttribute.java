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
 * 基准数据元属性集表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_attribute")
public class DataElementAttribute implements Serializable {
    /**
     * 唯一标识符
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "唯一标识符最大长度要小于 36")
    @NotBlank(message = "唯一标识符不能为空")
    private String id;

    /**
     * 数据元id
     */
    @TableField(value = "data_element_id")
    @Size(max = 36,message = "数据元id最大长度要小于 36")
    private String dataElementId;

    /**
     * 属性id
     */
    @TableField(value = "data_element_attribute_id")
    @Size(max = 36,message = "属性id最大长度要小于 36")
    private String dataElementAttributeId;

    /**
     * 属性值
     */
    @TableField(value = "data_element_attribute_value")
    @Size(max = 2000,message = "属性值最大长度要小于 2000")
    private String dataElementAttributeValue;

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
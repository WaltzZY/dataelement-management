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
 * 数据元和数据资源数据项关联表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_data_item")
public class DataElementDataItem implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "id最大长度要小于 36")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 中文名称
     */
    @TableField(value = "data_element_name")
    @Size(max = 200,message = "中文名称最大长度要小于 200")
    private String dataElementName;

    /**
     * 数据资源唯一标识符
     */
    @TableField(value = "data_resource_id")
    @Size(max = 50,message = "数据资源唯一标识符最大长度要小于 50")
    private String dataResourceId;

    /**
     * 数据资源唯名称
     */
    @TableField(value = "data_resource_name")
    @Size(max = 300,message = "数据资源唯名称最大长度要小于 50")
    private String dataResourceName;

    /**
     * 数据资源中数据项名称
     */
    @TableField(value = "data_resource_item_name")
    @Size(max = 200,message = "数据资源中数据项名称最大长度要小于 200")
    private String dataResourceItemName;

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
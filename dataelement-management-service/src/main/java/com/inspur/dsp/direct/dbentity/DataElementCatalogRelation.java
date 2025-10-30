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
 * 数据元-目录关联关系表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_catalog_relation")
public class DataElementCatalogRelation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 关联关系数据行ID
     */
    @TableId(value = "dataid", type = IdType.INPUT)
    @Size(max = 36, message = "关联关系数据行ID最大长度要小于 36")
    @NotBlank(message = "关联关系数据行ID不能为空")
    private String dataid;

    /**
     * 数据元ID
     */
    @TableField(value = "data_element_id")
    @Size(max = 36, message = "数据元ID最大长度要小于 36")
    private String dataElementId;

    /**
     * 目录的唯一标识
     */
    @TableField(value = "catalog_id")
    @Size(max = 50, message = "目录的唯一标识最大长度要小于 50")
    private String catalogId;

    /**
     * 目录的名称
     */
    @TableField(value = "`catalog_name`")
    @Size(max = 200, message = "目录的名称最大长度要小于 200")
    private String catalogName;

    /**
     * 目录描述（摘要）
     */
    @TableField(value = "catalog_desc")
    @Size(max = 500, message = "目录描述（摘要）最大长度要小于 500")
    private String catalogDesc;

    /**
     * 信息项ID
     */
    @TableField(value = "info_item_id")
    @Size(max = 50, message = "信息项ID最大长度要小于 50")
    private String infoItemId;

    /**
     * 信息项名称
     */
    @TableField(value = "info_item_name")
    @Size(max = 100, message = "信息项名称最大长度要小于 100")
    private String infoItemName;

    /**
     * 信息项类型
     */
    @TableField(value = "Info_item_datatype")
    @Size(max = 20, message = "信息项类型最大长度要小于 20")
    private String infoItemDatatype;

    /**
     * 目录单位统一社会信用代码
     */
    @TableField(value = "catalog_unit_code")
    @Size(max = 50, message = "目录单位统一社会信用代码最大长度要小于 50")
    private String catalogUnitCode;

    /**
     * 目录单位名称
     */
    @TableField(value = "catalog_unit_name")
    @Size(max = 200, message = "目录单位名称最大长度要小于 200")
    private String catalogUnitName;

    /**
     * 创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_account")
    @Size(max = 30, message = "创建人账号最大长度要小于 30")
    private String createAccount;

    /**
     * 最后修改日期
     */
    @TableField(value = "last_modify_date")
    private Date lastModifyDate;

    /**
     * 最后修改人账号
     */
    @TableField(value = "last_modify_account")
    @Size(max = 30, message = "最后修改人账号最大长度要小于 30")
    private String lastModifyAccount;
}
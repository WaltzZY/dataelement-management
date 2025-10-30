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
 * 单位/区划表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "organization_unit")
public class OrganizationUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据行ID
     */
    @TableId(value = "dataid", type = IdType.INPUT)
    @Size(max = 36, message = "数据行ID最大长度要小于 36")
    @NotBlank(message = "数据行ID不能为空")
    private String dataid;

    /**
     * 第三方ID
     */
    @TableField(value = "third_party_id")
    @Size(max = 50, message = "第三方ID最大长度要小于 50")
    private String thirdPartyId;

    /**
     * 节点类型(UNIT/REGION)
     */
    @TableField(value = "node_type")
    @Size(max = 10, message = "节点类型(UNIT/REGION)最大长度要小于 10")
    private String nodeType;

    /**
     * 统一社会信用代码/区划代码
     */
    @TableField(value = "unit_code")
    @Size(max = 30, message = "统一社会信用代码/区划代码最大长度要小于 30")
    private String unitCode;

    /**
     * 单位名称/区划名称
     */
    @TableField(value = "unit_name")
    @Size(max = 200, message = "单位名称/区划名称最大长度要小于 200")
    private String unitName;

    /**
     * 区划的所属区划
     */
    @TableField(value = "unit_region")
    @Size(max = 200, message = "区划的所属区划最大长度要小于 200")
    private String unitRegion;

    /**
     * 父节点ID
     */
    @TableField(value = "parent_node_id")
    @Size(max = 36, message = "父节点ID最大长度要小于 36")
    private String parentNodeId;

    /**
     * 上级行业主管单位ID
     */
    @TableField(value = "superior_unit_id")
    @Size(max = 36, message = "上级行业主管单位ID最大长度要小于 36")
    private String superiorUnitId;

    /**
     * 单位定源业务联系人账号
     */
    @TableField(value = "contact_account")
    @Size(max = 30, message = "单位定源业务联系人账号最大长度要小于 30")
    private String contactAccount;

    /**
     * 单位定源业务联系人姓名
     */
    @TableField(value = "contact_name")
    @Size(max = 30, message = "单位定源业务联系人姓名最大长度要小于 30")
    private String contactName;

    /**
     * 单位定源业务联系电话
     */
    @TableField(value = "contact_phone")
    @Size(max = 20, message = "单位定源业务联系电话最大长度要小于 20")
    private String contactPhone;

    /**
     * 创建时间
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
     * 最后修改时间
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
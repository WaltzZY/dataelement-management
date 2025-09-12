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
 * 定源需求申请单—领域数据元关系表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "source_request_detail")
public class SourceRequestDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据行唯一标识
     */
    @TableId(value = "dataid", type = IdType.INPUT)
    @Size(max = 36, message = "数据行唯一标识最大长度要小于 36")
    @NotBlank(message = "数据行唯一标识不能为空")
    private String dataid;

    /**
     * 申请单唯一标识
     */
    @TableField(value = "request_id")
    @Size(max = 36, message = "申请单唯一标识最大长度要小于 36")
    private String requestId;

    /**
     * 数据元名称
     */
    @TableField(value = "`name`")
    @Size(max = 50, message = "数据元名称最大长度要小于 50")
    private String name;

    /**
     * 数据元状态
     */
    @TableField(value = "`status`")
    @Size(max = 20, message = "数据元状态最大长度要小于 20")
    private String status;

    /**
     * 数据元定义描述
     */
    @TableField(value = "`definition`")
    private String definition;

    /**
     * 数据类型
     */
    @TableField(value = "datatype")
    @Size(max = 20, message = "数据类型最大长度要小于 20")
    private String datatype;

    /**
     * 数据格式
     */
    @TableField(value = "data_format")
    @Size(max = 50, message = "数据格式最大长度要小于 50")
    private String dataFormat;

    /**
     * 值域
     */
    @TableField(value = "value_domain")
    private String valueDomain;

    /**
     * 关联的基准数据元唯一标识
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36, message = "关联的基准数据元唯一标识最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 关联的基准数据元名称
     */
    @TableField(value = "base_dataelement_name")
    @Size(max = 50, message = "关联的基准数据元名称最大长度要小于 50")
    private String baseDataelementName;

    /**
     * 提供单位统一社会信用代码
     */
    @TableField(value = "provider_unit_code")
    @Size(max = 18, message = "提供单位统一社会信用代码最大长度要小于 18")
    private String providerUnitCode;

    /**
     * 提供单位名称
     */
    @TableField(value = "provider_unit_name")
    @Size(max = 200, message = "提供单位名称最大长度要小于 200")
    private String providerUnitName;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    private String remarks;

    /**
     * 数据元创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 数据元创建人账号
     */
    @TableField(value = "create_account")
    @Size(max = 30, message = "数据元创建人账号最大长度要小于 30")
    private String createAccount;

    /**
     * 数据元最后修改日期
     */
    @TableField(value = "last_modify_date")
    private Date lastModifyDate;

    /**
     * 数据元最后修改人账号
     */
    @TableField(value = "last_modify_account")
    @Size(max = 30, message = "数据元最后修改人账号最大长度要小于 30")
    private String lastModifyAccount;
}
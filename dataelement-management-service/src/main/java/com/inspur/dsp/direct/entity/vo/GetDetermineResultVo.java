package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 基准数据元表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "base_data_element")
public class GetDetermineResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据行唯一标识
     */
    @TableId(value = "dataid", type = IdType.INPUT)
    @Size(max = 36, message = "数据行唯一标识最大长度要小于 36")
    @NotBlank(message = "数据行唯一标识不能为空")
    private String dataid;

    /**
     * 数据元编码
     */
    @TableField(value = "data_element_id")
    @Size(max = 16, message = "数据元编码最大长度要小于 16")
    private String dataElementId;

    /**
     * 数据元状态
     */
    @TableField(value = "`status`")
    @Size(max = 20, message = "数据元状态最大长度要小于 20")
    private String status;

    /**
     * 数据元名称
     */
    @TableField(value = "`name`")
    @Size(max = 50, message = "数据元名称最大长度要小于 50")
    private String name;

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
     * 数源单位统一社会信用代码
     */
    @TableField(value = "source_unit_code")
    @Size(max = 18, message = "数源单位统一社会信用代码最大长度要小于 18")
    private String sourceUnitCode;

    /**
     * 数源单位名称
     */
    @TableField(value = "source_unit_name")
    @Size(max = 200, message = "数源单位名称最大长度要小于 200")
    private String sourceUnitName;

    /**
     * 数据元发布日期
     */
    @TableField(value = "publish_date")
    private Date publishDate;

    /**
     * 发起定源时间
     */
    @TableField(value = "send_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /**
     * 确定数源单位时间
     */
    @TableField(value = "confirm_date")
    private Date confirmDate;

    /**
     * 采集单位数量
     */
    @TableField(value = "collectunitqty")
    private Integer collectunitqty;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    private String remarks;

    /**
     * 创建日期
     */
    @TableField(value = "create_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifyDate;

    /**
     * 最后修改人账号
     */
    @TableField(value = "last_modify_account")
    @Size(max = 30, message = "最后修改人账号最大长度要小于 30")
    private String lastModifyAccount;

    /**
     * 最后修改人账号
     */
    @TableField(exist = false)
    private SourceEventRecord sourceEventRecord;

    /**
     * 最后修改人账号
     */
    @TableField(exist = false)
    private String statusChinese;

    /**
     * 最后修改人账号
     */
    @TableField(exist = false)
    private String taskStatus;

}
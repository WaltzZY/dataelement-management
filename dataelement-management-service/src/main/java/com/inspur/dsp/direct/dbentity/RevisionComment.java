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
 * 修订意见记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "revision_comment")
public class RevisionComment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 修订意见唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "修订意见唯一标识最大长度要小于 36")
    @NotBlank(message = "修订意见唯一标识不能为空")
    private String id;

    /**
     * 基准数据元id
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36,message = "基准数据元id最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 修订意见
     */
    @TableField(value = "`comment`")
    private String comment;

    /**
     * 修订发起人账号
     */
    @TableField(value = "revision_initiator_account")
    @Size(max = 30,message = "修订发起人账号最大长度要小于 30")
    private String revisionInitiatorAccount;

    /**
     * 修订发起人姓名
     */
    @TableField(value = "revision_initiator_name")
    @Size(max = 30,message = "修订发起人姓名最大长度要小于 30")
    private String revisionInitiatorName;

    /**
     * 修订发起时间
     */
    @TableField(value = "revision_createdate")
    private Date revisionCreatedate;

    /**
     * 修订发起人电话
     */
    @TableField(value = "revision_initiator_tel")
    @Size(max = 30,message = "修订发起人电话最大长度要小于 30")
    private String revisionInitiatorTel;

    /**
     * 申请单创建日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 申请单创建人账号
     */
    @TableField(value = "create_account")
    @Size(max = 30,message = "申请单创建人账号最大长度要小于 30")
    private String createAccount;

    /**
     * 申请单最后修改日期
     */
    @TableField(value = "last_modify_date")
    private Date lastModifyDate;

    /**
     * 申请单最后修改人
     */
    @TableField(value = "last_modify_account")
    @Size(max = 30,message = "申请单最后修改人最大长度要小于 30")
    private String lastModifyAccount;
}
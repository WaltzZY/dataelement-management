package com.inspur.dsp.direct.dbentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定源需求申请单主表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "source_request_main")
public class SourceRequestMain {
    /**
     * 申请单唯一标识
     */
    @TableId(value = "request_id", type = IdType.INPUT)
    @Size(max = 36,message = "申请单唯一标识最大长度要小于 36")
    @NotBlank(message = "申请单唯一标识不能为空")
    private String requestId;

    /**
     * 申请单标题
     */
    @TableField(value = "title")
    @Size(max = 200,message = "申请单标题最大长度要小于 200")
    private String title;

    /**
     * 申请单描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 申请单状态
     */
    @TableField(value = "`status`")
    @Size(max = 20,message = "申请单状态最大长度要小于 20")
    private String status;

    /**
     * 申请单提出部门统一社会信用代码
     */
    @TableField(value = "department_code")
    @Size(max = 18,message = "申请单提出部门统一社会信用代码最大长度要小于 18")
    private String departmentCode;

    /**
     * 申请单提出部门名称
     */
    @TableField(value = "department_name")
    @Size(max = 200,message = "申请单提出部门名称最大长度要小于 200")
    private String departmentName;

    /**
     * 申请单提出人账号
     */
    @TableField(value = "requester_account")
    @Size(max = 30,message = "申请单提出人账号最大长度要小于 30")
    private String requesterAccount;

    /**
     * 申请单提出人姓名
     */
    @TableField(value = "requester_name")
    @Size(max = 30,message = "申请单提出人姓名最大长度要小于 30")
    private String requesterName;

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
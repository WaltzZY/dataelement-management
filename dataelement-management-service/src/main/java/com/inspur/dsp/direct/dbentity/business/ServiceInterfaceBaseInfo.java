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
 * 服务接口基本信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "service_interface_base_info")
public class ServiceInterfaceBaseInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "id最大长度要小于 36")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 接口名称(接口的英文名称)
     */
    @TableField(value = "interface_name")
    @Size(max = 300,message = "接口名称(接口的英文名称)最大长度要小于 300")
    private String interfaceName;

    /**
     * 接口标题(接口的标题、中文名称)
     */
    @TableField(value = "interface_title")
    @Size(max = 300,message = "接口标题(接口的标题、中文名称)最大长度要小于 300")
    private String interfaceTitle;

    /**
     * 接口地址(接口调用地址)
     */
    @TableField(value = "interface_address")
    @Size(max = 300,message = "接口地址(接口调用地址)最大长度要小于 300")
    private String interfaceAddress;

    /**
     * 接口状态:1.草稿,2.待审核,3.正常服务,4已撤销
     */
    @TableField(value = "interface_status")
    @Size(max = 10,message = "接口状态:1.草稿,2.待审核,3.正常服务,4已撤销最大长度要小于 10")
    private String interfaceStatus;

    /**
     * 接口说明文本
     */
    @TableField(value = "interface_explanation")
    private String interfaceExplanation;

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

    /**
     * 接口所对应的数据资源系统中的资源ID
     */
    @TableField(value = "interface_res_id")
    @Size(max = 36,message = "接口所对应的数据资源系统中的资源ID最大长度要小于 36")
    private String interfaceResId;

    private static final long serialVersionUID = 1L;
}
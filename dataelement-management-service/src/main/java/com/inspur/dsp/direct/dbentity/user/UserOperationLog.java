package com.inspur.dsp.direct.dbentity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户操作日志表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_operation_log")
public class UserOperationLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日志唯一标识
     */
    @TableId(value = "log_id", type = IdType.INPUT)
    @Size(max = 36, message = "日志唯一标识最大长度要小于 36")
    @NotBlank(message = "日志唯一标识不能为空")
    private String logId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @Size(max = 50, message = "用户ID最大长度要小于 50")
    private String userId;

    /**
     * 用户名称
     */
    @TableField(value = "user_name")
    @Size(max = 32, message = "用户名称最大长度要小于 32")
    private String userName;

    /**
     * 操作类型，如登录、登出、数据修改、下载、访问页面等
     */
    @TableField(value = "operation_type")
    @Size(max = 50, message = "操作类型，如登录、登出、数据修改、下载、访问页面等最大长度要小于 50")
    private String operationType;

    /**
     * 用户来源IP
     */
    @TableField(value = "client_ip")
    @Size(max = 32, message = "用户来源IP最大长度要小于 32")
    private String clientIp;

    /**
     * 客户端浏览器
     */
    @TableField(value = "client_browser")
    @Size(max = 100, message = "客户端浏览器最大长度要小于 100")
    private String clientBrowser;

    /**
     * 客户端操作系统
     */
    @TableField(value = "client_system")
    @Size(max = 100, message = "客户端操作系统最大长度要小于 100")
    private String clientSystem;

    /**
     * 页面地址
     */
    @TableField(value = "page_url")
    @Size(max = 255, message = "页面地址最大长度要小于 255")
    private String pageUrl;

    /**
     * 页面名称
     */
    @TableField(value = "page_title")
    @Size(max = 255, message = "页面名称最大长度要小于 255")
    private String pageTitle;

    /**
     * 对象数据:操作对象数据
     */
    @TableField(value = "obj_data")
    private String objData;

    /**
     * 操作结果状态:success、fail
     */
    @TableField(value = "result_status")
    @Size(max = 7, message = "操作结果状态:success、fail最大长度要小于 7")
    private String resultStatus;

    /**
     * 异常信息:在发生异常时记录异常的详细信息
     */
    @TableField(value = "exception_info")
    private String exceptionInfo;

    /**
     * 日期生成时间
     */
    @TableField(value = "create_time")
    @NotNull(message = "日期生成时间不能为null")
    private Date createTime;
}
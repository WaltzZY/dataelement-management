package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum RespCode {
    /**
     * 请求成功,响应返回
     */
    OK("200", "请求成功,响应返回"),
    CREATED("201", "创建资源"),
    NO_CONTENT("204", "更新成功,客户端已经最新"),
    MOVED("301", "资源移动,用新返回的URI之一请求"),
    FOUND("302", "资源临时从其他URI请求,保持原有请求地址"),
    SEE_OTHER("303", "当前请求相应可在另一个URI上找到,须GET访问"),
    NOT_MODIFIED("304","GET请求被允许,但没有影响到资源"),
    /**
     * 请求体错误,返回错误描述
     */
    BAD_REQUEST("400", "访问失败"),
    /**
     * 适用于未登录检查
     */
    UNAUTHORIZED("401", "需验证用户身份"),
    /**
     * 适用于服务到期（比如付费的增值服务等） 因为某些原因不允许访问（比如被 ban ） 权限不够
     */
    FORBIDDEN("403", "服务器拒绝执行"),
    /**
     * 需要修改的资源不存在
     */
    NOT_FOUND("404", "找不到目标资源"),
    METHOD_NOT_ALLOWED("405", "不允许执行目标方法"),
    NOT_ACCEPTABLE("406", "服务器不支持客户端请求内容格式"),
    CONFLICT("409", "请求操作和资源的当前状态存在冲突"),
    GONE("410", "被请求资源已被删除"),
    /**
     * 主要使用场景在于实现并发控制
     */
    PRECONDITION_FAILED("412", "请求头字段验证失败"),
    REQUEST_ENTITY_TOO_LARGE("413", "POST请求实体过大"),
    UNSUPPORTED_MEDIA_TYPE("415", "服务器不支持请求中提交的数据的格式"),
    /**
     * 适用于发送了非法的资源
     */
    UNPROCESSABLE_ENTITY("422", "格式正确,语义错误"),
    PRECONDITION_REQUIRED("428", "缺少了必要的头信息"),
    INTERNAL_SERVER_ERROR("500", "服务出现意外情况,无法响应请求"),
    NOT_IMPLEMENTED("501", "服务器未支持该功能"),
    BAD_GATEWAY("502", "上游服务器无响应"),
    SERVICE_UNAVAILABLE("503", "服务器维护中"),
    SERVICE_NONSUPPORT("505", "服务器不支持"),

    /**
     * 600以后为自定义
     */
    SQL_FAIL("600", "数据库插入失败");

    private final String state;
    private final String stateInfo;

    RespCode(String state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static RespCode stateOf(int index) {
        for (RespCode state: values()) {
            if (state.getState().equals(String.valueOf(index))) {
                return state;
            }
        }
        return null;
    }
}

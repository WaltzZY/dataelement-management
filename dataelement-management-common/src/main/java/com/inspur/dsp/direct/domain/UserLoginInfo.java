package com.inspur.dsp.direct.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 用户登录信息
 * 忽略未知字段
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginInfo {

    @JsonProperty("IDENTITY_NUM")
    private String identityNum;

    /**
     * 账户
     */
    @JsonProperty("ACCOUNT")
    private String account;
    /**
     * 生日
     */
    @JsonProperty("BIRTHDAY")
    private long birthday;
    /**
     *
     */
    @JsonProperty("CODE")
    private String code;
    /**
     * 邮箱
     */
    @JsonProperty("EMAIL")
    private String email;
    /**
     * 性别编码
     */
    @JsonProperty("GENDER")
    private String gender;
    /**
     * id
     */
    @JsonProperty("ID")
    private String id;
    /**
     * 是否管理员
     */
    @JsonProperty("IS_ADMIN")
    private long isAdmin;
    /**
     * 最后登录时间
     */
    @JsonProperty("LAST_LOGIN_TIME")
    private String lastLogintime;
    /**
     * 用户在各应用系统的角色, key 应用系统code, value 应用系统角色code, 逗号拼接
     */
    @JsonProperty("LOGIN_ROLE_VALUE")
    private Map<String, String> loginRoleValue;
    /**
     * 手机
     */
    @JsonProperty("MOBILE")
    private String mobile;
    /**
     * 姓名
     */
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("PASSWORD")
    private String password;
    /**
     * 组织机构编码
     */
    @JsonProperty("ORG_CODE")
    private String orgCode;
    /**
     * 组织机构名称
     */
    @JsonProperty("ORG_NAME")
    private String orgName;
    /**
     * 组织机构简称
     */
    @JsonProperty("ORG_SHORT_CODE")
    private String orgShortCode;
    /**
     * 手机号
     */
    @JsonProperty("PHONE")
    private String phone;
    /**
     * 地区编码
     */
    @JsonProperty("REGION_CODE")
    private String regionCode;
    /**
     * 地区名称
     */
    @JsonProperty("REGION_NAME")
    private String regionName;
    /**
     * 角色id字符串,逗号拼接
     */
    @JsonProperty("ROLE_CODE")
    private String roleCode;
    /**
     * 用户在当前系统下角色code,逗号拼接
     */
    @JsonProperty("ROLE_VALUE")
    private String roleValue;
    /**
     * 角色权重
     */
    @JsonProperty("ROLE_WEIGHT")
    private String roleWeight;
    /**
     * 角色类型code
     */
    @JsonProperty("TYPE_CODE")
    private String typeCode;
}

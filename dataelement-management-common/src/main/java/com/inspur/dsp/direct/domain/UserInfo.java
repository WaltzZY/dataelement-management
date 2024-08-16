package com.inspur.dsp.direct.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    /**
     * 登录账户
     */
    private String ACCOUNT;
    /**
     * 生日
     */
    private Date BIRTHDAY;
    /**
     * 编码
     */
    private String CODE;
    /**
     * 邮箱
     */
    private String EMAIL;
    /**
     * 性别
     */
    private String GENDER;
    /**
     * 主键
     */
    private String ID;
    /**
     * 是否管理员
     */
    private String IS_ADMIN;
    /**
     * 最后登录时间
     */
    private String LAST_LOGIN_TIME;
    /**
     * 手机
     */
    private String MOBILE;
    /**
     * 姓名
     */
    private String NAME;
    /**
     * 机构编码
     */
    private String ORG_CODE;
    /**
     * 机构名称
     */
    private String ORG_NAME;
    /**
     * 机构简称
     */
    private String ORG_SHORT_CODE;
    /**
     * 密码
     */
    private String PASSWORD;
    /**
     * 电话
     */
    private String PHONE;
    /**
     * 区域编码
     */
    private String REGION_CODE;
    /**
     * 区域名称
     */
    private String REGION_NAME;
    /**
     * 角色编码
     */
    private String ROLE_CODE;
    /**
     * 角色名称
     */
    private String ROLE_VALUE;
    /**
     * 权重
     */
    private String ROLE_WEIGHT;

    private String TYPE_CODE;

    /**
     * 各系统下角色code
     */
    private List<LoginRoleValue> LOGIN_ROLE_VALUE;
}

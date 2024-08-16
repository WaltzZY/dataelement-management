package com.inspur.dsp.direct.domain;

import lombok.Data;

@Data
public class LoginRoleValue {

    /**
     * 系统名称
     */
    private String dspSystemName;

    /**
     * 角色编码, 逗号分割
     */
    private String roleCodes;
}

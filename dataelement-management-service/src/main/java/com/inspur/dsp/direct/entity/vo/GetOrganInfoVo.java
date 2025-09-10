package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

@Data
public class GetOrganInfoVo {

    /**
     * 组织机构id
     */
    private String id;
    /**
     * 组织机构编码
     */
    private String code;
    /**
     * 组织机构名称
     */
    private String name;
    /**
     * 组织机构负责人
     */
    private String leader;
    /**
     * 组织机构负责人联系电话
     */
    private String leaderPhone;
    /**
     * 路径
     */
    private String namePath;
}

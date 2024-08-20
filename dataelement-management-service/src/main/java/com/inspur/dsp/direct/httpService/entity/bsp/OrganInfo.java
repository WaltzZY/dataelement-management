package com.inspur.dsp.direct.httpService.entity.bsp;

import lombok.Data;

@Data
public class OrganInfo {

    private String REGION_TYPE;
    /**
     * 组织机构编码
     */
    private String CODE;
    private String PARENT_CODE;
    private String PARENT_NAME;
    private int SORT_ORDER;
    private String GRADE;
    private int CHILDS;
    private String SHORT_CODE;
    private String LAST_TIME;
    /**
     * 组织机构类型，0=部门
     */
    private String TYPE;
    /**
     * 组织机构名称
     */
    private String NAME;
}

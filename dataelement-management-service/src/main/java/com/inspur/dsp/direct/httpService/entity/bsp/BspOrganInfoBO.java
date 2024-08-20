package com.inspur.dsp.direct.httpService.entity.bsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取国家部委接口实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BspOrganInfoBO {


    private String ORG_ID;
    private String ORGAN_LEVEL;
    private String REGION_CODE;
    private String SHORT_CODE;
    /**
     *  组织机构简称
     */
    private String SHORT_NAME;
    /**
     * 组织机构名称
     */
    private String NAME;
    /**
     * 组织机构编码
     */
    private String CODE;
    private String ORGAN_TYPE;
    /**
     * 所属部门的上级部门编码
     */
    private String PARENT_CODE;
    private String REGION_NAME;
    /**
     * 部门全称名称
     */
    private String ORGAN_LINE;
    private Integer SORT_ORDER;
    private Integer CHILDS;
    private String ID;
    /**
     * 组织机构类型，0=部门
     */
    private String TYPE;
    private String REMARK;
}

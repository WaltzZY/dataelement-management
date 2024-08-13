package com.inspur.dsp.direct.entity.bo;

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
public class BspOraanInfoBO {


    private String ORG_ID;
    private String ORGAN_LEVEL;
    private String REGION_CODE;
    private String SHORT_CODE;
    private String SHORT_NAME;
    /**
     * 部门简称名称
     */
    private String NAME;
    /**
     * 部门编码
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
    private String SORT_ORDER;
    private String CHILDS;
    private String ID;
    private String TYPE;
}

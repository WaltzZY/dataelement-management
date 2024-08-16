package com.inspur.dsp.direct.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据字典详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictInfoBO {

    private String CODE;
    private String PARENT_CODE;
    private String KIND;
    private int SORT_ORDER;
    private String ID;
    private String CREATE_TIME;
    private String TYPE;
    private String REMARK;
    private String NAME;
}
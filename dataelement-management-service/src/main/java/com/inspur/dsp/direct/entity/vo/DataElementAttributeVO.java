package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataElementAttributeVO {
    /**
     * 属性名称
     */
    private String attributeName;
    /**
     * 属性值
     */
    private String dataElementAttributeValue;
    /**
     * 属性ID
     */
    private String attributeId;
}

package com.inspur.dsp.direct.entity.bo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataElementCategoryInfoBO {

    /**
     * 数据元ID
     */
    private String dataElementId;
    /**
     * 数据元分类编码
     */
    private String dataElementCategoryCode;
    /**
     * 数据元分类名称
     */
    private String dataElementCategoryName;
}

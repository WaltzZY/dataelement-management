package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryVO {
    /**
     * 分类名称
     */
    private String dataElementCategoryName;
    /**
     * 分类描述
     */
    private String dataElementCategoryDesc;
    /**
     * 分类代码
     */
    private String dataElementCategoryCode;
}

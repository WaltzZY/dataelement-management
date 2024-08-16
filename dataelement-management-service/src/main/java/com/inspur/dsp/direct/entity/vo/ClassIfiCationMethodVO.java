package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassIfiCationMethodVO {
    /**
     * 分类方式名称
     */
    private String classIfiCationMethodName;
    /**
     * 分类数组
     */
    private List<CategoryVO> categoryList;
}

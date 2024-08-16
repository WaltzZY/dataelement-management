package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 宾位词
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectVocabularyVO {
    /**
     * 主位词
     */
    private String subjectVocabulary;
    /**
     * 宾位词
     */
    private String objectVocabulary;
    /**
     * 定义
     */
    private String definition;
    /**
     * 关系
     */
    private String relationship;
}

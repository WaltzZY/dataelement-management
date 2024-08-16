package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 词汇
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectVocabularyVO {
    /**
     * 词汇名称
     */
    private String vocabulary;
    /**
     * 词汇定义
     */
    private String definition;
    /**
     * 宾位词
     */
    private List<ObjectVocabularyVO> objectVocabularyList;
}

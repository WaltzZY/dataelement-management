package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据元关联词汇
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyVO {
    /**
     * 数据元名称
     */
    private String dataElementName;
    /**
     * 主卫词名称
     */
    private List<SubjectVocabularyVO> vocabularyList;
}

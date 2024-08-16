package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import com.inspur.dsp.direct.entity.vo.VocabularyVO;

/**
 * 数据元与词汇关系表
 */
public interface DataElementVocabularyService extends IService<DataElementVocabulary> {

    /**
     * 查询数据元关联词汇数据
     * @param dataElementId 数据元id
     * @return
     */
    VocabularyVO getVocabularylist(String dataElementId);
}

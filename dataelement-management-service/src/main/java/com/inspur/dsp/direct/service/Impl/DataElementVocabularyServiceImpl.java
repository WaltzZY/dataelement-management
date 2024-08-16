package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementVocabularyDao;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import com.inspur.dsp.direct.service.DataElementVocabularyService;
import org.springframework.stereotype.Service;
/**
 * 数据元与词汇关系表
 */
@Service
public class DataElementVocabularyServiceImpl extends ServiceImpl<DataElementVocabularyDao, DataElementVocabulary> implements DataElementVocabularyService {
}

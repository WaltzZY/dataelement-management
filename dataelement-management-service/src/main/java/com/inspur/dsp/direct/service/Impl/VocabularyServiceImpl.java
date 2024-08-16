package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.VocabularyDao;
import com.inspur.dsp.direct.dbentity.business.Vocabulary;
import com.inspur.dsp.direct.service.VocabularyService;
import org.springframework.stereotype.Service;
/**
 * 词汇表
 */
@Service
public class VocabularyServiceImpl extends ServiceImpl<VocabularyDao, Vocabulary> implements VocabularyService {
}

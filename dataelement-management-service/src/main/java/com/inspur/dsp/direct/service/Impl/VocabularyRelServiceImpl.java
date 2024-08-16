package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.VocabularyRelDao;
import com.inspur.dsp.direct.dbentity.business.VocabularyRel;
import com.inspur.dsp.direct.service.VocabularyRelService;
import org.springframework.stereotype.Service;
/**
 * 词汇关系表
 */
@Service
public class VocabularyRelServiceImpl extends ServiceImpl<VocabularyRelDao, VocabularyRel> implements VocabularyRelService {
}

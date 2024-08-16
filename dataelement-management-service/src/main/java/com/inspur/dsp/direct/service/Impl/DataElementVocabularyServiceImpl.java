package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementVocabularyDao;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import com.inspur.dsp.direct.entity.vo.ObjectVocabularyVO;
import com.inspur.dsp.direct.entity.vo.SubjectVocabularyVO;
import com.inspur.dsp.direct.entity.vo.VocabularyVO;
import com.inspur.dsp.direct.service.DataElementVocabularyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据元与词汇关系表
 */
@Service
public class DataElementVocabularyServiceImpl extends ServiceImpl<DataElementVocabularyDao, DataElementVocabulary> implements DataElementVocabularyService {
    @Resource
    private DataElementVocabularyDao dataElementVocabularyDao;

    /**
     * 查询数据元关联词汇数据
     * @param dataElementId 数据元id
     * @return
     */
    @Override
    public VocabularyVO getVocabularylist(String dataElementId){
        VocabularyVO vocabularyVO = new VocabularyVO();
        List<SubjectVocabularyVO> subjectVocabularys = dataElementVocabularyDao.getSubjectVocabularyByDataElementId(dataElementId);
        vocabularyVO.setVocabularyList(subjectVocabularys);
        if (CollectionUtils.isNotEmpty(subjectVocabularys)){
            List<String> collect = subjectVocabularys.stream().map(SubjectVocabularyVO::getVocabulary).collect(Collectors.toList());
            //查询宾位词
            List<ObjectVocabularyVO> objectVocabularyVOS = dataElementVocabularyDao.getObjectVocabularyBySubject(collect);
            if (CollectionUtils.isNotEmpty(objectVocabularyVOS)){
                Map<String, List<ObjectVocabularyVO>> objectVocabularyMap = objectVocabularyVOS.stream().collect(Collectors.groupingBy(ObjectVocabularyVO::getSubjectVocabulary));
                //设置宾位词
                subjectVocabularys.forEach(bean ->{
                    bean.setObjectVocabularyList(objectVocabularyMap.get(bean.getVocabulary()));
                });
            }
        }
        return vocabularyVO;
    }
}

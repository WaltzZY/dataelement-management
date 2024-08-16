package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import java.util.List;

import com.inspur.dsp.direct.entity.vo.ObjectVocabularyVO;
import com.inspur.dsp.direct.entity.vo.SubjectVocabularyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementVocabularyDao extends BaseMapper<DataElementVocabulary> {
    int updateBatch(List<DataElementVocabulary> list);

    int updateBatchSelective(List<DataElementVocabulary> list);

    int batchInsert(@Param("list") List<DataElementVocabulary> list);

    int deleteByPrimaryKeyIn(List<String> list);

    /**
     * 查询数据元关联词汇
     * @param dataElementId 数据元id
     * @return
     */
    List<SubjectVocabularyVO> getSubjectVocabularyByDataElementId(String dataElementId);

    /**
     * 查询宾位词
     * @param vocabularys 主位词数组
     * @return
     */
    List<ObjectVocabularyVO> getObjectVocabularyBySubject(List<String> vocabularys);
}
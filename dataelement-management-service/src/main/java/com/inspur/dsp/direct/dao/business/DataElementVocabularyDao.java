package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementVocabularyDao extends BaseMapper<DataElementVocabulary> {
    int updateBatch(List<DataElementVocabulary> list);

    int updateBatchSelective(List<DataElementVocabulary> list);

    int batchInsert(@Param("list") List<DataElementVocabulary> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
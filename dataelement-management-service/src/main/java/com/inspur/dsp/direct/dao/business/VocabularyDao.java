package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.Vocabulary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VocabularyDao extends BaseMapper<Vocabulary> {
    int updateBatch(List<Vocabulary> list);

    int updateBatchSelective(List<Vocabulary> list);

    int batchInsert(@Param("list") List<Vocabulary> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
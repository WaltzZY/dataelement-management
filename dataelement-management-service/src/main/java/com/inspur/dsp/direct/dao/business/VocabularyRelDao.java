package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.VocabularyRel;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VocabularyRelDao extends BaseMapper<VocabularyRel> {
    int updateBatch(List<VocabularyRel> list);

    int updateBatchSelective(List<VocabularyRel> list);

    int batchInsert(@Param("list") List<VocabularyRel> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
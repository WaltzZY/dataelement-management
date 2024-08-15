package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.StandardFile;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StandardFileDao extends BaseMapper<StandardFile> {
    int updateBatch(List<StandardFile> list);

    int updateBatchSelective(List<StandardFile> list);

    int batchInsert(@Param("list") List<StandardFile> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementBaseDao extends BaseMapper<DataElementBase> {
    int updateBatch(List<DataElementBase> list);

    int updateBatchSelective(List<DataElementBase> list);

    int batchInsert(@Param("list") List<DataElementBase> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
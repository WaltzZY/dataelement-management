package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementDataItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementDataItemDao extends BaseMapper<DataElementDataItem> {
    int updateBatch(List<DataElementDataItem> list);

    int updateBatchSelective(List<DataElementDataItem> list);

    int batchInsert(@Param("list") List<DataElementDataItem> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
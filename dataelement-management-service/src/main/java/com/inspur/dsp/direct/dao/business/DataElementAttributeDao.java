package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementAttribute;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementAttributeDao extends BaseMapper<DataElementAttribute> {
    int updateBatch(List<DataElementAttribute> list);

    int updateBatchSelective(List<DataElementAttribute> list);

    int batchInsert(@Param("list") List<DataElementAttribute> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
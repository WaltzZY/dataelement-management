package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementSample;
import java.util.List;

import com.inspur.dsp.direct.entity.vo.DataElementSampleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementSampleDao extends BaseMapper<DataElementSample> {
    int updateBatch(List<DataElementSample> list);

    int updateBatchSelective(List<DataElementSample> list);

    int batchInsert(@Param("list") List<DataElementSample> list);

    int deleteByPrimaryKeyIn(List<String> list);

    List<DataElementSampleVO> getList(@Param("dataElementId") String dataElementId);
}
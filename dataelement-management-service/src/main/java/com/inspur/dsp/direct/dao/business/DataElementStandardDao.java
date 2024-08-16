package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementStandard;
import java.util.List;

import com.inspur.dsp.direct.entity.vo.DataElementStandardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementStandardDao extends BaseMapper<DataElementStandard> {
    int updateBatch(List<DataElementStandard> list);

    int updateBatchSelective(List<DataElementStandard> list);

    int batchInsert(@Param("list") List<DataElementStandard> list);

    int deleteByPrimaryKeyIn(List<String> list);

    List<DataElementStandardVO> getList(@Param("dataElementId") String dataElementId);
}
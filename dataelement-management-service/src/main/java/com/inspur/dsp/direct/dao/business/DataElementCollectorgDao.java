package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementCollectorg;
import java.util.List;

import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementCollectorgDao extends BaseMapper<DataElementCollectorg> {
    int updateBatch(List<DataElementCollectorg> list);

    int updateBatchSelective(List<DataElementCollectorg> list);

    int batchInsert(@Param("list") List<DataElementCollectorg> list);

    int deleteByPrimaryKeyIn(List<String> list);

    List<DataElementCollectOrgVO> getList(@Param("dataElementId") String dataElementId);
}
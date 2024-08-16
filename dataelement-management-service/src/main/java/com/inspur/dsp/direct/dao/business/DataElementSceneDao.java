package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementScene;
import java.util.List;

import com.inspur.dsp.direct.entity.vo.DataElementSceneVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementSceneDao extends BaseMapper<DataElementScene> {
    int updateBatch(List<DataElementScene> list);

    int updateBatchSelective(List<DataElementScene> list);

    int batchInsert(@Param("list") List<DataElementScene> list);

    int deleteByPrimaryKeyIn(List<String> list);

    List<DataElementSceneVO> getList(@Param("dataElementId") String dataElementId);
}
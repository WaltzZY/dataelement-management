package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementSceneAttachFile;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementSceneAttachFileDao extends BaseMapper<DataElementSceneAttachFile> {
    int updateBatch(List<DataElementSceneAttachFile> list);

    int updateBatchSelective(List<DataElementSceneAttachFile> list);

    int batchInsert(@Param("list") List<DataElementSceneAttachFile> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
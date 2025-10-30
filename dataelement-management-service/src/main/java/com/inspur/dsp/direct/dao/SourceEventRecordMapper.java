package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SourceEventRecordMapper extends BaseMapper<SourceEventRecord> {

    SourceEventRecord selectFirstByDataElementIdOrderBySourceDateDesc(@Param("dataElementId") String dataElementId);

    SourceEventRecord getProcessMessageForRecord(@Param("dataid") String dataId);


}
package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.ProcessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessRecordMapper extends BaseMapper<ProcessRecord> {
    ProcessRecord getProcessRecoderForPublished(@Param("dataid") String dataId);
    
    ProcessRecord getRevisionRecordByDataId(@Param("dataid") String dataId);
    
    List<ProcessRecord> getAllRevisionRecordsByDataId(@Param("dataid") String dataId);
    
    List<ProcessRecord> getProcessRecordsByDataId(@Param("dataid") String dataId);
}
package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.ProcessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcessRecordMapper extends BaseMapper<ProcessRecord> {

    ProcessRecord getProcessRecoderForPublished(@Param("dataid") String dataId);


}
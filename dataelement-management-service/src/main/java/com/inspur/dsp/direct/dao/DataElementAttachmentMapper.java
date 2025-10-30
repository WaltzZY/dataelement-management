package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.DataElementAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataElementAttachmentMapper extends BaseMapper<DataElementAttachment> {

    /**
     * 根据数据元ID和文件类型查询文件列表(定标模块专用)
     * @param baseDataelementDataid 数据元ID
     * @param attachfiletype 文件类型
     * @return 文件列表
     */
    List<DataElementAttachment> selectByDataElementIdAndType(
            @Param("baseDataelementDataid") String baseDataelementDataid,
            @Param("attachfiletype") String attachfiletype);
}
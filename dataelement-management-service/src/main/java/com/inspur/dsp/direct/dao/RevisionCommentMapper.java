package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.entity.RevisionComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 修订意见Mapper
 * 
 * @author system
 * @since 2025
 */
@Mapper
public interface RevisionCommentMapper extends BaseMapper<RevisionComment> {
    
    /**
     * 根据数据元ID查询修订意见
     * 
     * @param dataElementId 数据元ID
     * @return 修订意见（如存在多行，按create_date时间倒序取第一行）
     */
    RevisionComment selectbydataid(@Param("dataElementId") String dataElementId);
}
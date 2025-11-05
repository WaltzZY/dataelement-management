package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.StandardDataElementPageQueryDto;
import com.inspur.dsp.direct.entity.vo.StandardDataElementPageInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据元编制标准Mapper
 * 
 * @author system
 * @since 2025
 */
@Mapper
public interface DataElementStandardMapper extends BaseMapper<BaseDataElement> {
    
    /**
     * 定标阶段数据元列表查询
     * 
     * @param page 分页对象（可为null，null时查询全部）
     * @param queryDto 查询条件
     * @return 数据元列表查询结果
     */
    List<StandardDataElementPageInfoVo> getAllStandardList(
            @Param("page") Page<StandardDataElementPageInfoVo> page, 
            @Param("queryDto") StandardDataElementPageQueryDto queryDto);
}
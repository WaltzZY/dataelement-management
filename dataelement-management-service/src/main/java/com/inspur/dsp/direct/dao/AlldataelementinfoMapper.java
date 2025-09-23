package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据元信息相关Mapper接口
 *
 * @author Claude Code
 * @since 2025-09-22
 */
public interface AlldataelementinfoMapper extends BaseMapper<BaseDataElement> {

    /**
     * 数据元列表查询
     * @param page 分页对象
     * @param queryDto 查询条件
     * @return 数据元列表信息
     */
    List<DataElementPageInfoVo> getAllDataElementPage(@Param("page") Page<?> page, @Param("queryDto") DataElementPageQueryDto queryDto);
}
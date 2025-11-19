package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.StandardDataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.AuditDataElementQueryDto;
import com.inspur.dsp.direct.entity.vo.StandardDataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.AuditDataElementVo;
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
     * @param userOrgCode 用户组织机构编码（用于过滤数据源单位）
     * @return 数据元列表查询结果
     */
    List<StandardDataElementPageInfoVo> getAllStandardList(
            @Param("page") Page<StandardDataElementPageInfoVo> page, 
            @Param("queryDto") StandardDataElementPageQueryDto queryDto,
            @Param("userOrgCode") String userOrgCode);

    /**
     * 审核标准模块数据元查询
     * 
     * @param queryDto 查询条件
     * @return 数据元列表查询结果
     */
    List<AuditDataElementVo> queryAuditDataElementList(@Param("queryDto") AuditDataElementQueryDto queryDto);

    /**
     * 审核标准模块数据元分页查询
     * 
     * @param page 分页对象
     * @param queryDto 查询条件
     * @return 分页数据元列表查询结果
     */
    Page<AuditDataElementVo> queryAuditDataElementListPage(@Param("page") Page<AuditDataElementVo> page, @Param("queryDto") AuditDataElementQueryDto queryDto);
}
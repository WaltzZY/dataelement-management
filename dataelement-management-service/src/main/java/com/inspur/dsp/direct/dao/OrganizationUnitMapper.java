package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface OrganizationUnitMapper extends BaseMapper<OrganizationUnit> {

    OrganizationUnit selectFirstByUnitCode(@Param("unitCode")String unitCode);

    /**
     * 获取部门树
     * @param dto
     * @return
     */
    List<OrganizationUnit> getCollectionDeptTree(GetCollectionDeptTreeDto dto);

    /**
     * 根据部门编码批量查询部门信息
     * @param unitCodeCollection
     * @return
     */
    List<OrganizationUnit> selectAllByUnitCodeIn(@Param("unitCodeCollection")Collection<String> unitCodeCollection);

    /**
     * 获取部门搜索结果
     * @param page
     * @param dto
     * @return
     */
    List<OrganizationUnit> getCollectionDeptSearch(Page page, @Param("dto") GetDeptSearchDto dto);
}
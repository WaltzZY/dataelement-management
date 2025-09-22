package com.inspur.dsp.direct.dao;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import org.apache.ibatis.annotations.Mapper;

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
}
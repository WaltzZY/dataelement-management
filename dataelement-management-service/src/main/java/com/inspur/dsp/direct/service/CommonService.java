package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;

import java.util.List;

public interface CommonService {
    /**
     * 获取部门树
     */
    List<CollectionDeptTreeVo> getCollectionDeptTree(GetCollectionDeptTreeDto dto);

    /**
     * 获取部门树搜索
     */
    GetDeptSearchVo getCollectionDeptSearch(GetDeptSearchDto dto);

    /**
     * 获取部门信息
     */
    GetOrganInfoVo getOrganInfo(String organCode);

    /**
     * 获取部门信息-产品内部部门表
     * @param orgCode 部门统一社会信用代码
     *
     * @return 部门信息
     */
    OrganizationUnit getOrgInfoByOrgCode(String orgCode);
}

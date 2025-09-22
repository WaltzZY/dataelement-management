package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.dao.OrganizationUnitMapper;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.DeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;
import com.inspur.dsp.direct.enums.OrganRegionEnums;
import com.inspur.dsp.direct.httpentity.dto.GetOrganByNameLikeDto;
import com.inspur.dsp.direct.httpentity.vo.GetOrganByNameLikeVo;
import com.inspur.dsp.direct.httpentity.vo.OrganInfoVo;
import com.inspur.dsp.direct.httpentity.vo.RegionOrganTreeVo;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.httpService.BSPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final BSPService bspService;
    private final OrganizationUnitMapper organizationUnitMapper;

    /**
     * 获取部门树
     *
     * @param dto
     */
    @Override
    public List<CollectionDeptTreeVo> getCollectionDeptTree(GetCollectionDeptTreeDto dto) {
        // 查询部门区划树内部表
        List<OrganizationUnit> organizationUnits = organizationUnitMapper.getCollectionDeptTree(dto);


        // 调用bsp接口,查询部门树数据
        List<RegionOrganTreeVo> regionOrganTree = bspService.getRegionOrganTree(dto.getParentCode());
        return regionOrganTree.stream().map(regionOrganTreeVo -> {
            CollectionDeptTreeVo collectionDeptTreeVo = new CollectionDeptTreeVo();
            collectionDeptTreeVo.setCode(regionOrganTreeVo.getCode());
            collectionDeptTreeVo.setId(regionOrganTreeVo.getId());
            collectionDeptTreeVo.setName(regionOrganTreeVo.getName());
            collectionDeptTreeVo.setType(regionOrganTreeVo.getType());
            return collectionDeptTreeVo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取部门树搜索
     *
     * @param dto
     */
    @Override
    public GetDeptSearchVo getCollectionDeptSearch(GetDeptSearchDto dto) {
        GetOrganByNameLikeVo organByNameLike = bspService
                .getOrganByNameLike(new GetOrganByNameLikeDto(dto.getPageNum(), dto.getPageSize(), dto.getOrganName()));
        Long total = organByNameLike.getTotal();
        if (total == 0L) {
            return new GetDeptSearchVo(0L, Collections.emptyList());
        }
        List<OrganInfoVo> data = organByNameLike.getData();
        List<DeptSearchVo> rows = data.stream().map(vo -> {
            DeptSearchVo deptSearchVo = new DeptSearchVo();
            deptSearchVo.setCode(vo.getCode());
            deptSearchVo.setId(vo.getId());
            deptSearchVo.setName(vo.getName());
            deptSearchVo.setNamePath(vo.getOrganPath());
            deptSearchVo.setType(OrganRegionEnums.ORGAN.getCode());
            return deptSearchVo;
        }).collect(Collectors.toList());
        return new GetDeptSearchVo(total, rows);
    }

    /**
     * 获取部门信息
     *
     * @param organCode
     */
    @Override
    public GetOrganInfoVo getOrganInfo(String organCode) {
        OrganInfoVo organInfo = bspService.getOrganInfo(organCode);
        if (organInfo != null) {
            GetOrganInfoVo getOrganInfoVo = new GetOrganInfoVo();
            getOrganInfoVo.setId(organInfo.getId());
            getOrganInfoVo.setCode(organInfo.getCode());
            getOrganInfoVo.setName(organInfo.getName());
            getOrganInfoVo.setLeader("系统联系人");
            getOrganInfoVo.setLeaderPhone("13050005000");
            getOrganInfoVo.setNamePath(organInfo.getOrganPath());
            return getOrganInfoVo;
        }
        throw new RuntimeException("未查询到部门信息");
    }

    /**
     * 获取部门信息-产品内部部门表
     *
     * @param orgCode 部门统一社会信用代码
     * @return 部门信息
     */
    @Override
    public OrganizationUnit getOrgInfoByOrgCode(String orgCode) {
        return organizationUnitMapper.selectFirstByUnitCode(orgCode);
    }
}

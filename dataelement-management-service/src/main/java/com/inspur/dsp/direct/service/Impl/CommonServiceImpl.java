package com.inspur.dsp.direct.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.inspur.dsp.direct.dao.OrganizationUnitMapper;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.DeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;
import com.inspur.dsp.direct.enums.OrganRegionEnums;
import com.inspur.dsp.direct.httpService.BSPService;
import com.inspur.dsp.direct.httpentity.dto.GetOrganByNameLikeDto;
import com.inspur.dsp.direct.httpentity.vo.GetOrganByNameLikeVo;
import com.inspur.dsp.direct.httpentity.vo.OrganInfoVo;
import com.inspur.dsp.direct.httpentity.vo.RegionOrganTreeVo;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    /**
     * 批量获取部门信息-产品内部部门表
     *
     * @param orgCodes 部门统一社会信用代码
     * @return 部门信息
     */
    @Override
    public List<OrganizationUnit> getOrgInfoByBatchOrgCode(List<String> orgCodes) {
        if (CollectionUtils.isEmpty(orgCodes)) {
            log.warn("部门统一社会信用代码集合为空,退出方法[getOrgInfoByBatchOrgCode]");
            return new ArrayList<>();
        }
        List<OrganizationUnit> organizationUnits = organizationUnitMapper.selectAllByUnitCodeIn(orgCodes);
        if (CollectionUtils.isEmpty(organizationUnits)) {
            log.warn("无部门信息,退出方法[getOrgInfoByBatchOrgCode]");
            return new ArrayList<>();
        }
        return organizationUnits;
    }

    /**
     * excel导入统一处理方法
     *
     * @param file  Excel文件
     * @param clazz Excel数据实体类类型
     * @param <T>   数据实体类型
     * @return 解析后的数据列表
     */
    @Override
    public <T> List<T> importExcelData(MultipartFile file, Class<T> clazz) {
        try (InputStream inputStream = file.getInputStream()) {
            // 使用EasyExcel读取文件
            List<T> dataList = EasyExcel.read(inputStream)
                    .head(clazz)
                    .sheet()
                    .doReadSync();
            return dataList;
        } catch (IOException e) {
            log.error("读取Excel文件失败", e);
            throw new RuntimeException("读取Excel文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * excel导出统一处理方法
     *
     * @param dataList 导出的数据集合
     * @param response HttpServletResponse对象
     * @param fileName 导出的文件名
     * @param clazz    Excel数据实体类类型
     * @param <T>      数据实体类型
     */
    @Override
    public <T> void exportExcelData(List<T> dataList, HttpServletResponse response, String fileName, Class<T> clazz) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

        // 使用EasyExcel写入数据
        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet("数据列表")
                .doWrite(dataList);
    }
}

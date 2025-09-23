package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    /**
     * 批量获取部门信息-产品内部部门表
     * @param orgCodes 部门统一社会信用代码
     *
     * @return 部门信息
     */
    List<OrganizationUnit> getOrgInfoByBatchOrgCode(List<String> orgCodes);

    /**
     * excel导入统一处理方法
     */
    /**
     * excel导入统一处理方法
     * @param file Excel文件
     * @param clazz Excel数据实体类类型
     * @param <T> 数据实体类型
     * @return 解析后的数据列表
     */
    <T> List<T> importExcelData(MultipartFile file, Class<T> clazz);

    /**
     * excel导出统一处理方法
     * @param dataList 导出的数据集合
     * @param response HttpServletResponse对象
     * @param fileName 导出的文件名
     * @param clazz Excel数据实体类类型
     * @param <T> 数据实体类型
     * @throws IOException IO异常
     */
    <T> void exportExcelData(List<T> dataList, HttpServletResponse response, String fileName, Class<T> clazz) throws IOException;
}

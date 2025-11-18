package com.inspur.dsp.direct.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.constant.Constants;
import com.inspur.dsp.direct.dao.FlowActivityDefinitionMapper;
import com.inspur.dsp.direct.dao.FlowDefinitionMapper;
import com.inspur.dsp.direct.dao.FlowTransferDefinitionMapper;
import com.inspur.dsp.direct.dao.OrganizationUnitMapper;
import com.inspur.dsp.direct.dbentity.FlowActivityDefinition;
import com.inspur.dsp.direct.dbentity.FlowDefinition;
import com.inspur.dsp.direct.dbentity.FlowTransferDefinition;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.DeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final OrganizationUnitMapper organizationUnitMapper;
    private final FlowDefinitionMapper flowDefinitionMapper;
    private final FlowTransferDefinitionMapper flowTransferDefinitionMapper;
    private final FlowActivityDefinitionMapper flowActivityDefinitionMapper;

    @Value("${upload.path}")
    private String uploadFilePath;

    /**
     * 获取部门树
     *
     * @param dto
     */
    @Override
    public List<CollectionDeptTreeVo> getCollectionDeptTree(GetCollectionDeptTreeDto dto) {
        log.info("[getCollectionDeptTree]参数: dto={}", dto);
        String parentCode = dto.getParentCode();
        // 如果parentCode为空,则只查询顶层部门,顶层部门父级为NULL
        if (!StringUtils.hasText(parentCode)) {
            dto.setParentCode(null);
        }
        // 查询部门区划树内部表
        List<OrganizationUnit> organizationUnits = organizationUnitMapper.getCollectionDeptTree(dto);
        // 遍历organizationUnits将dataid收集为list
        List<String> parentNodeIdCollection = organizationUnits.stream()
                .map(OrganizationUnit::getDataid)
                .collect(Collectors.toList());
        // 查询每个节点的下级节点数量
        Map<String, Long> parentNodeIdCountMap;
        if (!CollectionUtils.isEmpty(parentNodeIdCollection)) {
            List<String> parentNodeIds = organizationUnitMapper.selectParentNodeIdByParentNodeIdIn(parentNodeIdCollection);
            // 将parentNodeIds的每个id分组计算每个id的个数
            parentNodeIdCountMap = parentNodeIds.stream()
                    .collect(Collectors.groupingBy(parentNodeId -> parentNodeId, Collectors.counting()));
        } else {
            parentNodeIdCountMap = new HashMap<>();
        }

        List<CollectionDeptTreeVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(organizationUnits)) {
            vos = organizationUnits.stream()
                    .filter(Objects::nonNull)
                    .map(unit -> {
                                CollectionDeptTreeVo collectionDeptTreeVo = new CollectionDeptTreeVo();
                                collectionDeptTreeVo.setCode(unit.getUnitCode());
                                collectionDeptTreeVo.setId(unit.getDataid());
                                collectionDeptTreeVo.setName(unit.getUnitName());
                                collectionDeptTreeVo.setType(unit.getNodeType());
                                collectionDeptTreeVo.setChildCount(parentNodeIdCountMap.getOrDefault(unit.getDataid(), 0L));
                                return collectionDeptTreeVo;
                            }
                    ).collect(Collectors.toList());
        }
        return vos;
    }

    /**
     * 获取部门树搜索
     *
     * @param dto
     */
    @Override
    public GetDeptSearchVo getCollectionDeptSearch(GetDeptSearchDto dto) {
        Page page = new Page(dto.getPageNum(), dto.getPageSize());
        List<OrganizationUnit> collectionDeptSearch = organizationUnitMapper.getCollectionDeptSearch(page, dto);
        page.setRecords(collectionDeptSearch);
        if (CollectionUtils.isEmpty(collectionDeptSearch)) {
            return new GetDeptSearchVo(0L, Collections.emptyList());
        }
        List<DeptSearchVo> rows = collectionDeptSearch.stream().map(unit -> {
            DeptSearchVo deptSearchVo = new DeptSearchVo();
            deptSearchVo.setCode(unit.getUnitCode());
            deptSearchVo.setId(unit.getDataid());
            deptSearchVo.setName(unit.getUnitName());
            deptSearchVo.setType(unit.getNodeType());
            // 调用循环查询,获取部门全路径
            deptSearchVo.setNamePath(buildOrganizationUnitPath(unit));
            return deptSearchVo;
        }).collect(Collectors.toList());
        return new GetDeptSearchVo(page.getTotal(), rows);
    }

    /**
     * 构建组织单位的全路径
     *
     * @param unit 组织单位
     * @return 完整路径名称
     */
    private String buildOrganizationUnitPath(OrganizationUnit unit) {
        StringBuilder path = new StringBuilder(unit.getUnitName());
        String parentId = unit.getParentNodeId();

        // 循环查询父级部门，直到根节点
        while (parentId != null) {
            OrganizationUnit parentUnit = organizationUnitMapper.selectById(parentId);
            if (parentUnit == null) {
                break;
            }
            // 在路径前面添加父级部门名称
            path.insert(0, parentUnit.getUnitName() + Constants.SLASH);
            // 更新parentId为父级部门的parentId
            parentId = parentUnit.getParentNodeId();
        }
        return path.toString();
    }

    /**
     * 获取部门信息
     *
     * @param organCode
     */
    @Override
    public GetOrganInfoVo getOrganInfo(String organCode) {
        OrganizationUnit organizationUnit = organizationUnitMapper.selectFirstByUnitCode(organCode);
        if (organizationUnit != null) {
            GetOrganInfoVo getOrganInfoVo = new GetOrganInfoVo();
            getOrganInfoVo.setId(organizationUnit.getDataid());
            getOrganInfoVo.setCode(organizationUnit.getUnitCode());
            getOrganInfoVo.setName(organizationUnit.getUnitName());
            getOrganInfoVo.setLeader(organizationUnit.getContactName());
            getOrganInfoVo.setLeaderPhone(organizationUnit.getContactPhone());
            getOrganInfoVo.setNamePath(buildOrganizationUnitPath(organizationUnit));
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

    /**
     * 文件上传,返回文件上传的保存路径
     *
     * @param file
     */
    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        try {
            Path uploadDir = Paths.get(uploadFilePath);
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

            // 构建完整文件路径
            Path filePath = uploadDir.resolve(uniqueFileName);

            // 保存文件
            Files.copy(file.getInputStream(), filePath);

            // 返回文件名（或者根据需要返回完整路径）
            return filePath.toString();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 文件下载,返回文件二进制数据
     *
     * @param filePath
     */
    @Override
    public byte[] downloadFile(String filePath) {
        try {
            // 构建完整文件路径
            Path fullPath = Paths.get(filePath);
            // 检查文件是否存在
            if (!Files.exists(fullPath)) {
                throw new RuntimeException("文件不存在: " + filePath);
            }
            // 读取文件内容并返回字节数组
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            log.error("文件下载失败: {}", filePath, e);
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据流程编号查询流程定义及关联的流程步骤,节点信息
     *
     * @param code 流程编号
     * @return
     */
    @Override
    public FlowDefinition selectFlowDefinitionByCode(String code) {
        FlowDefinition flowDefinition = flowDefinitionMapper.selectFirstByFlowcode(code);
        if (Objects.isNull(flowDefinition)) {
            log.warn("无流程定义信息,退出方法[selectFlowDefinitionByCode],查询流程code为:{}", code);
            return null;
        }
        List<FlowTransferDefinition> flowTransferDefinitions = flowTransferDefinitionMapper.selectAllByFlowid(flowDefinition.getFlowid());
        List<FlowActivityDefinition> flowActivityDefinitions = flowActivityDefinitionMapper.selectAllByFlowidOrderByActivityorder(flowDefinition.getFlowid());
        flowDefinition.setFlowTransferDefinitions(flowTransferDefinitions);
        flowDefinition.setFlowActivityDefinitions(flowActivityDefinitions);
        return flowDefinition;
    }
}

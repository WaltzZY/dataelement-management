package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.entity.dto.*;
import com.inspur.dsp.direct.entity.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数据元编制标准Service接口
 * 负责定标保存的所有业务逻辑
 *
 * @author system
 * @since 2025
 */
public interface DataElementStandardService {

    /**
     * 获取编制标准完整信息
     * 一次性查询数据元的完整编制标准信息
     *
     * @param dataid 数据元ID
     * @param sourceunitcode 数源单位统一社会信用代码
     * @return 包含基本信息、联系人、属性、关联目录、标准文件、样例文件的完整VO
     */
    StandardCompleteInfoVo getStandardCompleteInfo(String dataid, String sourceunitcode);

    /**
     * 查询可关联的目录数据项
     * 查询本单位可关联的目录数据项，支持关键字搜索和分页
     *
     * @param dto 查询条件DTO
     * @return 包含数据元名称/定义和分页目录数据项列表
     */
    SearchCatalogItemResultVo searchCatalogItems(SearchCatalogItemDto dto);

    /**
     * 保存目录-数据元关联
     * 批量保存数据元与目录数据项的关联关系
     *
     * @param dto 关联请求DTO
     */
    void associateCatalog(AssociateCatalogDto dto);

    /**
     * 查询目录-数据元关联列表
     * 查询指定数据元已关联的目录列表
     *
     * @param dataid 数据元ID
     * @param sourceOrgCode 数源单位编码(可选)
     * @return 关联目录列表
     */
    List<AssociatedCatalogVo> getAssociatedCatalogs(String dataid, String sourceOrgCode);

    /**
     * 取消关联
     * 删除指定的目录-数据元关联关系
     *
     * @param relationid 关联关系ID
     */
    void cancelAssociation(String relationid);

    /**
     * 上传文件
     * 上传标准规范文件或样例文件
     *
     * @param file 上传的文件
     * @param dataid 数据元ID
     * @param filetype 文件类型("standardfile"或"examplefile")
     */
    void uploadFile(MultipartFile file, String dataid, String filetype);

    /**
     * 查询文件列表
     * 查询指定数据元的文件列表
     *
     * @param dataid 数据元ID
     * @param filetype 文件类型("standardfile"或"examplefile")
     * @return 文件列表
     */
    List<AttachmentFileVo> getFileList(String dataid, String filetype);

    /**
     * 删除文件
     * 删除指定的附件文件
     *
     * @param attachFileId 附件ID
     */
    void deleteFile(String attachFileId);

    /**
     * 保存编制信息
     * 保存编制标准的基本信息、联系人信息和属性信息
     *
     * @param dto 保存请求DTO
     */
    void saveStandard(SaveStandardDto dto);

    /**
     * 提交审核
     * 保存编制标准信息并提交审核，状态流转到下一阶段
     *
     * @param dto 提交请求DTO
     * @return 提交结果VO
     */
    SubmitResultVo submitStandard(SaveStandardDto dto);
}
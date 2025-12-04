package com.inspur.dsp.direct.service.Impl;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.*;
import com.inspur.dsp.direct.dbentity.*;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.*;
import com.inspur.dsp.direct.entity.vo.*;
import com.inspur.dsp.direct.entity.RevisionComment;
import com.inspur.dsp.direct.enums.CalibrationStatusEnums;
import com.inspur.dsp.direct.httpService.BasecatalogService;
import com.inspur.dsp.direct.httpentity.dto.QueryCatalogByColumnNameDto;
import com.inspur.dsp.direct.httpentity.vo.QueryCatalogByColumnNameVo;
import com.inspur.dsp.direct.service.DataElementStandardService;
import com.inspur.dsp.direct.service.FlowProcessService;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 数据元编制标准Service实现类
 * 负责编制标准的具体业务逻辑实现
 *
 * @author system
 * @since 2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataElementStandardServiceImpl implements DataElementStandardService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final DataElementContactMapper dataElementContactMapper;
    private final DataElementAttributeMapper dataElementAttributeMapper;
    private final DataElementCatalogRelationMapper dataElementCatalogRelationMapper;
    private final DataElementAttachmentMapper dataElementAttachmentMapper;
    private final OrganizationUnitMapper organizationUnitMapper;
    private final FlowProcessService flowProcessService;
    private final BasecatalogService basecatalogService;
    private final CommonService commonService;
    private final DataElementStandardMapper dataElementStandardMapper;
    private final RevisionCommentMapper revisionCommentMapper;
    private final ProcessRecordMapper processRecordMapper;
    private final FlowActivityDefinitionMapper flowActivityDefinitionMapper;

    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @Override
    public StandardCompleteInfoVo getStandardCompleteInfo(String dataid, String sourceunitcode) {
        log.info("获取编制标准完整信息 - dataid: {}, sourceunitcode: {}", dataid, sourceunitcode);

        // 查询基本信息
        BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
        log.info("查询BaseDataElement结果: {}", dataElement);
        
        if (dataElement == null) {
            log.warn("数据元不存在 - dataid: {}", dataid);
            throw new IllegalArgumentException("数据元不存在");
        }

        BasicInfoVo basicInfo = new BasicInfoVo();
        basicInfo.setDataid(dataElement.getDataid());
        basicInfo.setDataelementcode(dataElement.getDataElementId());
        basicInfo.setName(dataElement.getName());
        basicInfo.setStatus(dataElement.getStatus());
        basicInfo.setStatusDesc(CalibrationStatusEnums.getDescByCode(dataElement.getStatus()));
        basicInfo.setDefinition(dataElement.getDefinition());
        basicInfo.setDatatype(dataElement.getDatatype());
        basicInfo.setDataFormat(dataElement.getDataFormat());
        basicInfo.setValueDomain(dataElement.getValueDomain());
        basicInfo.setRemarks(dataElement.getRemarks());

        // 查询联系人信息
        ContactInfoVo contactInfo = new ContactInfoVo();
        DataElementContact contact = dataElementContactMapper.selectByDataElementId(dataid);

        OrganizationUnit organizationUnit = organizationUnitMapper.selectByCode(sourceunitcode);

        if (contact != null) {
            contactInfo.setDataid(contact.getDataid());
            contactInfo.setContactName(contact.getContactname());
            contactInfo.setContactTel(contact.getContacttel());
        }
        contactInfo.setUnitCode(organizationUnit.getUnitCode());
        contactInfo.setUnitName(organizationUnit.getUnitName());
        contactInfo.setUnitRegion(organizationUnit.getUnitRegion());

        // 查询属性列表
        List<DataElementAttribute> attributes = dataElementAttributeMapper.selectByDataElementId(dataid);
        List<AttributeVo> attributeVos = new ArrayList<>();
        for (DataElementAttribute attr : attributes) {
            AttributeVo vo = new AttributeVo();
            vo.setDataid(attr.getDataid());
            vo.setAttributeName(attr.getAttributename());
            vo.setAttributeValue(attr.getAttributevalue());
            attributeVos.add(vo);
        }

        // 查询关联目录列表
        List<DataElementCatalogRelation> relations = dataElementCatalogRelationMapper.selectAssociatedCatalogs(dataid, sourceunitcode);
        List<AssociatedCatalogVo> catalogVos = new ArrayList<>();
        for (DataElementCatalogRelation relation : relations) {
            AssociatedCatalogVo vo = new AssociatedCatalogVo();
            vo.setRelationid(relation.getDataid());
            vo.setCatalogId(relation.getCatalogId());
            vo.setCatalogName(relation.getCatalogName());
            vo.setCatalogDescription(relation.getCatalogDesc());
            vo.setCatalogitemid(relation.getInfoItemId());
            vo.setDataItemName(relation.getInfoItemName());
            vo.setDataType(relation.getInfoItemDatatype());
            vo.setSourceOrgCode(relation.getCatalogUnitCode());
            vo.setSourceOrgName(relation.getCatalogUnitName());
            
            // 生成目录预览URL
            try {
                String previewUrl = basecatalogService.getCatalogPreviewUrl(
                    relation.getCatalogId(), 
                    relation.getCatalogUnitCode(), 
                    relation.getCatalogName()
                );
                vo.setPreviewUrl(previewUrl);
            } catch (Exception e) {
                log.warn("生成目录预览URL失败，catalogId: {}, error: {}", relation.getCatalogId(), e.getMessage());
                vo.setPreviewUrl("");
            }
            
            catalogVos.add(vo);
        }

        // 查询标准文件列表
        List<AttachmentFileVo> standardFiles = getFileList(dataid, "standardfile");

        // 查询样例文件列表
        List<AttachmentFileVo> exampleFiles = getFileList(dataid, "examplefile");

        // 查询驳回信息列表（从流程记录表获取）
        List<RejectionInfoVO> rejectionInfo = new ArrayList<>();
        List<ProcessRecord> revisionProcessRecords = processRecordMapper.getAllRevisionRecordsByDataId(dataid);
        if (revisionProcessRecords != null && !revisionProcessRecords.isEmpty()) {
            for (int i = 0; i < revisionProcessRecords.size(); i++) {
                ProcessRecord processRecord = revisionProcessRecords.get(i);
                RejectionInfoVO vo = new RejectionInfoVO();
                
                // 如果是多条记录，添加序号到驳回内容中
                String content = processRecord.getUsersuggestion();
                if (revisionProcessRecords.size() > 1) {
                    content = (i + 1) + ". " + content;
                }
                vo.setRevisionContent(content);
                vo.setRevisionCreatedate(processRecord.getProcessdatetime());
                vo.setCreateUserName(processRecord.getProcessusername());
                vo.setCreateUserOrgName(processRecord.getProcessunitname());
                
                // 映射驳回节点名称
                String rejectionNode = mapRejectionNode(processRecord.getSourceactivityname());
                vo.setRevisionNode(rejectionNode);
                vo.setContactTel(""); // 流程记录中没有联系电话，设为空
                
                rejectionInfo.add(vo);
            }
        }

        // 查询修订意见列表（从修订意见记录表获取）
        List<RevisionCommentVO> revisionComments = new ArrayList<>();
        List<RevisionComment> revisionCommentEntities = revisionCommentMapper.selectAllByDataId(dataid);
        if (revisionCommentEntities != null && !revisionCommentEntities.isEmpty()) {
            for (int i = 0; i < revisionCommentEntities.size(); i++) {
                RevisionComment entity = revisionCommentEntities.get(i);
                RevisionCommentVO vo = new RevisionCommentVO();
                
                // 如果是多条记录，添加序号到修订意见内容中
                String content = entity.getComment();
                if (revisionCommentEntities.size() > 1) {
                    content = (i + 1) + ". " + content;
                }
                vo.setRevisionContent(content);
                vo.setRevisionCreatedate(entity.getRevisionCreatedate());
                vo.setRevisionInitiatorAccount(entity.getCreateAccount());
                vo.setRevisionInitiatorName(entity.getRevisionInitiatorName()); // 当前设为空，后续可以添加用户信息查询
                vo.setContactTel(entity.getRevisionInitiatorTel());
                
                revisionComments.add(vo);
            }
        }

        // 查询流程记录列表
        List<ProcessVO> processRecords = getProcessRecords(dataid);

        // 封装完整VO
        StandardCompleteInfoVo result = new StandardCompleteInfoVo();
        result.setBasicInfo(basicInfo);
        result.setContactInfo(contactInfo);
        result.setAttributes(attributeVos);
        result.setAssociatedCatalogs(catalogVos);
        result.setStandardFiles(standardFiles);
        result.setExampleFiles(exampleFiles);
        result.setRejectionInfo(rejectionInfo);
        result.setRevisionComments(revisionComments);
        result.setProcessRecords(processRecords);

        return result;
    }

    @Override
    public SearchCatalogItemResultVo searchCatalogItems(SearchCatalogItemDto dto) {

        String keyword = dto.getKeyword();

        // 调用目录服务查询可关联的数据项
        QueryCatalogByColumnNameDto queryDto = new QueryCatalogByColumnNameDto();
        queryDto.setName(keyword);
        List<String> orgCodes = new ArrayList<>();
        orgCodes.add(dto.getSourceOrgCode());
        queryDto.setOrgCodes(orgCodes);
        
        List<QueryCatalogByColumnNameVo> catalogItems = basecatalogService.getRelationCatalogItemLike(queryDto);
        
        // 转换为CatalogItemVo并实现分页
        List<CatalogItemVo> catalogItemVos = new ArrayList<>();
        for (QueryCatalogByColumnNameVo item : catalogItems) {
            CatalogItemVo vo = new CatalogItemVo();
            vo.setCatalogitemid(item.getInfoItemId());
            vo.setDataItemName(item.getInfoItemName());
            vo.setDataType(item.getInfoItemDatatype());
            vo.setCatalogId(item.getCatalogId());
            vo.setCatalogName(item.getCatalogName());
            vo.setCatalogDescription(item.getCatalogDesc());
            vo.setSourceOrgCode(item.getCatalogUnitCode());
            vo.setSourceOrgName(item.getCatalogUnitName());
            catalogItemVos.add(vo);
        }
        
        // 手动分页
        int startIndex = (dto.getPageNum() - 1) * dto.getPageSize();
        int endIndex = Math.min(startIndex + dto.getPageSize(), catalogItemVos.size());
        List<CatalogItemVo> pagedItems = startIndex < catalogItemVos.size() ? 
            catalogItemVos.subList(startIndex, endIndex) : new ArrayList<>();
        
        SearchCatalogItemResultVo result = new SearchCatalogItemResultVo();
        result.setItems(pagedItems);
        result.setPageNum(dto.getPageNum());
        result.setPageSize(dto.getPageSize());
        result.setTotal(catalogItemVos.size());
        result.setPages((catalogItemVos.size() + dto.getPageSize() - 1) / dto.getPageSize());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateCatalog(AssociateCatalogDto dto) {
        log.info("保存目录-数据元关联 - dto: {}", dto);

        if (dto.getBaseDataelementDataid() == null || dto.getBaseDataelementDataid().isEmpty()) {
            throw new IllegalArgumentException("数据元ID不能为空");
        }
        if (dto.getRelations() == null || dto.getRelations().isEmpty()) {
            throw new IllegalArgumentException("关联关系列表不能为空");
        }

        BaseDataElement dataElement = baseDataElementMapper.selectById(dto.getBaseDataelementDataid());
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }

        for (CatalogRelationDto relation : dto.getRelations()) {
            DataElementCatalogRelation existingRelation = dataElementCatalogRelationMapper.selectRelationByDataElementAndItem(
                    dto.getBaseDataelementDataid(), relation.getCatalogitemid());

            if (existingRelation != null) {
                // 更新逻辑
                existingRelation.setCatalogName(relation.getCatalogName());
                existingRelation.setCatalogDesc(relation.getCatalogDescription());
                existingRelation.setInfoItemName(relation.getDataItemName());
                existingRelation.setInfoItemDatatype(relation.getDataType());
                dataElementCatalogRelationMapper.updateById(existingRelation);
            } else {
                // 插入逻辑
                DataElementCatalogRelation newRelation = new DataElementCatalogRelation();
                newRelation.setDataid(UUID.randomUUID().toString());
                newRelation.setDataElementId(dto.getBaseDataelementDataid());
                newRelation.setCatalogId(relation.getCatalogId());
                newRelation.setInfoItemId(relation.getCatalogitemid());
                newRelation.setCatalogName(relation.getCatalogName());
                newRelation.setCatalogDesc(relation.getCatalogDescription());
                newRelation.setInfoItemName(relation.getDataItemName());
                newRelation.setInfoItemDatatype(relation.getDataType());
                newRelation.setCatalogUnitCode(relation.getSourceOrgCode());
                newRelation.setCatalogUnitName(relation.getSourceOrgName());
                dataElementCatalogRelationMapper.insert(newRelation);
            }
        }
    }

    @Override
    public List<AssociatedCatalogVo> getAssociatedCatalogs(String dataid, String sourceOrgCode) {
        log.info("查询目录-数据元关联列表 - dataid: {}, sourceOrgCode: {}", dataid, sourceOrgCode);

        BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }

        List<DataElementCatalogRelation> relations = dataElementCatalogRelationMapper.selectAssociatedCatalogs(dataid, sourceOrgCode);
        List<AssociatedCatalogVo> result = new ArrayList<>();
        for (DataElementCatalogRelation relation : relations) {
            AssociatedCatalogVo vo = new AssociatedCatalogVo();
            vo.setRelationid(relation.getDataid());
            vo.setCatalogId(relation.getCatalogId());
            vo.setCatalogName(relation.getCatalogName());
            vo.setCatalogDescription(relation.getCatalogDesc());
            vo.setCatalogitemid(relation.getInfoItemId());
            vo.setDataItemName(relation.getInfoItemName());
            vo.setDataType(relation.getInfoItemDatatype());
            vo.setSourceOrgCode(relation.getCatalogUnitCode());
            vo.setSourceOrgName(relation.getCatalogUnitName());
            
            // 生成目录预览URL
            try {
                String previewUrl = basecatalogService.getCatalogPreviewUrl(
                    relation.getCatalogId(), 
                    relation.getCatalogUnitCode(), 
                    relation.getCatalogName()
                );
                vo.setPreviewUrl(previewUrl);
            } catch (Exception e) {
                log.warn("生成目录预览URL失败，catalogId: {}, error: {}", relation.getCatalogId(), e.getMessage());
                vo.setPreviewUrl("");
            }
            
            result.add(vo);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAssociation(String relationid) {
        log.info("取消关联 - relationid: {}", relationid);

        DataElementCatalogRelation relation = dataElementCatalogRelationMapper.selectById(relationid);
        if (relation == null) {
            throw new IllegalArgumentException("关联关系不存在");
        }

        int deleteCount = dataElementCatalogRelationMapper.deleteById(relationid);
        if (deleteCount == 0) {
            throw new RuntimeException("删除关联关系失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadFile(MultipartFile file, String dataid, String filetype) {
        log.info("上传文件 - dataid: {}, filetype: {}, fileName: {}", dataid, filetype, file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (dataid == null || dataid.isEmpty()) {
            throw new IllegalArgumentException("数据元ID不能为空");
        }

        BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }

        // 文件格式校验(仅针对standardfile)
        if ("standardfile".equals(filetype)) {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("标准规范文件必须为PDF格式");
            }
        }

        // 调用CommonService上传文件
        String fileLocation = commonService.uploadFile(file);

        // 插入附件表
        DataElementAttachment attachment = new DataElementAttachment();
        attachment.setAttachfileid(UUID.randomUUID().toString());
        attachment.setBaseDataelementDataid(dataid);
        attachment.setAttachfiletype(filetype);
        attachment.setAttachfilename(file.getOriginalFilename());
        attachment.setAttachfileurl(fileLocation);
        attachment.setAttachfilelocation(fileLocation);
        dataElementAttachmentMapper.insert(attachment);

    }

    @Override
    public List<AttachmentFileVo> getFileList(String dataid, String filetype) {
        log.info("查询文件列表 - dataid: {}, filetype: {}", dataid, filetype);

        BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }

        List<DataElementAttachment> attachments = dataElementAttachmentMapper.selectByDataElementIdAndType(dataid, filetype);
        List<AttachmentFileVo> result = new ArrayList<>();
        for (DataElementAttachment attachment : attachments) {
            AttachmentFileVo vo = new AttachmentFileVo();
            vo.setAttachFileId(attachment.getAttachfileid());
            vo.setAttachFileName(attachment.getAttachfilename());
            vo.setAttachFileLocation(attachment.getAttachfilelocation());
            vo.setAttachFileUrl(attachment.getAttachfileurl());
            
            // 获取文件内容
            try {
                byte[] fileContent = commonService.downloadFile(attachment.getAttachfilelocation());
                vo.setFileContent(fileContent);
                log.info("成功获取文件内容 - fileName: {}, size: {}", attachment.getAttachfilename(), fileContent.length);
            } catch (Exception e) {
                log.warn("获取文件内容失败 - fileName: {}, location: {}, error: {}", 
                    attachment.getAttachfilename(), attachment.getAttachfilelocation(), e.getMessage());
                vo.setFileContent(null);
            }
            
            result.add(vo);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String attachFileId) {
        log.info("删除文件 - attachFileId: {}", attachFileId);

        DataElementAttachment attachment = dataElementAttachmentMapper.selectById(attachFileId);
        if (attachment == null) {
            throw new IllegalArgumentException("附件不存在");
        }

        // 注意：通常只删除数据库记录，物理文件由定时任务清理
        // 如果需要立即删除物理文件，可以通过文件路径手动删除

        int deleteCount = dataElementAttachmentMapper.deleteById(attachFileId);
        if (deleteCount == 0) {
            throw new RuntimeException("删除附件记录失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStandard(SaveStandardDto dto) {
        log.info("保存编制信息 - dto: {}", dto);
        // 保存基本信息、联系人信息和属性信息
        this.saveBasicInfo(dto);

        // 保存关联目录信息到数据库
        if (dto.getAssociatedCatalogs() != null && !dto.getAssociatedCatalogs().isEmpty()) {
            log.info("保存关联目录信息，共{}条", dto.getAssociatedCatalogs().size());
            
            // 先删除原有的关联关系
            dataElementCatalogRelationMapper.deleteCatalogAssociation(dto.getDataid());
            
            // 批量插入新的关联关系
            for (CatalogRelationDto catalogDto : dto.getAssociatedCatalogs()) {
                // 创建新的关联关系
                DataElementCatalogRelation newRelation = new DataElementCatalogRelation();
                newRelation.setDataid(UUID.randomUUID().toString());
                newRelation.setDataElementId(dto.getDataid());
                newRelation.setInfoItemId(catalogDto.getCatalogitemid());
                newRelation.setCatalogId(catalogDto.getCatalogId());
                newRelation.setCatalogName(catalogDto.getCatalogName());
                newRelation.setInfoItemName(catalogDto.getDataItemName());
                newRelation.setCatalogUnitCode(catalogDto.getSourceOrgCode());
                newRelation.setCatalogUnitName(catalogDto.getSourceOrgName());
                newRelation.setCreateDate(new Date());
                dataElementCatalogRelationMapper.insert(newRelation);
            }
        }
        
        log.info("保存完成 - 关联目录已更新到数据库，基本信息等待提交时更新");
    }

    /**
     * 保存基本信息、联系人信息和属性信息
     */
    private void saveBasicInfo(SaveStandardDto dto) {
        log.info("保存基本信息、联系人信息和属性信息 - dto: {}", dto);
        // 参数校验
        if (dto.getDataelementcode() == null || dto.getDataelementcode().isEmpty()) {
            throw new IllegalArgumentException("数据元编码不能为空");
        }
        if (dto.getDatatype() == null || dto.getDatatype().isEmpty()) {
            throw new IllegalArgumentException("数据类型不能为空");
        }

        // 数据元编码格式校验
        String code = dto.getDataelementcode();
        if (!CODE_PATTERN.matcher(code).matches()) {
            throw new IllegalArgumentException("该项需为英文字母或阿拉伯数字");
        }

        // 查询并校验数据元
        BaseDataElement dataElement = baseDataElementMapper.selectById(dto.getDataid());
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }

        // 获取当前登录用户信息
        log.debug("正在获取当前登录用户信息...");
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        
        // 更新基本信息
        dataElement.setDataElementId(dto.getDataelementcode());
        dataElement.setDefinition(dto.getDefinition());
        dataElement.setDatatype(dto.getDatatype());
        dataElement.setDataFormat(dto.getDataFormat());
        dataElement.setValueDomain(dto.getValueDomain());
        dataElement.setRemarks(dto.getRemarks());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 保存/更新联系人信息
        if (dto.getContactInfo() != null) {
            if (dto.getContactInfo().getContactName() == null || dto.getContactInfo().getContactName().isEmpty()) {
                throw new IllegalArgumentException("联系人姓名不能为空");
            }
            if (dto.getContactInfo().getContactTel() == null || dto.getContactInfo().getContactTel().isEmpty()) {
                throw new IllegalArgumentException("联系人电话不能为空");
            }

            DataElementContact existingContact = dataElementContactMapper.selectByDataElementId(dto.getDataid());

            if (existingContact != null) {
                // 更新逻辑
                existingContact.setContactname(dto.getContactInfo().getContactName());
                existingContact.setContacttel(dto.getContactInfo().getContactTel());
                dataElementContactMapper.updateById(existingContact);
            } else {
                // 插入逻辑
                DataElementContact newContact = new DataElementContact();
                newContact.setDataid(UUID.randomUUID().toString());
                newContact.setBaseDataelementDataid(dto.getDataid());
                newContact.setContactname(dto.getContactInfo().getContactName());
                newContact.setContacttel(dto.getContactInfo().getContactTel());
                dataElementContactMapper.insert(newContact);
            }
        }

        // 保存属性信息
        if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
            // 先删除原有属性
            dataElementAttributeMapper.deleteByDataElementId(dto.getDataid());

            // 批量插入新属性
            for (AttributeDto attrDto : dto.getAttributes()) {
                if (attrDto.getAttributeName() == null || attrDto.getAttributeName().isEmpty()) {
                    throw new IllegalArgumentException("属性名称不能为空");
                }
                if (attrDto.getAttributeValue() == null || attrDto.getAttributeValue().isEmpty()) {
                    throw new IllegalArgumentException("属性值不能为空");
                }

                DataElementAttribute attribute = new DataElementAttribute();
                attribute.setDataid(UUID.randomUUID().toString());
                attribute.setBaseDataelementDataid(dto.getDataid());
                attribute.setAttributename(attrDto.getAttributeName());
                attribute.setAttributevalue(attrDto.getAttributeValue());
                dataElementAttributeMapper.insert(attribute);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubmitResultVo submitStandard(SaveStandardDto dto) {
        log.info("提交审核 - dto: {}", dto);

        // 保存基本信息、联系人信息和属性信息
        this.saveBasicInfo(dto);

        // 查询并校验数据元状态
        BaseDataElement dataElement = baseDataElementMapper.selectById(dto.getDataid());
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        if (!"designated_source".equals(dataElement.getStatus()) && !"TodoRevised".equals(dataElement.getStatus()))
        {
            throw new IllegalArgumentException("当前状态不允许提交，必须为待定标状态");
        }

        // 计算下一状态 - 将designated_source映射为TodoDetermined用于查询流程配置
        // String flowStatus = mapStatusForFlow(dataElement.getStatus());
        //取消映射
        String flowStatus = dataElement.getStatus();
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(flowStatus, "提交审核");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 获取当前登录用户信息
        log.debug("正在获取当前登录用户信息...");
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 更新数据元状态
        Date date = new Date();
        dataElement.setStatus(nextStatus);
        dataElement.setLastModifyDate(date);
        dataElement.setLastModifyAccount(userInfo.getAccount());
        dataElement.setLastSubmitDate(date);
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dto.getDataid());
        processRecordDto.setOperation("提交审核");
        processRecordDto.setSourceStatus(flowStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        flowProcessService.recordProcessHistory(processRecordDto);

        // 构建返回结果
        SubmitResultVo result = new SubmitResultVo();
        result.setNextStatus(nextStatus);

        return result;
    }


    @Override
    public Page<StandardDataElementPageInfoVo> getAllStandardListPage(StandardDataElementPageQueryDto queryDto) {
        log.info("获取编制标准完整信息 - queryDto: {}", queryDto);

        // 处理日期边界问题
        normalizeDateRange(queryDto);

        // 校验并规范化排序参数
        validateAndNormalizeSortParams(queryDto);

        // 创建分页对象
        Page<StandardDataElementPageInfoVo> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());

        // 获取当前登录信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 执行查询
        List<StandardDataElementPageInfoVo> records = dataElementStandardMapper.getAllStandardList(page, queryDto, userOrgCode);
        
        // 设置状态描述
        for (StandardDataElementPageInfoVo record : records) {
            record.setStatusDesc(CalibrationStatusEnums.getDescByCode(record.getStatus()));
        }
        
        page.setRecords(records);
        return page;
    }

    @Override
    public RevisionComment getRevisionCommentbydataid(String dataid) {
        log.info("获取修订意见 - dataid: {}", dataid);
        
        if (dataid == null || dataid.isEmpty()) {
            throw new IllegalArgumentException("数据元ID不能为空");
        }
        
        BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        
        return revisionCommentMapper.selectbydataid(dataid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubmitResultVo submitReExamination(SaveStandardDto dto) {
        log.info("提交复审 - dto: {}", dto);

        // 保存基本信息、联系人信息和属性信息
        this.saveBasicInfo(dto);

        // 查询并校验数据元状态
        BaseDataElement dataElement = baseDataElementMapper.selectById(dto.getDataid());
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        if (!"TodoRevised".equals(dataElement.getStatus())) {
            throw new IllegalArgumentException("当前状态不允许提交复审，必须为待修订状态");
        }

        // 计算下一状态
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus("TodoRevised", "提交复审");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 获取当前登录用户信息
        log.debug("正在获取当前登录用户信息...");
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        
        // 更新数据元状态
        dataElement.setStatus(nextStatus);
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dto.getDataid());
        processRecordDto.setOperation("提交复审");
        processRecordDto.setSourceStatus("TodoRevised");
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        flowProcessService.recordProcessHistory(processRecordDto);

        // 构建返回结果
        SubmitResultVo result = new SubmitResultVo();
        result.setNextStatus(nextStatus);

        return result;
    }

    @Override
    public void exportTodoDetermineList(StandardDataElementPageQueryDto queryDto, javax.servlet.http.HttpServletResponse response) {
        log.info("导出待定标数据元列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportTodoDetermineDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            StandardDataElementPageInfoVo vo = exportList.get(i);
            ExportTodoDetermineDTO dto = new ExportTodoDetermineDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "待定标数据元列表", ExportTodoDetermineDTO.class);
        } catch (Exception e) {
            log.error("导出待定标数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportTodoRevisedList(StandardDataElementPageQueryDto queryDto, javax.servlet.http.HttpServletResponse response) {
        log.info("导出待修订数据元列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportTodoRevisedDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            StandardDataElementPageInfoVo vo = exportList.get(i);
            ExportTodoRevisedDTO dto = new ExportTodoRevisedDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataelementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setRevisionCreateDate(vo.getRevisionCreateDate());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "待修订数据元列表", ExportTodoRevisedDTO.class);
        } catch (Exception e) {
            log.error("导出待修订数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportSourcedoneStandardList(StandardDataElementPageQueryDto queryDto, javax.servlet.http.HttpServletResponse response) {
        log.info("导出定标阶段已处理数据元列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);
        
        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportSourcedoneStandardDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            StandardDataElementPageInfoVo vo = exportList.get(i);
            ExportSourcedoneStandardDTO dto = new ExportSourcedoneStandardDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataelementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishDate(vo.getPublishDate());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "定标阶段已处理数据元列表", ExportSourcedoneStandardDTO.class);
        } catch (Exception e) {
            log.error("导出定标阶段已处理数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 处理日期边界问题
     */
    private void normalizeDateRange(StandardDataElementPageQueryDto queryDto) {
        if (queryDto.getConfirmDateBegin() != null) {
            // 开始时间设置为当天00:00:00.000
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getConfirmDateBegin());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            queryDto.setConfirmDateBegin(cal.getTime());
        }
        if (queryDto.getConfirmDateEnd() != null) {
            // 结束时间设置为当天23:59:59.999
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getConfirmDateEnd());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            queryDto.setConfirmDateEnd(cal.getTime());
        }
        // 类似处理其他日期字段
        if (queryDto.getPublishDateBegin() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getPublishDateBegin());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            queryDto.setPublishDateBegin(cal.getTime());
        }
        if (queryDto.getPublishDateEnd() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getPublishDateEnd());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            queryDto.setPublishDateEnd(cal.getTime());
        }
        if (queryDto.getRevisionCreateDateBegin() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getRevisionCreateDateBegin());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            queryDto.setRevisionCreateDateBegin(cal.getTime());
        }
        if (queryDto.getRevisionCreateDateEnd() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getRevisionCreateDateEnd());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            queryDto.setRevisionCreateDateEnd(cal.getTime());
        }
    }

    /**
     * 校验并规范化排序参数
     */
    private void validateAndNormalizeSortParams(StandardDataElementPageQueryDto queryDto) {
        // 这里可以根据实际需要校验排序字段是否在允许的枚举中
        // 如果需要严格校验，可以创建一个排序字段枚举类
        if (queryDto.getSortField() == null || queryDto.getSortField().isEmpty()) {
            queryDto.setSortField("create_date");
            queryDto.setSortOrder("desc");
        } else {
            // 规范化排序方向
            if (queryDto.getSortOrder() == null || queryDto.getSortOrder().isEmpty()) {
                queryDto.setSortOrder("desc");
            } else {
                String order = queryDto.getSortOrder().toLowerCase();
                if (!"asc".equals(order) && !"desc".equals(order)) {
                    queryDto.setSortOrder("desc");
                }
            }
        }
    }

    /**
     * 将数据元状态映射为流程配置中使用的状态
     */
    private String mapStatusForFlow(String status) {
        // designated_source 映射为 TodoDetermined，用于查询流程配置
        if ("designated_source".equals(status)) {
            return "TodoDetermined";
        }
        // 其他状态保持不变
        return status;
    }
    
    /**
     * 映射驳回节点名称
     */
    private String mapRejectionNode(String sourceActivityName) {
        if (sourceActivityName == null) {
            return "未知节点";
        }
        
        switch (sourceActivityName) {
            case "TodoDetermined":
            case "designated_source":
                return "制定数据元标准";
            case "PendingReview":
                return "审核数据元标准";
            case "SolicitingOpinions":
                return "征求意见";
            case "TodoRevised":
                return "修订数据元标准";
            case "PendingReExamination":
                return "复审数据元标准";
            case "Todoreleased":
                return "发布数据源标准";
            default:
                return "未知节点";
        }
    }

    @Override
    public Page<AuditDataElementVo> auditDataElementList(AuditDataElementQueryDto queryDto) {
        log.info("执行审核标准模块数据元查询 - queryDto: {}", queryDto);
        
        // 规范化日期范围
        normalizeAuditDateRange(queryDto);
        
        // 校验并规范化排序参数
        validateAndNormalizeAuditSortParams(queryDto);
        
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 创建分页对象
        Page<AuditDataElementVo> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        
        // 执行分页查询
        Page<AuditDataElementVo> resultPage = dataElementStandardMapper.queryAuditDataElementListPage(page, queryDto, userOrgCode);
        
        // 补充状态描述
        for (AuditDataElementVo vo : resultPage.getRecords()) {
            vo.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
        }
        
        log.info("查询完成，共返回{}条记录，总数：{}", resultPage.getRecords().size(), resultPage.getTotal());
        return resultPage;
    }

    /**
     * 处理审核查询的日期边界问题
     */
    private void normalizeAuditDateRange(AuditDataElementQueryDto queryDto) {
        // 处理提交时间
        normalizeDateField(queryDto.getSubmitTimeBegin(), queryDto::setSubmitTimeBegin, true);
        normalizeDateField(queryDto.getSubmitTimeEnd(), queryDto::setSubmitTimeEnd, false);
        
        // 处理审核时间
        normalizeDateField(queryDto.getApproveTimeBegin(), queryDto::setApproveTimeBegin, true);
        normalizeDateField(queryDto.getApproveTimeEnd(), queryDto::setApproveTimeEnd, false);
        
        // 处理修订提交时间
        normalizeDateField(queryDto.getRevisionSubmitTimeBegin(), queryDto::setRevisionSubmitTimeBegin, true);
        normalizeDateField(queryDto.getRevisionSubmitTimeEnd(), queryDto::setRevisionSubmitTimeEnd, false);
        
        // 处理报送时间
        normalizeDateField(queryDto.getReportTimeBegin(), queryDto::setReportTimeBegin, true);
        normalizeDateField(queryDto.getReportTimeEnd(), queryDto::setReportTimeEnd, false);
        
        // 处理审核驳回时间
        normalizeDateField(queryDto.getRejectTimeBegin(), queryDto::setRejectTimeBegin, true);
        normalizeDateField(queryDto.getRejectTimeEnd(), queryDto::setRejectTimeEnd, false);
        
        // 处理发起修订时间
        normalizeDateField(queryDto.getInitiateRevisionTimeBegin(), queryDto::setInitiateRevisionTimeBegin, true);
        normalizeDateField(queryDto.getInitiateRevisionTimeEnd(), queryDto::setInitiateRevisionTimeEnd, false);
        
        // 处理定源时间
        normalizeDateField(queryDto.getSourceTimeBegin(), queryDto::setSourceTimeBegin, true);
        normalizeDateField(queryDto.getSourceTimeEnd(), queryDto::setSourceTimeEnd, false);
    }
    
    /**
     * 标准化单个日期字段
     * @param date 原始日期
     * @param setter 日期设置器
     * @param isBeginTime 是否为开始时间（true：设为00:00:00.000，false：设为23:59:59.999）
     */
    private void normalizeDateField(Date date, java.util.function.Consumer<Date> setter, boolean isBeginTime) {
        if (date != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            if (isBeginTime) {
                // 开始时间设置为当天00:00:00.000
                cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);
                cal.set(java.util.Calendar.MILLISECOND, 0);
            } else {
                // 结束时间设置为当天23:59:59.999
                cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
                cal.set(java.util.Calendar.MINUTE, 59);
                cal.set(java.util.Calendar.SECOND, 59);
                cal.set(java.util.Calendar.MILLISECOND, 999);
            }
            setter.accept(cal.getTime());
        }
    }

    /**
     * 校验并规范化审核查询的排序参数
     */
    private void validateAndNormalizeAuditSortParams(AuditDataElementQueryDto queryDto) {
        if (queryDto.getSortField() == null || queryDto.getSortField().isEmpty()) {
            queryDto.setSortField("submitTime");
            queryDto.setSortOrder("desc");
        } else {
            // 规范化排序方向
            if (queryDto.getSortOrder() == null || queryDto.getSortOrder().isEmpty()) {
                queryDto.setSortOrder("desc");
            } else {
                String order = queryDto.getSortOrder().toLowerCase();
                if (!"asc".equals(order) && !"desc".equals(order)) {
                    queryDto.setSortOrder("desc");
                }
            }
        }
    }

    @Override
    public StandardOperationResultVo appvoveStandard(ApproveInfoDTO approveDTO) {
        log.info("审核标准 - approveDTO: {}", approveDTO);
        
        if (approveDTO.getList() == null || approveDTO.getList().isEmpty()) {
            throw new IllegalArgumentException("数据元ID列表不能为空");
        }

        // 驳回时必须提供意见
        if ("驳回".equals(approveDTO.getUseroperation()) &&
            (approveDTO.getUsersuggestion() == null || approveDTO.getUsersuggestion().trim().isEmpty())) {
            throw new IllegalArgumentException("驳回意见不能为空");
        }

        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorDetails = new StringBuilder();

        for (String dataid : approveDTO.getList()) {
            try {
                // 验证数据元是否存在
                BaseDataElement dataElement = dataElementStandardMapper.selectById(dataid);
                if (dataElement == null) {
                    errorCount++;
                    errorDetails.append("数据元[").append(dataid).append("]不存在；");
                    continue;
                }

                // 根据审核操作类型处理
                if ("审核通过".equals(approveDTO.getUseroperation())) {
                    // 处理审核通过逻辑
                    processApprovalPass(dataElement, dataid, approveDTO.getUsersuggestion());
                    successCount++;
                } else if ("驳回".equals(approveDTO.getUseroperation())) {
                    // 处理驳回逻辑
                    processApprovalReject(dataElement, dataid, approveDTO.getUsersuggestion());
                    successCount++;
                } else {
                    errorCount++;
                    errorDetails.append("数据元[").append(dataid).append("]操作类型无效；");
                }

            } catch (Exception e) {
                errorCount++;
                errorDetails.append("数据元[").append(dataid).append("]处理失败：").append(e.getMessage()).append("；");
                log.error("审核数据元[{}]时发生异常", dataid, e);
            }
        }

        // 构建返回结果
        StandardOperationResultVo result = new StandardOperationResultVo();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        result.setTotalCount(approveDTO.getList().size());
        result.setOperationType(approveDTO.getUseroperation());
        result.setIsSuccess(errorCount == 0);

        String resultMessage = String.format("审核完成：成功%d条，失败%d条", successCount, errorCount);
        result.setResultMessage(resultMessage);

        if (errorCount > 0) {
            result.setErrorDetails(errorDetails.toString());
        }

        log.info("审核结果：{}", resultMessage);
        return result;
    }

    private void processApprovalPass(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 计算下一状态
        String originalStatus = dataElement.getStatus();
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(originalStatus, "审核通过");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 更新数据元状态和审核时间
        dataElement.setStatus(nextStatus);
        dataElement.setLastApproveDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史到process_record表
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("审核通过");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion); // 审核意见写入process_record（可以为null）
        processRecordDto.setOperateTime(new Date()); // 确保记录时间，防止时间差
        flowProcessService.recordProcessHistory(processRecordDto);

        log.info("数据元[{}]审核通过，状态变更为：{}", dataid, nextStatus);
    }

    private void processApprovalReject(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 计算下一状态（驳回）
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(dataElement.getStatus(), "驳回");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 更新数据元状态和审核时间
        String originalStatus = dataElement.getStatus();
        dataElement.setStatus(nextStatus);
        dataElement.setLastApproveDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史到process_record表（驳回意见写入usersuggestion字段）
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("驳回");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion); // 驳回意见写入process_record的usersuggestion字段
        processRecordDto.setOperateTime(new Date()); // 确保记录时间，防止时间差
        flowProcessService.recordProcessHistory(processRecordDto);

        log.info("数据元[{}]已驳回，状态变更为：{}，驳回意见：{}", dataid, nextStatus, usersuggestion);
    }

    @Override
    public void exportPendingReviewList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待审核列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList == null) {
            exportList = new ArrayList<>();
        }

        // 数据转换
        List<ExportPendingReviewDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            AuditDataElementVo vo = exportList.get(i);
            ExportPendingReviewDTO dto = new ExportPendingReviewDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setLastSubmitDate(vo.getLastSubmitDate());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "待审核数据元列表", ExportPendingReviewDTO.class);
        } catch (Exception e) {
            log.error("导出待审核数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportSolicitingOpinionsList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出征求意见列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportSolicitingOpinionsDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            AuditDataElementVo vo = exportList.get(i);
            ExportSolicitingOpinionsDTO dto = new ExportSolicitingOpinionsDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setLastSubmitDate(vo.getLastSubmitDate());
            dto.setLastApproveDate(vo.getLastApproveDate());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "征求意见数据元列表", ExportSolicitingOpinionsDTO.class);
        } catch (Exception e) {
            log.error("导出征求意见数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportPendingReExaminationList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待复审列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportPendingReExaminationDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            AuditDataElementVo vo = exportList.get(i);
            ExportPendingReExaminationDTO dto = new ExportPendingReExaminationDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setLastSubmitReexaminationDate(vo.getLastSubmitReexaminationDate());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "待复审数据元列表", ExportPendingReExaminationDTO.class);
        } catch (Exception e) {
            log.error("导出待复审数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportTodoReleasedList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待发布列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportTodoReleasedDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            AuditDataElementVo vo = exportList.get(i);
            ExportTodoReleasedDTO dto = new ExportTodoReleasedDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setLastSubmitReleasedDate(vo.getLastSubmitReleasedDate());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "待发布数据元列表", ExportTodoReleasedDTO.class);
        } catch (Exception e) {
            log.error("导出待发布数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportDoneOrganizerList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方已处理列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportDoneOrganizerDTO> exportData = new ArrayList<>();
        for (int i = 0; i < exportList.size(); i++) {
            AuditDataElementVo vo = exportList.get(i);
            ExportDoneOrganizerDTO dto = new ExportDoneOrganizerDTO();
            dto.setSerialNumber(String.valueOf(i + 1));
            dto.setName(vo.getName());
            dto.setDataElementId(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setLastApproveDate(vo.getLastApproveDate());
            dto.setLastInitiateRevisedDate(vo.getLastInitiateRevisedDate());
            dto.setPublishDate(vo.getPublishDate());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方已处理数据元列表", ExportDoneOrganizerDTO.class);
        } catch (Exception e) {
            log.error("导出组织方已处理数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 查询流程记录列表
     * 只有一个dataid参数，先去base_data_element [基准数据元表]查询数据元状态，
     * 去flow_activity_definition [节点配置表]中查出对应的状态+小于当前状态所有的，
     * 再用dataid去process_record [流程记录表]查询，保留当前+小于保留的流程，list[processVO]返回给前端
     * @param dataid 数据元ID
     * @return ProcessVO列表
     */
    private List<ProcessVO> getProcessRecords(String dataid) {
        log.info("查询流程记录 - dataid: {}", dataid);

        List<ProcessVO> result = new ArrayList<>();

        try {
            // 1. 先去base_data_element [基准数据元表]查询数据元状态
            BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
            if (dataElement == null) {
                log.warn("数据元不存在 - dataid: {}", dataid);
                return result;
            }
            String currentStatus = dataElement.getStatus();
            log.info("查询到数据元状态: {}", currentStatus);

            // 2. 查询flow_activity_definition表，获取所有节点并按顺序排序（不使用flowid过滤）
            List<FlowActivityDefinition> allActivities = flowActivityDefinitionMapper.selectAll();

            // 3. 将数据元状态直接当做activityname，找到当前状态对应的activityorder
            Integer currentOrder = null;
            for (FlowActivityDefinition activity : allActivities) {
                if (currentStatus.equals(activity.getActivityname())) {
                    currentOrder = activity.getActivityorder();
                    break;
                }
            }

            if (currentOrder == null) {
                log.warn("未找到当前状态[{}]对应的流程节点", currentStatus);
                return result;
            }

            // 4. 获取当前状态及小于当前状态的所有节点名称
            Set<String> allowedActivityNames = new HashSet<>();
            for (FlowActivityDefinition activity : allActivities) {
                if (activity.getActivityorder() != null && activity.getActivityorder() <= currentOrder) {
                    allowedActivityNames.add(activity.getActivityname());
                }
            }

            log.info("当前状态[{}]，当前order[{}]，允许的流程节点: {}", currentStatus, currentOrder, allowedActivityNames);

            // 5. 查询process_record表中的所有记录
            List<ProcessRecord> dbRecords = processRecordMapper.getProcessRecordsByDataId(dataid);

            // 6. 过滤出目标状态在允许列表中的记录，并转换为ProcessVO
            for (ProcessRecord record : dbRecords) {
                if (record.getDestactivityname() != null && allowedActivityNames.contains(record.getDestactivityname())) {
                    ProcessVO processVO = new ProcessVO();
                    processVO.setSourceactivityname(record.getSourceactivityname());
                    processVO.setProcessusername(record.getProcessusername());
                    processVO.setProcessunitname(record.getProcessunitname());
                    processVO.setProcessdatetime(record.getProcessdatetime());
                    result.add(processVO);
                }
            }

            log.info("查询到{}条符合条件的流程记录", result.size());

        } catch (Exception e) {
            log.error("查询流程记录失败 - dataid: {}", dataid, e);
        }

        return result;
    }

    /**
     * 将数据元状态映射为flow_activity_definition表中的activityname
     * @param status 数据元状态
     * @return activityname
     */
    private String mapStatusToActivityName(String status) {
        switch (status) {
            case "designated_source":
                return "待定标";
            case "PendingReview":
                return "待审核";
            case "SolicitingOpinions":
                return "征求意见";
            case "TodoRevised":
                return "待修订";
            case "PendingReExamination":
                return "待复审";
            case "Todoreleased":
                return "待发布";
            case "Published":
                return "已发布";
            default:
                return status;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StandardOperationResultVo reExaminationStandard(ReExaminationDataElementDTO reExaminationDTO) {
        log.info("复审操作 - reExaminationDTO: {}", reExaminationDTO);

        if (reExaminationDTO.getDataid() == null || reExaminationDTO.getDataid().isEmpty()) {
            throw new IllegalArgumentException("数据元ID列表不能为空");
        }

        // 驳回时必须提供意见
        if ("驳回".equals(reExaminationDTO.getUseroperation()) &&
            (reExaminationDTO.getUsersuggestion() == null || reExaminationDTO.getUsersuggestion().trim().isEmpty())) {
            throw new IllegalArgumentException("驳回意见不能为空");
        }

        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorDetails = new StringBuilder();

        for (String dataid : reExaminationDTO.getDataid()) {
            try {
                // 验证数据元是否存在
                BaseDataElement dataElement = baseDataElementMapper.selectById(dataid);
                if (dataElement == null) {
                    errorCount++;
                    errorDetails.append("数据元[").append(dataid).append("]不存在；");
                    continue;
                }
                
                // 根据复审操作类型处理
                if ("报送通过".equals(reExaminationDTO.getUseroperation())) {
                    // 处理复审通过逻辑
                    processReExaminationPass(dataElement, dataid, reExaminationDTO.getUsersuggestion());
                    successCount++;
                } else if ("驳回".equals(reExaminationDTO.getUseroperation())) {
                    // 处理驳回逻辑
                    processReExaminationReject(dataElement, dataid, reExaminationDTO.getUsersuggestion());
                    successCount++;
                } else {
                    errorCount++;
                    errorDetails.append("数据元[").append(dataid).append("]操作类型无效；");
                }
                
            } catch (Exception e) {
                errorCount++;
                errorDetails.append("数据元[").append(dataid).append("]处理失败：").append(e.getMessage()).append("；");
                log.error("复审数据元[{}]时发生异常", dataid, e);
            }
        }
        
        // 构建返回结果
        StandardOperationResultVo result = new StandardOperationResultVo();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        result.setTotalCount(reExaminationDTO.getDataid().size());
        result.setOperationType(reExaminationDTO.getUseroperation());
        result.setIsSuccess(errorCount == 0);
        
        String resultMessage = String.format("复审完成：成功%d条，失败%d条", successCount, errorCount);
        result.setResultMessage(resultMessage);
        
        if (errorCount > 0) {
            result.setErrorDetails(errorDetails.toString());
        }
        
        log.info("复审结果：{}", resultMessage);
        return result;
    }
    
    private void processReExaminationPass(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        
        // 计算下一状态
        String originalStatus = dataElement.getStatus();
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(originalStatus, "报送领导审阅");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();
        
        // 更新数据元状态和复审时间
        dataElement.setStatus(nextStatus);
        dataElement.setLastSubmitreleasedDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);
        
        // 记录流程历史到process_record表
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("报送通过");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion); // 复审意见写入process_record（可以为null）
        processRecordDto.setOperateTime(new Date()); // 确保记录时间，防止时间差
        flowProcessService.recordProcessHistory(processRecordDto);
        
        log.info("数据元[{}]复审通过，状态变更为：{}", dataid, nextStatus);
    }
    
    private void processReExaminationReject(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        
        // 计算下一状态（驳回）
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(dataElement.getStatus(), "复审驳回");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();
        
        // 更新数据元状态和复审时间
        String originalStatus = dataElement.getStatus();
        dataElement.setStatus(nextStatus);
        dataElement.setLastSubmitreleasedDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);
        
        // 记录流程历史到process_record表（驳回意见写入usersuggestion字段）
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("驳回");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion); // 驳回意见写入process_record的usersuggestion字段
        processRecordDto.setOperateTime(new Date()); // 确保记录时间，防止时间差
        flowProcessService.recordProcessHistory(processRecordDto);
        
        log.info("数据元[{}]复审驳回，状态变更为：{}，驳回意见：{}", dataid, nextStatus, usersuggestion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StandardOperationResultVo initiateRevision(RevisionInfoDTO revisionDTO) {
        log.info("发起修订操作 - revisionDTO: {}", revisionDTO);
        
        if (revisionDTO.getList() == null || revisionDTO.getList().isEmpty()) {
            throw new IllegalArgumentException("数据元ID列表不能为空");
        }

        int successCount = 0;
        int errorCount = 0;
        List<StandardOperationResultVo.FailureItem> failureItems = new ArrayList<>();
        List<RevisionComment> revisionComments = new ArrayList<>();

        // 查询所有数据元信息，用于验证存在性
        List<BaseDataElement> dataElements = dataElementStandardMapper.queryDataElementsByIds(revisionDTO.getList());
        Map<String, BaseDataElement> dataElementMap = new HashMap<>();
        for (BaseDataElement element : dataElements) {
            dataElementMap.put(element.getDataid(), element);
        }

        for (String dataid : revisionDTO.getList()) {
            // 验证数据元是否存在
            BaseDataElement dataElement = dataElementMap.get(dataid);
            if (dataElement == null) {
                errorCount++;
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName("未知");
                failureItem.setSourceUnitName("");
                failureItem.setFailureReason("系统中未找到相关数据元");
                failureItems.add(failureItem);
                continue;
            }

            // 验证修订意见
            if ("发起修订".equals(revisionDTO.getUseroperation()) &&
                (revisionDTO.getUsersuggestion() == null || revisionDTO.getUsersuggestion().trim().isEmpty())) {
                errorCount++;
                // 获取组织单位名称（非必须，仅用于显示）
                String sourceUnitName = "";
                try {
                    OrganizationUnit orgUnit = organizationUnitMapper.selectById(dataElement.getSourceUnitCode());
                    sourceUnitName = orgUnit != null ? orgUnit.getUnitName() : "";
                } catch (Exception e) {
                    // 组织单位获取失败不影响主流程
                }
                
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName(dataElement.getName());
                failureItem.setSourceUnitName(sourceUnitName);
                failureItem.setFailureReason("修订意见为空");
                failureItems.add(failureItem);
                continue;
            }

            try {
                // 根据操作类型处理
                if ("发起修订".equals(revisionDTO.getUseroperation())) {
                    // 处理发起修订逻辑
                    processInitiateRevision(dataElement, dataid, revisionDTO.getUsersuggestion());
                    
                    // 创建修订意见记录
                    RevisionComment revisionComment = createRevisionComment(dataid, revisionDTO.getUsersuggestion());
                    revisionComments.add(revisionComment);
                    
                    successCount++;
                } else if ("报送".equals(revisionDTO.getUseroperation())) {
                    // 处理报送逻辑
                    processSubmitRelease(dataElement, dataid, revisionDTO.getUsersuggestion());
                    successCount++;
                } else {
                    // 操作类型无效，但这不应该发生，因为前端应该控制
                    successCount++;
                }

            } catch (Exception e) {
                errorCount++;
                // 获取组织单位名称（非必须，仅用于显示）
                String sourceUnitName = "";
                try {
                    OrganizationUnit orgUnit = organizationUnitMapper.selectById(dataElement.getSourceUnitCode());
                    sourceUnitName = orgUnit != null ? orgUnit.getUnitName() : "";
                } catch (Exception ex) {
                    // 组织单位获取失败不影响主流程
                }
                
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName(dataElement.getName());
                failureItem.setSourceUnitName(sourceUnitName);
                failureItem.setFailureReason("系统异常：" + e.getMessage());
                failureItems.add(failureItem);
                log.error("发起修订数据元[{}]时发生异常", dataid, e);
            }
        }

        // 批量插入修订意见记录
        if (!revisionComments.isEmpty()) {
            try {
                revisionCommentMapper.batchInsert(revisionComments);
            } catch (Exception e) {
                log.error("批量插入修订意见失败", e);
                throw new RuntimeException("保存修订意见失败：" + e.getMessage());
            }
        }

        // 构建返回结果
        StandardOperationResultVo result = new StandardOperationResultVo();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        result.setTotalCount(revisionDTO.getList().size());
        result.setOperationType(revisionDTO.getUseroperation());
        result.setIsSuccess(errorCount == 0);
        result.setFailureItems(failureItems);
        
        if (errorCount == 0) {
            result.setResultMessage("操作完成，全部成功");
        } else {
            result.setResultMessage("操作完成，成功" + successCount + "条，失败" + errorCount + "条");
        }

        log.info("发起修订操作完成 - 总数：{}，成功：{}，失败：{}", 
                revisionDTO.getList().size(), successCount, errorCount);
        
        return result;
    }

    /**
     * 私有方法 - processInitiateRevision
     * 
     * 用途：处理单个数据元的发起修订逻辑，确保状态从当前状态过渡到待修订状态，
     * 同时更新相关时间戳和修订记录。
     * 
     * 业务逻辑：首先验证当前数据元是否允许发起修订（例如，必须处于可修订的状态，如征求意见）。
     * 然后，计算下一状态，更新数据元的最后发起修订时间、最后修改时间和最后修改人。
     * 创建流程记录，包括操作类型"发起修订"、源状态、目标状态、操作人信息和修订意见。
     * 
     * 状态流转：当前状态（征求意见） → "按意见完善" → 待修订状态。
     * 
     * @param dataElement 数据元实体
     * @param dataid 数据元ID
     * @param usersuggestion 用户修订意见
     */
    private void processInitiateRevision(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        
        String originalStatus = dataElement.getStatus();
        
        // 验证当前数据元是否允许发起修订（必须处于可修订的状态，如征求意见）
        if (!"SolicitingOpinions".equals(originalStatus)) {
            throw new RuntimeException("数据元当前状态为[" + originalStatus + "]，不允许发起修订操作，只有处于[征求意见]状态的数据元才能发起修订");
        }

        // 计算下一状态：当前状态（征求意见） → "按意见完善" → 待修订状态
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(originalStatus, "按意见完善");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效：从[" + originalStatus + "]执行[按意见完善]操作");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 更新数据元的状态和相关时间戳
        dataElement.setStatus(nextStatus);
        dataElement.setLastInitiaterevisedDate(new Date()); // 最后发起修订时间
        dataElement.setLastModifyDate(new Date()); // 最后修改时间
        dataElement.setLastModifyAccount(userInfo.getAccount()); // 最后修改人
        baseDataElementMapper.updateById(dataElement);

        // 创建流程记录，包括操作类型、源状态、目标状态、操作人信息和修订意见
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("发起修订"); // 操作类型
        processRecordDto.setSourceStatus(originalStatus); // 源状态
        processRecordDto.setDestStatus(nextStatus); // 目标状态
        processRecordDto.setOperatorAccount(userInfo.getAccount()); // 操作人账号
        processRecordDto.setOperatorName(userInfo.getName()); // 操作人姓名
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode()); // 操作人组织代码
        processRecordDto.setOperatorUnitName(userInfo.getOrgName()); // 操作人组织名称
        processRecordDto.setUsersuggestion(usersuggestion); // 修订意见
        processRecordDto.setOperateTime(new Date()); // 操作时间
        flowProcessService.recordProcessHistory(processRecordDto);

        log.info("数据元[{}]发起修订成功，状态从[{}]变更为[{}]，修订意见：{}", dataid, originalStatus, nextStatus, usersuggestion);
    }

    private void processSubmitRelease(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 计算下一状态
        String originalStatus = dataElement.getStatus();
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(originalStatus, "报送领导审阅");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 更新数据元状态和报送时间
        dataElement.setStatus(nextStatus);
        dataElement.setLastSubmitreleasedDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史到process_record表
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("报送");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion);
        processRecordDto.setOperateTime(new Date());
        flowProcessService.recordProcessHistory(processRecordDto);

        log.info("数据元[{}]报送，状态变更为：{}", dataid, nextStatus);
    }

    private RevisionComment createRevisionComment(String dataid, String usersuggestion) {
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        Date now = new Date();

        RevisionComment revisionComment = new RevisionComment();
        revisionComment.setId(UUID.randomUUID().toString());
        revisionComment.setBaseDataelementDataid(dataid);
        revisionComment.setComment(usersuggestion);
        revisionComment.setRevisionInitiatorAccount(userInfo.getAccount());
        revisionComment.setRevisionInitiatorName(userInfo.getName());
        revisionComment.setRevisionCreatedate(now);
        revisionComment.setRevisionInitiatorTel(userInfo.getMobile());
        revisionComment.setCreateDate(now);
        revisionComment.setCreateAccount(userInfo.getAccount());
        revisionComment.setLastModifyDate(now);
        revisionComment.setLastModifyAccount(userInfo.getAccount());

        return revisionComment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StandardOperationResultVo batchInitiateRevision(MultipartFile file, String useroperation, String usersuggestion) {
        log.info("批量发起修订操作 - useroperation: {}, fileName: {}", useroperation, file.getOriginalFilename());
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        
        if (useroperation == null || useroperation.trim().isEmpty()) {
            throw new IllegalArgumentException("操作类型不能为空");
        }

        List<String> dataElementIds = new ArrayList<>();
        Map<String, String> dataElementRevisionMap = new HashMap<>(); // 存储数据元ID和对应的修订意见
        
        // 解析Excel文件内容获取数据元名称和修订意见列表
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".xlsx")) {
                throw new IllegalArgumentException("仅支持.xlsx格式的Excel文件");
            }

            try (InputStream inputStream = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {
                
                Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表
                
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // 从第二行开始读取，跳过表头
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;
                    
                    // 读取第二列：数据元名称
                    Cell dataElementNameCell = row.getCell(1);
                    if (dataElementNameCell == null) continue;
                    
                    String dataElementName = "";
                    if (dataElementNameCell.getCellType() == CellType.STRING) {
                        dataElementName = dataElementNameCell.getStringCellValue();
                    } else if (dataElementNameCell.getCellType() == CellType.NUMERIC) {
                        dataElementName = String.valueOf((long) dataElementNameCell.getNumericCellValue());
                    }
                    
                    // 读取第三列：修订意见
                    Cell revisionCommentCell = row.getCell(2);
                    String revisionComment = "";
                    if (revisionCommentCell != null) {
                        if (revisionCommentCell.getCellType() == CellType.STRING) {
                            revisionComment = revisionCommentCell.getStringCellValue();
                        } else if (revisionCommentCell.getCellType() == CellType.NUMERIC) {
                            revisionComment = String.valueOf((long) revisionCommentCell.getNumericCellValue());
                        }
                    }
                    
                    dataElementName = dataElementName.trim();
                    revisionComment = revisionComment.trim();
                    
                    if (!dataElementName.isEmpty()) {
                        // 通过数据元名称查询dataid
                        BaseDataElement element = baseDataElementMapper.selectFirstByName(dataElementName);
                        if (element != null) {
                            dataElementIds.add(element.getDataid());
                            // 存储数据元ID和对应的修订意见
                            dataElementRevisionMap.put(element.getDataid(), revisionComment);
                        } else {
                            log.warn("未找到数据元名称为[{}]的记录", dataElementName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析Excel文件失败", e);
            throw new IllegalArgumentException("Excel文件格式不正确或内容解析失败：" + e.getMessage());
        }
        
        if (dataElementIds.isEmpty()) {
            throw new IllegalArgumentException("文件中未找到有效的数据元ID");
        }
        
        // 由于每个数据元都有对应的修订意见，需要单独处理每个数据元
        int successCount = 0;
        int errorCount = 0;
        List<StandardOperationResultVo.FailureItem> failureItems = new ArrayList<>();
        List<RevisionComment> revisionComments = new ArrayList<>();

        // 查询所有数据元信息，用于验证存在性
        List<BaseDataElement> dataElements = dataElementStandardMapper.queryDataElementsByIds(dataElementIds);
        Map<String, BaseDataElement> dataElementMap = new HashMap<>();
        for (BaseDataElement element : dataElements) {
            dataElementMap.put(element.getDataid(), element);
        }

        for (String dataid : dataElementIds) {
            // 验证数据元是否存在
            BaseDataElement dataElement = dataElementMap.get(dataid);
            if (dataElement == null) {
                errorCount++;
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName("未知");
                failureItem.setSourceUnitName("");
                failureItem.setFailureReason("系统中未找到相关数据元");
                failureItems.add(failureItem);
                continue;
            }

            // 获取对应的修订意见
            String elementRevisionComment = dataElementRevisionMap.get(dataid);
            if (elementRevisionComment == null || elementRevisionComment.trim().isEmpty()) {
                elementRevisionComment = usersuggestion; // 如果没有单独的修订意见，使用统一的意见
            }

            // 验证修订意见
            if ("发起修订".equals(useroperation) &&
                (elementRevisionComment == null || elementRevisionComment.trim().isEmpty())) {
                errorCount++;
                // 获取组织单位名称（非必须，仅用于显示）
                String sourceUnitName = "";
                try {
                    OrganizationUnit orgUnit = organizationUnitMapper.selectById(dataElement.getSourceUnitCode());
                    sourceUnitName = orgUnit != null ? orgUnit.getUnitName() : "";
                } catch (Exception e) {
                    // 组织单位获取失败不影响主流程
                }
                
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName(dataElement.getName());
                failureItem.setSourceUnitName(sourceUnitName);
                failureItem.setFailureReason("修订意见为空");
                failureItems.add(failureItem);
                continue;
            }

            try {
                // 根据操作类型处理
                if ("发起修订".equals(useroperation)) {
                    // 处理发起修订逻辑
                    processInitiateRevision(dataElement, dataid, elementRevisionComment);
                    
                    // 创建修订意见记录
                    RevisionComment revisionComment = createRevisionComment(dataid, elementRevisionComment);
                    revisionComments.add(revisionComment);
                    
                    successCount++;
                } else if ("报送".equals(useroperation)) {
                    // 处理报送逻辑
                    processSubmitRelease(dataElement, dataid, elementRevisionComment);
                    successCount++;
                } else {
                    // 操作类型无效，但这不应该发生，因为前端应该控制
                    successCount++;
                }

            } catch (Exception e) {
                errorCount++;
                // 获取组织单位名称（非必须，仅用于显示）
                String sourceUnitName = "";
                try {
                    OrganizationUnit orgUnit = organizationUnitMapper.selectById(dataElement.getSourceUnitCode());
                    sourceUnitName = orgUnit != null ? orgUnit.getUnitName() : "";
                } catch (Exception ex) {
                    // 组织单位获取失败不影响主流程
                }
                
                StandardOperationResultVo.FailureItem failureItem = new StandardOperationResultVo.FailureItem();
                failureItem.setDataElementName(dataElement.getName());
                failureItem.setSourceUnitName(sourceUnitName);
                failureItem.setFailureReason("系统异常：" + e.getMessage());
                failureItems.add(failureItem);
                log.error("发起修订数据元[{}]时发生异常", dataid, e);
            }
        }

        // 批量插入修订意见记录
        if (!revisionComments.isEmpty()) {
            try {
                revisionCommentMapper.batchInsert(revisionComments);
            } catch (Exception e) {
                log.error("批量插入修订意见失败", e);
                throw new RuntimeException("保存修订意见失败：" + e.getMessage());
            }
        }

        // 构建返回结果
        StandardOperationResultVo result = new StandardOperationResultVo();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        result.setTotalCount(dataElementIds.size());
        result.setOperationType(useroperation);
        result.setIsSuccess(errorCount == 0);
        result.setFailureItems(failureItems);
        
        if (errorCount == 0) {
            result.setResultMessage("操作完成，全部成功");
        } else {
            result.setResultMessage("操作完成，成功" + successCount + "条，失败" + errorCount + "条");
        }

        log.info("批量发起修订操作完成 - 总数：{}，成功：{}，失败：{}", 
                dataElementIds.size(), successCount, errorCount);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StandardOperationResultVo publishStandard(ApproveInfoDTO publishDTO) {
        log.info("发布标准 - publishDTO: {}", publishDTO);
        
        if (publishDTO.getList() == null || publishDTO.getList().isEmpty()) {
            throw new IllegalArgumentException("数据元ID列表不能为空");
        }

        // 只支持"发布"操作
        if (!"发布".equals(publishDTO.getUseroperation())) {
            throw new IllegalArgumentException("操作类型只能为'发布'");
        }

        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorDetails = new StringBuilder();

        for (String dataid : publishDTO.getList()) {
            try {
                // 验证数据元是否存在
                BaseDataElement dataElement = dataElementStandardMapper.selectById(dataid);
                if (dataElement == null) {
                    errorCount++;
                    errorDetails.append("数据元[").append(dataid).append("]不存在；");
                    continue;
                }

                // 处理发布逻辑
                processPublish(dataElement, dataid, publishDTO.getUsersuggestion());
                successCount++;

            } catch (Exception e) {
                errorCount++;
                errorDetails.append("数据元[").append(dataid).append("]处理失败：").append(e.getMessage()).append("；");
                log.error("发布数据元[{}]时发生异常", dataid, e);
            }
        }

        // 构建返回结果
        StandardOperationResultVo result = new StandardOperationResultVo();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        result.setTotalCount(publishDTO.getList().size());
        result.setOperationType(publishDTO.getUseroperation());
        result.setIsSuccess(errorCount == 0);

        String resultMessage = String.format("发布完成：成功%d条，失败%d条", successCount, errorCount);
        result.setResultMessage(resultMessage);

        if (errorCount > 0) {
            result.setErrorDetails(errorDetails.toString());
        }

        log.info("发布结果：{}", resultMessage);
        return result;
    }

    private void processPublish(BaseDataElement dataElement, String dataid, String usersuggestion) {
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        String originalStatus = dataElement.getStatus();
        
        // 验证数据元状态：必须为待发布状态
        if (!"Todoreleased".equals(originalStatus)) {
            throw new RuntimeException("数据元当前状态为[" + originalStatus + "]，不允许发布操作，只有处于[Todoreleased]状态的数据元才能发布");
        }

        // 计算下一状态（发布）
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(originalStatus, "发布");
        if (!nextStatusVo.getIsValid()) {
            throw new RuntimeException("状态流转配置不存在或无效：从[" + originalStatus + "]执行[发布]操作");
        }
        String nextStatus = nextStatusVo.getNextStatus();

        // 更新数据元状态和发布时间
        dataElement.setStatus(nextStatus);
        dataElement.setLastApproveDate(new Date());
        dataElement.setLastModifyDate(new Date());
        dataElement.setPublishDate(new Date());
        dataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElementMapper.updateById(dataElement);

        // 记录流程历史到process_record表
        ProcessRecordDto processRecordDto = new ProcessRecordDto();
        processRecordDto.setBaseDataelementDataid(dataid);
        processRecordDto.setOperation("发布");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(usersuggestion);
        processRecordDto.setOperateTime(new Date());
        flowProcessService.recordProcessHistory(processRecordDto);
        
        log.info("数据元[{}]发布成功：{} -> {}", dataid, originalStatus, nextStatus);
    }

    @Override
    public void exportRevisionFailures(MultipartFile file, String useroperation, String usersuggestion, HttpServletResponse response) {
        log.info("导出修订失败条目 - useroperation: {}, fileName: {}", useroperation, file.getOriginalFilename());
        
        // 先执行批量修订操作获取结果
        StandardOperationResultVo result = batchInitiateRevision(file, useroperation, usersuggestion);
        
        // 初始化List<ExportRevisionFailureDTO> failList
        List<ExportRevisionFailureDTO> failList = new ArrayList<>();
        
        if (result.getFailureItems() == null || result.getFailureItems().isEmpty()) {
            // 如果没有失败条目，创建一个提示信息
            ExportRevisionFailureDTO noFailureDTO = new ExportRevisionFailureDTO();
            noFailureDTO.setSerialNumber("1");
            noFailureDTO.setDataElementName("无");
            noFailureDTO.setSourceUnitName("无");
            noFailureDTO.setRevisionSuggestion("无");
            noFailureDTO.setFailureReason("所有数据元都处理成功，没有失败条目");
            failList.add(noFailureDTO);
        } else {
            int seqNo = 1;
            // 遍历失败条目，依次取出failItem
            for (StandardOperationResultVo.FailureItem failItem : result.getFailureItems()) {
                // 创建ExportRevisionFailureDTO exportFail
                ExportRevisionFailureDTO exportFail = new ExportRevisionFailureDTO();
                exportFail.setSerialNumber(String.valueOf(seqNo++));
                exportFail.setDataElementName(failItem.getDataElementName());
                exportFail.setSourceUnitName(failItem.getSourceUnitName());
                exportFail.setRevisionSuggestion(usersuggestion);
                exportFail.setFailureReason(failItem.getFailureReason());
                
                // 添加exportFail到failList中
                failList.add(exportFail);
            }
        }
        
        // 调用CommonService.exportExcelData
        try {
            commonService.exportExcelData(failList, response, "修订失败条目", ExportRevisionFailureDTO.class);
            log.info("成功导出{}条修订失败条目", failList.size());
        } catch (Exception e) {
            log.error("导出修订失败条目失败", e);
            throw new RuntimeException("导出修订失败条目失败");
        }
    }

    @Override
    public void exportPublishedList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出已发布数据元列表 - queryDto: {}", queryDto);

        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);

        // 查询所有符合条件的数据元记录
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto, userOrgCode);

        if (exportList == null) {
            exportList = new ArrayList<>();
        }

        // 数据转换
        List<ExportPublishedDTO> exportData = new ArrayList<>();
        for (AuditDataElementVo vo : exportList) {
            ExportPublishedDTO dto = new ExportPublishedDTO();
            dto.setDataElementName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setReportTime(vo.getReportTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "已发布数据元列表", ExportPublishedDTO.class);
            log.info("成功导出{}条已发布数据元", exportData.size());
        } catch (Exception e) {
            log.error("导出已发布数据元列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出进度列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportProgressDTO dto = new ExportProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setInitiateRevisionTime(vo.getInitiateRevisionTime());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "进度列表", ExportProgressDTO.class);
        } catch (Exception e) {
            log.error("导出进度列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportSourceUnitInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出数源单位在办列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportSourceUnitInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportSourceUnitInProgressDTO dto = new ExportSourceUnitInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setInitiateRevisionTime(vo.getInitiateRevisionTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "数源单位在办列表", ExportSourceUnitInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出数源单位在办列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgAuditInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方在办-待审核列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgAuditInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgAuditInProgressDTO dto = new ExportOrgAuditInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setSubmitTime(vo.getSubmitTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方在办-待审核列表", ExportOrgAuditInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出组织方在办-待审核列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgOpinionInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方在办-征求意见列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgOpinionInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgOpinionInProgressDTO dto = new ExportOrgOpinionInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setAuditTime(vo.getAuditTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方在办-征求意见列表", ExportOrgOpinionInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出组织方在办-征求意见列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgReexamInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方在办-待复审列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgReexamInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgReexamInProgressDTO dto = new ExportOrgReexamInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setInitiateRevisionTime(vo.getInitiateRevisionTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方在办-待复审列表", ExportOrgReexamInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出组织方在办-待复审列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgReleaseInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方在办-待发布列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgReleaseInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgReleaseInProgressDTO dto = new ExportOrgReleaseInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setReportTime(vo.getReportTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方在办-待发布列表", ExportOrgReleaseInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出组织方在办-待发布列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportCompletedList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出已完成列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportCompletedDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportCompletedDTO dto = new ExportCompletedDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "已完成列表", ExportCompletedDTO.class);
        } catch (Exception e) {
            log.error("导出已完成列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportUnderstandingSituationList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出我要了解情况列表 - queryDto: {}", queryDto);

        // 查询所有符合条件的数据元记录(不分页)，不筛选当前登录用户的数源单位
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, null);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportUnderstandingSituationDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportUnderstandingSituationDTO dto = new ExportUnderstandingSituationDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "我要了解情况列表", ExportUnderstandingSituationDTO.class);
        } catch (Exception e) {
            log.error("导出我要了解情况列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportSourceUnitStandardResultList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出数源单位定标结果列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportSourceUnitStandardResultDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportSourceUnitStandardResultDTO dto = new ExportSourceUnitStandardResultDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "数源单位定标结果列表", ExportSourceUnitStandardResultDTO.class);
        } catch (Exception e) {
            log.error("导出数源单位定标结果列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgStandardResultList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方定标结果列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgStandardResultDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgStandardResultDTO dto = new ExportOrgStandardResultDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setSourceUnitName(vo.getSourceunitName());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方定标结果列表", ExportOrgStandardResultDTO.class);
        } catch (Exception e) {
            log.error("导出组织方定标结果列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOwnUnitInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出本单位在办列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOwnUnitInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOwnUnitInProgressDTO dto = new ExportOwnUnitInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setInitiateRevisionTime(vo.getInitiateRevisionTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "本单位在办列表", ExportOwnUnitInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出本单位在办列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportOrgInProgressList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方在办列表 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportOrgInProgressDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportOrgInProgressDTO dto = new ExportOrgInProgressDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setSubmitTime(vo.getSubmitTime());
            dto.setRevisionSubmitTime(vo.getRevisionSubmitTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "组织方在办列表", ExportOrgInProgressDTO.class);
        } catch (Exception e) {
            log.error("导出组织方在办列表失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportCompletedPageList(StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出已完成列表页 - queryDto: {}", queryDto);

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String userOrgCode = userInfo.getOrgCode();
        
        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto, userOrgCode);

        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
        }

        // 数据转换
        List<ExportCompletedPageDTO> exportData = new ArrayList<>();
        for (StandardDataElementPageInfoVo vo : exportList) {
            ExportCompletedPageDTO dto = new ExportCompletedPageDTO();
            dto.setName(vo.getName());
            dto.setDataElementCode(vo.getDataelementCode());
            dto.setDefinition(vo.getDefinition());
            dto.setDatatype(vo.getDatatype());
            dto.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
            dto.setConfirmDate(vo.getConfirmDate());
            dto.setPublishTime(vo.getPublishTime());
            exportData.add(dto);
        }

        // 导出Excel
        try {
            commonService.exportExcelData(exportData, response, "已完成列表页", ExportCompletedPageDTO.class);
        } catch (Exception e) {
            log.error("导出已完成列表页失败", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void downloadBatchRevisionTemplate(HttpServletResponse response) {
        log.info("开始下载批量修订导入模板");

        try {
            String templateFileName = "批量修订导入模板.xlsx";
            String downloadFileName = "批量修订导入模板.xlsx";

            // 从项目根目录读取模板文件
            downloadTemplateFile(templateFileName, downloadFileName, response);

            log.info("批量修订导入模板下载完成");
        } catch (Exception e) {
            log.error("下载批量修订导入模板失败", e);
            throw new RuntimeException("下载模板失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载模板文件（支持jar包运行）
     */
    private void downloadTemplateFile(String templateFileName, String downloadFileName, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" +
                URLEncoder.encode(downloadFileName, "UTF-8"));

        // 尝试从classpath读取模板文件（支持jar包运行）
        try (InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateFileName)) {
            if (templateStream == null) {
                // 如果classpath中没有，尝试从项目根目录读取（开发环境）
                String projectRoot = System.getProperty("user.dir");
                String templateFilePath = projectRoot + File.separator + templateFileName;

                File templateFile = new File(templateFilePath);
                if (!templateFile.exists()) {
                    throw new RuntimeException("模板文件不存在: " + templateFilePath + " 且在classpath中也未找到: templates/" + templateFileName);
                }

                try (FileInputStream fis = new FileInputStream(templateFile);
                     OutputStream os = response.getOutputStream()) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                }
            } else {
                // 从classpath读取模板文件
                try (OutputStream os = response.getOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = templateStream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                }
            }
        }

        log.info("成功下载模板文件: {}", templateFileName);
    }
}
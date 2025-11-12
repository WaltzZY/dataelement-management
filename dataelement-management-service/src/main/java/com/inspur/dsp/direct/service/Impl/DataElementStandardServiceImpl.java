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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
        if (organizationUnit == null) {
            throw new IllegalArgumentException("数源单位不存在");
        }

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
        List<ProcessRecord> processRecords = processRecordMapper.getAllRevisionRecordsByDataId(dataid);
        if (processRecords != null && !processRecords.isEmpty()) {
            for (int i = 0; i < processRecords.size(); i++) {
                ProcessRecord processRecord = processRecords.get(i);
                RejectionInfoVO vo = new RejectionInfoVO();
                
                // 如果是多条记录，添加序号到驳回内容中
                String content = processRecord.getUsersuggestion();
                if (processRecords.size() > 1) {
                    content = (i + 1) + ". " + content;
                }
                vo.setRevisionContent(content);
                vo.setRevisionCreatedate(processRecord.getProcessdatetime());
                vo.setCreateUserName(processRecord.getProcessusername());
                
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
                String content = entity.getRevisionContent();
                if (revisionCommentEntities.size() > 1) {
                    content = (i + 1) + ". " + content;
                }
                vo.setRevisionContent(content);
                vo.setRevisionCreatedate(entity.getRevisionCreatedate());
                vo.setRevisionInitiatorAccount(entity.getCreateAccount());
                // TODO: 如果需要发起修订人姓名，需要通过createAccount查询用户表
                vo.setRevisionInitiatorName(""); // 当前设为空，后续可以添加用户信息查询
                vo.setContactTel(entity.getContactTel());
                
                revisionComments.add(vo);
            }
        }

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
        String flowStatus = mapStatusForFlow(dataElement.getStatus());
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus(flowStatus, "提交复审");
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
        
        // 执行查询
        List<StandardDataElementPageInfoVo> records = dataElementStandardMapper.getAllStandardList(page, queryDto);
        
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

        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto);
        
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

        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto);
        
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

        // 查询所有符合条件的数据元记录(不分页)
        List<StandardDataElementPageInfoVo> exportList = dataElementStandardMapper.getAllStandardList(null, queryDto);
        
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
    public List<AuditDataElementVo> auditDataElementList(AuditDataElementQueryDto queryDto) {
        log.info("执行审核标准模块数据元查询 - queryDto: {}", queryDto);
        
        // 规范化日期范围
        normalizeAuditDateRange(queryDto);
        
        // 校验并规范化排序参数
        validateAndNormalizeAuditSortParams(queryDto);
        
        // 执行查询
        List<AuditDataElementVo> resultList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
        // 补充状态描述
        for (AuditDataElementVo vo : resultList) {
            vo.setStatusDesc(CalibrationStatusEnums.getDescByCode(vo.getStatus()));
        }
        
        log.info("查询完成，共返回{}条记录", resultList.size());
        return resultList;
    }

    /**
     * 处理审核查询的日期边界问题
     */
    private void normalizeAuditDateRange(AuditDataElementQueryDto queryDto) {
        if (queryDto.getSubmitTimeBegin() != null) {
            // 开始时间设置为当天00:00:00.000
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getSubmitTimeBegin());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            queryDto.setSubmitTimeBegin(cal.getTime());
        }
        if (queryDto.getSubmitTimeEnd() != null) {
            // 结束时间设置为当天23:59:59.999
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(queryDto.getSubmitTimeEnd());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            queryDto.setSubmitTimeEnd(cal.getTime());
        }
    }

    /**
     * 校验并规范化审核查询的排序参数
     */
    private void validateAndNormalizeAuditSortParams(AuditDataElementQueryDto queryDto) {
        if (queryDto.getSortField() == null || queryDto.getSortField().isEmpty()) {
            queryDto.setSortField("submit_time");
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
    public String appvoveStandard(ApproveInfoDTO approveDTO) {
        log.info("审核标准 - approveDTO: {}", approveDTO);
        
        if (approveDTO.getList() == null || approveDTO.getList().isEmpty()) {
            throw new IllegalArgumentException("审核信息列表不能为空");
        }
        
        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorDetails = new StringBuilder();
        
        for (ApproveInfoOB approveInfo : approveDTO.getList()) {
            try {
                // 验证数据元是否存在
                BaseDataElement dataElement = dataElementStandardMapper.selectById(approveInfo.getDataid());
                if (dataElement == null) {
                    errorCount++;
                    errorDetails.append("数据元[").append(approveInfo.getDataid()).append("]不存在；");
                    continue;
                }
                
                // 根据审核操作类型处理
                if ("审核通过".equals(approveInfo.getUseroperation())) {
                    // 处理审核通过逻辑
                    processApprovalPass(dataElement, approveInfo);
                    successCount++;
                } else if ("驳回".equals(approveInfo.getUseroperation())) {
                    // 处理驳回逻辑
                    processApprovalReject(dataElement, approveInfo);
                    successCount++;
                } else {
                    errorCount++;
                    errorDetails.append("数据元[").append(approveInfo.getDataid()).append("]操作类型无效；");
                }
                
            } catch (Exception e) {
                errorCount++;
                errorDetails.append("数据元[").append(approveInfo.getDataid()).append("]处理失败：").append(e.getMessage()).append("；");
                log.error("审核数据元[{}]时发生异常", approveInfo.getDataid(), e);
            }
        }
        
        String result = String.format("审核完成：成功%d条，失败%d条", successCount, errorCount);
        if (errorCount > 0) {
            result += "。失败详情：" + errorDetails;
        }
        
        log.info("审核结果：{}", result);
        return result;
    }
    
    private void processApprovalPass(BaseDataElement dataElement, ApproveInfoOB approveInfo) {
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
        processRecordDto.setBaseDataelementDataid(approveInfo.getDataid());
        processRecordDto.setOperation("审核通过");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(approveInfo.getUsersuggestion()); // 审核意见写入process_record
        flowProcessService.recordProcessHistory(processRecordDto);
        
        log.info("数据元[{}]审核通过，状态变更为：{}", approveInfo.getDataid(), nextStatus);
    }
    
    private void processApprovalReject(BaseDataElement dataElement, ApproveInfoOB approveInfo) {
        // 驳回操作必须提供驳回意见
        if (approveInfo.getUsersuggestion() == null || approveInfo.getUsersuggestion().trim().isEmpty()) {
            throw new IllegalArgumentException("驳回意见不能为空");
        }
        
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
        processRecordDto.setBaseDataelementDataid(approveInfo.getDataid());
        processRecordDto.setOperation("驳回");
        processRecordDto.setSourceStatus(originalStatus);
        processRecordDto.setDestStatus(nextStatus);
        processRecordDto.setOperatorAccount(userInfo.getAccount());
        processRecordDto.setOperatorName(userInfo.getName());
        processRecordDto.setOperatorUnitCode(userInfo.getOrgCode());
        processRecordDto.setOperatorUnitName(userInfo.getOrgName());
        processRecordDto.setUsersuggestion(approveInfo.getUsersuggestion()); // 驳回意见写入process_record的usersuggestion字段
        flowProcessService.recordProcessHistory(processRecordDto);
        
        log.info("数据元[{}]已驳回，状态变更为：{}，驳回意见：{}", approveInfo.getDataid(), nextStatus, approveInfo.getUsersuggestion());
    }

    @Override
    public void exportPendingReviewList(AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待审核列表 - queryDto: {}", queryDto);
        
        // 移除分页参数，查询所有数据
        queryDto.setPageNum(null);
        queryDto.setPageSize(null);
        
        // 查询所有符合条件的数据元记录
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
        if (exportList.isEmpty()) {
            throw new IllegalArgumentException("无数据可导出");
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
            dto.setSourceUnitName(vo.getSourceUnitName());
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
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
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
            dto.setSourceUnitName(vo.getSourceUnitName());
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
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
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
            dto.setSourceUnitName(vo.getSourceUnitName());
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
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
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
            dto.setSourceUnitName(vo.getSourceUnitName());
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
        List<AuditDataElementVo> exportList = dataElementStandardMapper.queryAuditDataElementList(queryDto);
        
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
            dto.setSourceUnitName(vo.getSourceUnitName());
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
}
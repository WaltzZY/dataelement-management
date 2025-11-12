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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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

        log.info("数据元状态检查 - dataid: {}, status: {}", dataid, dataElement.getStatus());
        if (!"designated_source".equals(dataElement.getStatus())) {
            log.warn("状态不符合要求 - expected: designated_source, actual: {}", dataElement.getStatus());
            throw new IllegalArgumentException("当前状态不允许编辑");
        }

        BasicInfoVo basicInfo = new BasicInfoVo();
        basicInfo.setDataid(dataElement.getDataid());
        basicInfo.setDataelementcode(dataElement.getDataElementId());
        basicInfo.setName(dataElement.getName());
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
            catalogVos.add(vo);
        }

        // 查询标准文件列表
        List<AttachmentFileVo> standardFiles = getFileList(dataid, "standardfile");

        // 查询样例文件列表
        List<AttachmentFileVo> exampleFiles = getFileList(dataid, "examplefile");

        // 封装完整VO
        StandardCompleteInfoVo result = new StandardCompleteInfoVo();
        result.setBasicInfo(basicInfo);
        result.setContactInfo(contactInfo);
        result.setAttributes(attributeVos);
        result.setAssociatedCatalogs(catalogVos);
        result.setStandardFiles(standardFiles);
        result.setExampleFiles(exampleFiles);

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
                log.debug("正在获取当前登录用户信息...");
                UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

                existingRelation.setCatalogName(relation.getCatalogName());
                existingRelation.setCatalogDesc(relation.getCatalogDescription());
                existingRelation.setInfoItemName(relation.getDataItemName());
                existingRelation.setInfoItemDatatype(relation.getDataType());
                dataElementCatalogRelationMapper.updateById(existingRelation);
            } else {
                // 插入逻辑
                log.debug("正在获取当前登录用户信息...");
                UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

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

        if (file == null || file.isEmpty()) {
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
        String fileUrl = fileLocation; // CommonService返回的就是可访问的路径

        // 插入附件表
        DataElementAttachment attachment = new DataElementAttachment();
        attachment.setAttachfileid(UUID.randomUUID().toString());
        attachment.setBaseDataelementDataid(dataid);
        attachment.setAttachfiletype(filetype);
        attachment.setAttachfilename(file.getOriginalFilename());
        attachment.setAttachfileurl(fileUrl);
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

        // 参数基础校验
        if (dto == null) {
            throw new IllegalArgumentException("保存请求不能为空");
        }
        if (dto.getDataid() == null || dto.getDataid().isEmpty()) {
            throw new IllegalArgumentException("数据元ID不能为空");
        }
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
        if (!"designated_source".equals(dataElement.getStatus())) {
            throw new IllegalArgumentException("当前状态不允许编辑");
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

        // 调用saveStandard保存信息(包含所有数据校验)
        this.saveStandard(dto);

        // 查询并校验数据元状态
        BaseDataElement dataElement = baseDataElementMapper.selectById(dto.getDataid());
        if (dataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        if (!"designated_source".equals(dataElement.getStatus())) {
            throw new IllegalArgumentException("当前状态不允许提交，必须为待定标状态");
        }

        // 计算下一状态
        NextStatusVo nextStatusVo = flowProcessService.calculateNextStatus("designated_source", "提交审核");
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
        processRecordDto.setSourceStatus("designated_source");
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

        // 调用saveStandard保存信息(包含所有数据校验)
        this.saveStandard(dto);

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

        if (CollectionUtils.isEmpty(exportList)) {
            exportList = new ArrayList<>();
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

        if (CollectionUtils.isEmpty(exportList)) {
            exportList = new ArrayList<>();
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
}
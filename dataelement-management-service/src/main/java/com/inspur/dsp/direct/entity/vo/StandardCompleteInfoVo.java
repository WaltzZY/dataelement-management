package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 编制标准完整信息VO
 */
@Data
public class StandardCompleteInfoVo {

    private BasicInfoVo basicInfo;
    
    private ContactInfoVo contactInfo;
    
    private List<AttributeVo> attributes;
    
    private List<AssociatedCatalogVo> associatedCatalogs;
    
    private List<AttachmentFileVo> standardFiles;
    
    private List<AttachmentFileVo> exampleFiles;
    
    private List<RejectionInfoVO> rejectionInfo;
    
    private List<RevisionCommentVO> revisionComments;
    
    private List<ProcessVO> processRecords;
}
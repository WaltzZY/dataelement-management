package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 修订意见VO
 * 
 * @author system
 * @since 2025
 */
@Data
public class RevisionCommentVO {
    
    /**
     * 修订意见内容
     */
    private String revisionContent;
    
    /**
     * 发起修订人账号
     */
    private String revisionInitiatorAccount;
    
    /**
     * 发起修订人姓名
     */
    private String revisionInitiatorName;
    
    /**
     * 发起时间
     */
    private Date revisionCreatedate;
    
    /**
     * 联系方式/联系电话
     */
    private String contactTel;
}
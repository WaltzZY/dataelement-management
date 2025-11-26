package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 驳回信息VO
 * 
 * @author system
 * @since 2025
 */
@Data
public class RejectionInfoVO {
    
    /**
     * 驳回原因/修订意见内容
     */
    private String revisionContent;
    
    /**
     * 驳回时间/修订创建时间
     */
    private Date revisionCreatedate;

    
    /**
     * 驳回人姓名（需要通过createAccount关联查询用户表获取）
     */
    private String createUserName;

    /**
     * 驳回人所属组织单位名称
     */
    private String createUserOrgName;

    /**
     * 驳回节点/审核节点
     */
    private String revisionNode;
    
    /**
     * 联系电话
     */
    private String contactTel;
}
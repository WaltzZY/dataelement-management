package com.inspur.dsp.direct.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 修订意见实体类
 * 
 * @author system
 * @since 2025
 */
@Data
@TableName("revision_comment")
public class RevisionComment {
    
    /**
     * 主键ID
     */
    @TableId
    private String id;
    
    /**
     * 数据元ID
     */
    private String baseDataelementDataid;
    
    /**
     * 修订意见内容
     */
    private String revisionContent;
    
    /**
     * 修订创建时间
     */
    private Date revisionCreatedate;

    /**
     * 修订发起人姓名
     */
    private Date revisionCreateName;


    /**
     * 修订发起人账号
     */
    private Date revisionCreateAccount;
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 修改时间
     */
    private Date modifyDate;
    
    /**
     * 创建人账号
     */
    private String createAccount;
    
    /**
     * 修改人账号
     */
    private String modifyAccount;
    
    /**
     * 联系电话
     */
    private String contactTel;
}
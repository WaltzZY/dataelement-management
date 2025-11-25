package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 审核标准模块数据元查询条件DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class AuditDataElementQueryDto {
    
    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;
    
    /**
     * 数源单位统一社会信用代码列表（支持多选）
     */
    private List<String> sourceunitCodeList;
    
    /**
     * 数据元状态列表（支持多选）
     */
    private List<String> statusList;
    
    /**
     * 提交开始时间
     */
    private Date submitTimeBegin;
    
    /**
     * 提交结束时间
     */
    private Date submitTimeEnd;
    
    /**
     * 审核开始时间
     */
    private Date approveTimeBegin;
    
    /**
     * 审核结束时间
     */
    private Date approveTimeEnd;
    
    /**
     * 修订提交开始时间
     */
    private Date revisionSubmitTimeBegin;
    
    /**
     * 修订提交结束时间
     */
    private Date revisionSubmitTimeEnd;
    
    /**
     * 报送开始时间
     */
    private Date reportTimeBegin;
    
    /**
     * 报送结束时间
     */
    private Date reportTimeEnd;
    
    /**
     * 审核驳回开始时间
     */
    private Date rejectTimeBegin;
    
    /**
     * 审核驳回结束时间
     */
    private Date rejectTimeEnd;
    
    /**
     * 发起修订开始时间
     */
    private Date initiateRevisionTimeBegin;
    
    /**
     * 发起修订结束时间
     */
    private Date initiateRevisionTimeEnd;
    
    /**
     * 定源开始时间
     */
    private Date sourceTimeBegin;
    
    /**
     * 定源结束时间
     */
    private Date sourceTimeEnd;
    
    /**
     * 关键词（在数据元名称、数据元编码、定义中模糊匹配）
     */
    private String keyword;
    
    /**
     * 排序字段
     */
    private String sortField;
    
    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder;
}
package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 定标阶段数据元列表查询条件DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class StandardDataElementPageQueryDto {
    
    /**
     * 页码
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 数据元状态列表（支持多选）
     */
    private List<String> status;
    
    /**
     * 发起修订开始日期
     */
    private Date revisionCreateDateBegin;
    
    /**
     * 发起修订结束日期
     */
    private Date revisionCreateDateEnd;
    
    /**
     * 定源时间开始日期
     */
    private Date confirmDateBegin;
    
    /**
     * 定源时间结束日期
     */
    private Date confirmDateEnd;
    
    /**
     * 发布时间开始日期
     */
    private Date publishDateBegin;
    
    /**
     * 发布时间结束日期
     */
    private Date publishDateEnd;

    /**
     * 审核驳回时间开始日期
     */
    private Date rejectTimeBegin;

    /**
     * 审核驳回时间结束日期
     */
    private Date rejectTimeEnd;
    
    /**
     * 关键字（在名称/定义/数据元编码中搜索）
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
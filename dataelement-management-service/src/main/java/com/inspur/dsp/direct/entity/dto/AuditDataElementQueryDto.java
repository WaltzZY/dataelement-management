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
     * 数源单位统一社会信用代码（可选）
     */
    private String sourceunitCode;
    
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
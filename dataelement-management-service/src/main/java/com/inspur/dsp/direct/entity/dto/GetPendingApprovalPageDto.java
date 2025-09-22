package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 分页查询待核定数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPendingApprovalPageDto {

    /**
     * 发起时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateBegin;

    /**
     * 发起时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateEnd;

    /**
     * 定源时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateBegin;

    /**
     * 定源时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateEnd;

    /**
     * 数源单位code集合
     */
    private List<String> sourceUnitCodeList;

    /**
     * 基准数据元名称/定义/数源单位名称模糊
     */
    private String keyword;

    /**
     * 页面状态：待核定传递"pending_approval"，已定源传递"confirmed"
     */
    @NotEmpty(message = "页面状态不能为空")
    private String auditStatus;

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 每页条数
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式
     */
    private String sortOrder;
}
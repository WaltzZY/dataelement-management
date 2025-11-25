package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 标准操作结果VO
 * 支持操作、复审、修订、发布等多种操作的结果返回
 * 
 * @author system
 * @since 2025
 */
@Data
public class StandardOperationResultVo {
    
    /**
     * 操作成功数量
     */
    private Integer successCount;
    
    /**
     * 操作失败数量
     */
    private Integer errorCount;
    
    /**
     * 操作总数量
     */
    private Integer totalCount;
    
    /**
     * 操作类型（审核通过、驳回/通过并报送、驳回/发起修订、报送）
     */
    private String operationType;
    
    /**
     * 操作结果描述
     */
    private String resultMessage;
    
    /**
     * 失败详情
     */
    private String errorDetails;
    
    /**
     * 操作是否完全成功
     */
    private Boolean isSuccess;
    
    /**
     * 失败条目详细列表(用于批量操作)
     */
    private List<FailureItem> failureItems;
    
    /**
     * 失败条目详细信息内部类
     */
    @Data
    public static class FailureItem {
        
        /**
         * 数据元名称
         */
        private String dataElementName;
        
        /**
         * 数源单位
         */
        private String sourceUnitName;
        
        /**
         * 失败原因
         */
        private String failureReason;
    }

}
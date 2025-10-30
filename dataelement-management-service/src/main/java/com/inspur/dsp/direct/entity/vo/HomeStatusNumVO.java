// HomeStatusNumVO.java
package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeStatusNumVO {
    // 组织方字段
    private Long pendingSourceQty = 0L;
    private Long pendingSourceConfirmQty = 0L;
    private Long pendingSourceClaimQty = 0L;
    private Long pendingApprovalQty = 0L;
    private Long pendingNegotiationQty = 0L;
    private Long negotiatingQty = 0L;
    private Long designatedSourceQty = 0L;
    private Long confirmingQty = 0L;
    private Long claimedingQty = 0L;
    
    // 采集方字段
    private Long toBeProcessedQty = 0L;
    private Long pendingQty = 0L;
    private Long pendingClaimedQty = 0L;
    private Long processedQty = 0L;
    private Long myDesignatedSourceQty = 0L;
    private Long confirmedQty = 0L;
    private Long rejectedQty = 0L;
    private Long claimedQty = 0L;
    private Long notClaimedQty = 0L;

}
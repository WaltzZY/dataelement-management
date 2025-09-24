package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Map;

/**
 * 认领或拒绝DTO
 */
@Data
public class ClaimOrRejectDTO {

    /**
     * Map<任务id, 数据元id>
     */
    private Map<String, String> idTaskId;

    /** 认领或不认领 */
    private String operation;

    /** 当operation为不认领时的不认领说明 */
    private String instruction;

    // 构造函数
    public ClaimOrRejectDTO() {}

}
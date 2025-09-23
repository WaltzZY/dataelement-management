package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Map;

/**
 * 认领或拒绝DTO
 */
@Data
public class ClaimOrRejectDTO {

    /**
     * 数据元id与任务id映射
     */
    private Map<String, String> idTaskId;

//    /** 认领或不认领的数据元id */
//    private List<String> id;
//
//    /** 任务id */
//    private List<String> task_id;

    /** 认领或不认领 */
    private String operation;

    /** 当operation为不认领时的不认领说明 */
    private String instruction;

    // 构造函数
    public ClaimOrRejectDTO() {}

}
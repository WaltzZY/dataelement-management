package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.entity.dto.ProcessRecordDto;
import com.inspur.dsp.direct.entity.vo.NextStatusVo;

/**
 * 定标流程状态计算和流程记录Service接口
 * 专门负责流程状态管理
 *
 * @author system
 * @since 2025
 */
public interface FlowProcessService {

    /**
     * 计算下一状态
     * 根据当前状态和操作类型，利用FlowTransferDefinition表计算下一步状态
     *
     * @param currentStatus 当前状态
     * @param operation 操作类型(如"提交审核"、"驳回"、"审核通过"等)
     * @return 包含下一状态信息的VO
     */
    NextStatusVo calculateNextStatus(String currentStatus, String operation);

    /**
     * 记录定标流程
     * 将定标操作记录写入process_record表，用于流程追踪
     *
     * @param dto 流程记录请求DTO
     */
    void recordProcessHistory(ProcessRecordDto dto);
}
package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.dsp.direct.dao.FlowTransferDefinitionMapper;
import com.inspur.dsp.direct.dao.ProcessRecordMapper;
import com.inspur.dsp.direct.dbentity.FlowTransferDefinition;
import com.inspur.dsp.direct.dbentity.ProcessRecord;
import com.inspur.dsp.direct.entity.dto.ProcessRecordDto;
import com.inspur.dsp.direct.entity.vo.NextStatusVo;
import com.inspur.dsp.direct.service.FlowProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 定标流程状态计算和流程记录Service实现类
 * 负责流程状态管理的具体业务逻辑实现
 *
 * @author system
 * @since 2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FlowProcessServiceImpl implements FlowProcessService {

    private final FlowTransferDefinitionMapper flowTransferDefinitionMapper;
    private final ProcessRecordMapper processRecordMapper;

    @Override
    public NextStatusVo calculateNextStatus(String currentStatus, String operation) {
        log.info("计算下一状态 - currentStatus: {}, operation: {}", currentStatus, operation);

        // 参数校验
        if (currentStatus == null || currentStatus.isEmpty()) {
            throw new IllegalArgumentException("当前状态不能为空");
        }
        if (operation == null || operation.isEmpty()) {
            throw new IllegalArgumentException("操作类型不能为空");
        }

        // 查询流转规则
        QueryWrapper<FlowTransferDefinition> wrapper = new QueryWrapper<>();
        wrapper.eq("sourceactivityname", currentStatus)
                .eq("transfercondition", operation);
        FlowTransferDefinition transferDefinition = flowTransferDefinitionMapper.selectOne(wrapper);

        if (transferDefinition == null) {
            log.warn("未找到流转规则 - currentStatus: {}, operation: {}", currentStatus, operation);
            return NextStatusVo.invalid();
        }

        // 构建返回结果
        NextStatusVo result = new NextStatusVo();
        result.setNextStatus(transferDefinition.getDestactivityname());
        result.setNextStatusName(getStatusDisplayName(transferDefinition.getDestactivityname()));
        result.setIsValid(true);

        log.info("计算得到下一状态 - nextStatus: {}", result.getNextStatus());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordProcessHistory(ProcessRecordDto dto) {
        log.info("记录定标流程 - dto: {}", dto);

        // 参数校验
        if (dto == null) {
            throw new IllegalArgumentException("流程记录请求不能为空");
        }
        if (dto.getBaseDataelementDataid() == null || dto.getBaseDataelementDataid().isEmpty()) {
            throw new IllegalArgumentException("基准数据元ID不能为空");
        }
        if (dto.getOperation() == null || dto.getOperation().isEmpty()) {
            throw new IllegalArgumentException("操作类型不能为空");
        }

        // 构建流程记录对象
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessid(UUID.randomUUID().toString());
        processRecord.setBaseDataelementDataid(dto.getBaseDataelementDataid());
        processRecord.setFlowid(null); // 可选：关联到flow_definition表的流程ID
        processRecord.setProcessuserid(dto.getOperatorAccount());
        processRecord.setProcessusername(dto.getOperatorName());
        processRecord.setProcessunitcode(dto.getOperatorUnitCode());
        processRecord.setProcessunitname(dto.getOperatorUnitName());
        processRecord.setUseroperation(dto.getOperation());
        processRecord.setProcessdatetime(new Date());
        processRecord.setSourceactivityname(dto.getSourceStatus());
        processRecord.setDestactivityname(dto.getDestStatus());
        processRecord.setCreateDate(new Date());
        processRecord.setCreateAccount(dto.getOperatorAccount());

        // 插入数据库记录
        processRecordMapper.insert(processRecord);
        log.info("流程记录插入成功 - processid: {}", processRecord.getProcessid());
    }

    /**
     * 根据状态编码获取中文名称
     */
    private String getStatusDisplayName(String statusCode) {
        switch (statusCode) {
            case "TodoDetermined":
                return "待定标";
            case "PendingReview":
                return "待审核";
            case "SolicitingOpinions":
                return "征求意见";
            case "TodoRevised":
                return "待修订";
            case "PendingReExamination":
                return "待复审";
            case "Todoreleased":
                return "待发布";
            case "Published":
                return "已发布";
            default:
                return statusCode;
        }
    }
}
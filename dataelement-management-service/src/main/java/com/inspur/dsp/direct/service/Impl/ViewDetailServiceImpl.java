package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.*;
import com.inspur.dsp.direct.dbentity.*;
import com.inspur.dsp.direct.entity.bo.DomainSourceUnitInfo;
import com.inspur.dsp.direct.entity.dto.FlowNodeDTO;
import com.inspur.dsp.direct.entity.enums.DataElementStatus;
import com.inspur.dsp.direct.entity.vo.GetDuPontInfoVo;
import com.inspur.dsp.direct.service.ViewDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ViewDetailServiceImpl implements ViewDetailService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private ConfirmationTaskMapper confirmationTaskMapper;

    @Autowired
    private DomainDataElementMapper domainDataElementMapper;

    @Autowired
    private NegotiationRecordMapper negotiationRecordMapper;

    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

    @Override
    public GetDuPontInfoVo getDuPontInfo(String dataid) {
        BaseDataElement baseInfo = baseDataElementMapper.selectById(dataid);
        if (baseInfo == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        GetDuPontInfoVo getDuPontInfoVo = new GetDuPontInfoVo();
        BeanUtils.copyProperties(baseInfo, getDuPontInfoVo);
        List<DomainDataElement> domainDataElements = domainDataElementMapper.selectAllByBaseDataelementDataid(dataid);
        getDuPontInfoVo.setChildList(domainDataElements);
        return getDuPontInfoVo;
    }

    @Override
    public BaseDataElement getElementDetail(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (baseDataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        String statusChinese = StatusUtil.getStatusChinese(baseDataElement.getStatus());
        baseDataElement.setStatusChinese(statusChinese);
        return baseDataElement;
    }


    @Override
    public SourceEventRecord getSourceEventRecord(String dataId) {
        SourceEventRecord sourceEventRecord = sourceEventRecordMapper.selectFirstByDataElementIdOrderBySourceDateDesc(dataId);
        if (sourceEventRecord != null) {
            return sourceEventRecord;
        }
        return new SourceEventRecord();
    }

    @Override
    public List<ConfirmationTask> getCollectUnitList(String dataId) {
        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(null, dataId);
        if (confirmationTaskList != null) {
            for (ConfirmationTask confirmationTask : confirmationTaskList) {
                String status = confirmationTask.getStatus();
                String statusChinese = StatusUtil.getStatusChinese(status);
                confirmationTask.setStatusChinese(statusChinese);
            }
            return confirmationTaskList;
        }
        return Collections.emptyList();
    }


    @Override
    public List<FlowNodeDTO> getFlowInfo(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (baseDataElement == null) {
            throw new IllegalArgumentException("数据元不存在");
        }
        String status = baseDataElement.getStatus();
        DataElementStatus dataElementStatus;
        try {
            dataElementStatus = DataElementStatus.fromString(status);
        } catch (IllegalArgumentException e) {
            throw e; // 或者根据需求处理异常
        }
        switch (dataElementStatus) {
            case PENDING_SOURCE:
                return getNotStartedFlow(dataId);
            case CONFIRMING:
                return getConfirmingFlow(dataId);
            case CLAIMED_ING:
                return getClaimingFlow(dataId);
            case PENDING_APPROVAL:
                return getToBeVerifiedFlow(dataId);
            case PENDING_NEGOTIATION:
                return getToBeNegotiatedFlow(dataId);
            case NEGOTIATING:
                return getNegotiatingFlow(dataId);
            default:
                throw new IllegalArgumentException("不支持的数据元状态: " + dataElementStatus);
        }
    }

    private List<FlowNodeDTO> getNotStartedFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"pending_source".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitList = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitList.size() < 1) {
            throw new RuntimeException("数据元采集单位丢失!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个固定节点
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方-业务员");   // TODO:
        node1.setNodeShowStatus(1);
        flow.add(node1);

        // 第二个节点（根据采集单位数量动态设置）
        FlowNodeDTO node2 = new FlowNodeDTO();
        if (domainSourceUnitList.size() == 1) {
            node2.setSeqNo(1);
            node2.setNodeName("采集单位确认");
            node2.setNodeHandleUserName(String.format("%s-业务员", domainSourceUnitList.get(0).getSourceUnitName()));
            node2.setNodeShowStatus(0);
        } else {
            node2.setSeqNo(1);
            node2.setNodeName("采集单位认领");
            StringBuilder userNames = new StringBuilder();
            for (DomainSourceUnitInfo unit : domainSourceUnitList) {
                if (userNames.length() > 0) {
                    userNames.append(",");
                }
                userNames.append(unit.getSourceUnitName());
            }
            node2.setNodeHandleUserName(userNames.toString());
            node2.setNodeShowStatus(0);
        }
        flow.add(node2);

        // 第三个固定节点
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("核定数源单位");
        node3.setNodeHandleUserName("组织方-业务员");
        node3.setNodeShowStatus(0);
        flow.add(node3);

        return flow;
    }

    private List<FlowNodeDTO> getConfirmingFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"confirming".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitList = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitList.size() != 1) {
            throw new RuntimeException("数据元采集单位数量异常!");
        }

        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(baseDataElement.getStatus(), dataId);
        if (confirmationTaskList.size() != 1) {
            throw new RuntimeException("定源任务数量异常!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个节点（从确认任务中提取）
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方业务员");
        node1.setPassDate(baseDataElement.getSendDate());
        node1.setNodeShowStatus(2);
        flow.add(node1);

        // 第二个节点（从采集单位信息中提取）
        FlowNodeDTO node2 = new FlowNodeDTO();
        node2.setSeqNo(1);
        node2.setNodeName("采集单位确认");
        node2.setNodeHandleUserName(String.format("%s-业务员", domainSourceUnitList.get(0).getSourceUnitName()));
        node2.setNodeShowStatus(1);
        flow.add(node2);

        // 第三个固定节点
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("核定数源单位");
        node3.setNodeHandleUserName("组织方-业务员");
        node3.setNodeShowStatus(0);
        flow.add(node3);

        return flow;
    }

    private List<FlowNodeDTO> getClaimingFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"claimed_ing".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitList = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitList.size() < 2) {
            throw new RuntimeException("数据元采集单位丢失!");
        }

        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(baseDataElement.getStatus(), dataId);
        if (confirmationTaskList.size() < 2) {
            throw new RuntimeException("定源任务丢失!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个节点（从确认任务中提取）
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方业务员");
        // 获取最晚的发送时间
        node1.setPassDate(baseDataElement.getSendDate());
        node1.setNodeShowStatus(2);
        flow.add(node1);

        // 第二个固定节点
        FlowNodeDTO node2 = new FlowNodeDTO();
        node2.setSeqNo(1);
        node2.setNodeName("采集单位认领");
        node2.setNodeHandleUserName("认领中");
        node2.setNodeShowStatus(1);
        flow.add(node2);

        // 第三个固定节点
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("核定数源单位");
        node3.setNodeHandleUserName("组织方-业务员");
        node3.setNodeShowStatus(0);
        flow.add(node3);
        return flow;
    }

    private List<FlowNodeDTO> getToBeVerifiedFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"pending_approval".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitInfos = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitInfos.isEmpty()) {
            throw new RuntimeException("数据元采集单位丢失!");
        }

        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(baseDataElement.getStatus(), dataId);
        if (confirmationTaskList.isEmpty()) {
            throw new RuntimeException("定源任务丢失!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个节点（从确认任务中提取）
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方业务员");
        // 获取最晚的发送时间
        node1.setPassDate(baseDataElement.getSendDate());
        node1.setNodeShowStatus(2);
        flow.add(node1);

        // 第二个节点（根据确认任务数量和处理结果动态设置）
        FlowNodeDTO node2 = new FlowNodeDTO();
        if (confirmationTaskList.size() == 1) {
            ConfirmationTask task = confirmationTaskList.get(0);
            node2.setSeqNo(1);
            node2.setNodeName("采集单位确认");
            node2.setNodeHandleUserName(task.getProcessingUnitName());
            node2.setPassDate(task.getProcessingDate());
            node2.setNodeResult("0");
            // node2.setNodeFeedback("确认本单位可以成为数源单位");
            node2.setNodeFeedback("");
            node2.setNodeShowStatus(2);
        } else {
            // 获取状态为"确认"的任务
            ConfirmationTask confirmedTask = confirmationTaskList.stream()
                    .filter(task -> "claimed".equals(task.getStatus()))
                    .findFirst()
                    .orElse(null);

            if (confirmedTask != null) {
                node2.setSeqNo(1);
                node2.setNodeName("采集单位认领");
                node2.setNodeHandleUserName(confirmedTask.getProcessingUnitName());
                node2.setPassDate(confirmedTask.getProcessingDate());
                node2.setNodeResult("2");
                // node2.setNodeFeedback("单独认领");
                node2.setNodeFeedback("");
                node2.setNodeShowStatus(2);
            }
        }
        flow.add(node2);

        // 第三个固定节点
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("核定数源单位");
        node3.setNodeHandleUserName("组织方-业务员");
        node3.setNodeShowStatus(1);
        flow.add(node3);

        return flow;
    }

    private List<FlowNodeDTO> getToBeNegotiatedFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"pending_negotiation".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitList = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitList.size() < 1) {
            throw new RuntimeException("数据元采集单位丢失!");
        }

        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(baseDataElement.getStatus(), dataId);
        if (confirmationTaskList.size() < 1) {
            throw new RuntimeException("定源任务丢失!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个节点（从确认任务中提取）
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方业务员");
        // 获取最晚的发送时间
        node1.setPassDate(baseDataElement.getSendDate());
        node1.setNodeShowStatus(2);
        flow.add(node1);

        // 第二个节点（根据确认任务数量和处理结果动态设置）
        FlowNodeDTO node2 = new FlowNodeDTO();
        if (confirmationTaskList.size() == 1) {
            ConfirmationTask task = confirmationTaskList.get(0);
            node2.setSeqNo(1);
            node2.setNodeName("采集单位确认");
            node2.setNodeHandleUserName(task.getProcessingUnitName());
            node2.setPassDate(task.getProcessingDate());
            node2.setNodeResult("1");
            node2.setNodeFeedback(task.getProcessingOpinion());
            node2.setNodeShowStatus(2);
        } else {
            // 统计确认和拒绝的任务数量
            long confirmedCount = confirmationTaskList.stream()
                    .filter(task -> "claimed".equals(task.getStatus()))
                    .count();

            long rejectedCount = confirmationTaskList.stream()
                    .filter(task -> "not_claimed".equals(task.getStatus()))
                    .count();

            node2.setSeqNo(1);
            node2.setNodeName("采集单位认领");

            if (confirmedCount > 1) {
                // 多个单位确认
                StringBuilder userNames = new StringBuilder();
                for (ConfirmationTask task : confirmationTaskList) {
                    if ("claimed".equals(task.getStatus())) {
                        userNames.append(task.getProcessingUnitName());
                    }
                }
                if (userNames.length() > 0) {
                    node2.setNodeHandleUserName(userNames.deleteCharAt(userNames.length() - 1).toString());
                } else {
                    node2.setNodeHandleUserName(userNames.toString());
                }
                node2.setNodeResult("3");
            } else if (rejectedCount == confirmationTaskList.size()) {
                // 所有单位都拒绝
                StringBuilder userNames = new StringBuilder();
                for (ConfirmationTask task : confirmationTaskList) {
                    userNames.append(task.getProcessingUnitName());
                }
                node2.setNodeHandleUserName(userNames.toString());
                node2.setNodeResult("4");
            }
            node2.setNodeShowStatus(2);
        }
        flow.add(node2);

        // 第三个固定节点
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("发起协商");
        node3.setNodeHandleUserName("组织方-业务员");
        node3.setNodeShowStatus(1);
        flow.add(node3);

        // 第四个固定节点
        FlowNodeDTO node4 = new FlowNodeDTO();
        node4.setSeqNo(3);
        node4.setNodeName("录入数源单位");
        node4.setNodeHandleUserName("组织方-业务员");
        node4.setNodeShowStatus(0);
        flow.add(node4);

        return flow;
    }

    private List<FlowNodeDTO> getNegotiatingFlow(String dataId) {
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataId);
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!");
        }

        if (!"negotiating".equals(baseDataElement.getStatus())) {
            throw new RuntimeException("数据元状态不符合要求!");
        }

        List<DomainSourceUnitInfo> domainSourceUnitList = domainDataElementMapper.selectSourceUnitInfoByBaseDataid(Collections.singletonList(dataId));
        if (domainSourceUnitList.size() < 1) {
            throw new RuntimeException("数据元采集单位丢失!");
        }

        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(baseDataElement.getStatus(), Collections.singletonList(dataId));
        if (confirmationTaskList.size() < 1) {
            throw new RuntimeException("定源任务丢失!");
        }

        NegotiationRecord negotiationRecord = negotiationRecordMapper.selectFirstByBaseDataelementDataid(dataId);
        if (Objects.isNull(negotiationRecord)) {
            throw new RuntimeException("协商记录不存在!");
        }

        List<FlowNodeDTO> flow = new ArrayList<>();

        // 第一个节点（从确认任务中提取）
        FlowNodeDTO node1 = new FlowNodeDTO();
        node1.setSeqNo(0);
        node1.setNodeName("发起定源");
        node1.setNodeHandleUserName("组织方业务员");
        // 获取最晚的发送时间
        node1.setPassDate(baseDataElement.getSendDate());
        node1.setNodeShowStatus(2);
        flow.add(node1);

        // 第二个节点（根据确认任务数量和处理结果动态设置）
        FlowNodeDTO node2 = new FlowNodeDTO();
        if (confirmationTaskList.size() == 1) {
            ConfirmationTask task = confirmationTaskList.get(0);
            node2.setSeqNo(1);
            node2.setNodeName("采集单位确认");
            node2.setNodeHandleUserName(task.getProcessingUnitName());
            node2.setPassDate(task.getProcessingDate());
            node2.setNodeResult("1");
            node2.setNodeFeedback(task.getProcessingOpinion());
            node2.setNodeShowStatus(2);
        } else {
            // 统计确认和拒绝的任务数量
            long confirmedCount = confirmationTaskList.stream().filter(task -> "claimed".equals(task.getStatus())).count();

            long rejectedCount = confirmationTaskList.stream()
                    .filter(task -> "not_claimed".equals(task.getStatus()))
                    .count();

            node2.setSeqNo(1);
            node2.setNodeName("采集单位认领");

            if (confirmedCount > 1) {
                // 多个单位确认
                StringBuilder userNames = new StringBuilder();
                for (ConfirmationTask task : confirmationTaskList) {
                    if ("claimed".equals(task.getStatus())) {
                        userNames.append(task.getProcessingUnitName()).append(",");
                    }
                }
                if (userNames.length() > 0) {
                    node2.setNodeHandleUserName(userNames.deleteCharAt(userNames.length() - 1).toString());
                } else {
                    node2.setNodeHandleUserName(userNames.toString());
                }
                node2.setNodeResult("3");
            } else if (rejectedCount == confirmationTaskList.size()) {
                // 所有单位都拒绝
                StringBuilder userNames = new StringBuilder();
                for (ConfirmationTask task : confirmationTaskList) {
                    userNames.append(task.getProcessingUnitName()).append(",");
                }
                if (userNames.length() > 0) {
                    node2.setNodeHandleUserName(userNames.deleteCharAt(userNames.length() - 1).toString());
                } else {
                    node2.setNodeHandleUserName(userNames.toString());
                }
                node2.setNodeResult("4");
            }
            node2.setNodeShowStatus(2);
        }
        flow.add(node2);

        // 第三个节点（从协商记录中提取）
        FlowNodeDTO node3 = new FlowNodeDTO();
        node3.setSeqNo(2);
        node3.setNodeName("发起协商");
        node3.setPassDate(negotiationRecord.getSendDate());
        node3.setNodeShowStatus(2);
        node3.setNodeHandleUserName("组织方-" + negotiationRecord.getSenderName());
        flow.add(node3);

        // 第四个固定节点
        FlowNodeDTO node4 = new FlowNodeDTO();
        node4.setSeqNo(3);
        node4.setNodeName("录入数源单位");
        node4.setNodeHandleUserName("组织方-业务员");
        node4.setNodeShowStatus(1);
        flow.add(node4);

        return flow;
    }
}
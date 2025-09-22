package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.common.utils.CollectionUtils;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TaskTypeEnums;
import com.inspur.dsp.direct.service.BaseDataElementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaseDataElementServiceImpl implements BaseDataElementService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;

    /**
     * 认领任务处理完成后,更新基准数据元状态信息
     *
     * @param dataid
     */
    @Override
    public void updateBaseDataElementStatusByClaimTask(String dataid) {
        // 查询基准数据元对应的认领任务数据
        List<ConfirmationTask> claimTasks = confirmationTaskMapper
                .selectAllByTasktypeAndBaseDataelementDataid(TaskTypeEnums.CLAIM_TASK.getCode(), dataid);

        // 无认领任务,退出方法
        if (CollectionUtils.isEmpty(claimTasks)) {
            log.warn("无认领任务,退出方法[updateBaseDataElementStatusByClaimTask]");
            return;
        }

        // 有认领任务,判断认领任务状态
        long claimedCount = claimTasks.stream()
                .filter(task -> ConfirmationTaskEnums.CLAIMED.getCode().equals(task.getStatus()))
                .count();

        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        if (baseDataElement == null) {
            log.warn("无基准数据元,退出方法[updateBaseDataElementStatusByClaimTask]");
            return;
        }

        // 有只仅有一个已认领状态的认领任务,更新基准数据元状态为待核定
        if (claimedCount == 1) {
            baseDataElement.setStatus(StatusEnums.PENDING_APPROVAL.getCode());
            baseDataElementMapper.updateById(baseDataElement);
        }
        // 有多个已认领状态的认领任务或者所有任务都没人认领,更新基准数据元状态为待协商
        else {
            baseDataElement.setStatus(StatusEnums.PENDING_NEGOTIATION.getCode());
            baseDataElementMapper.updateById(baseDataElement);
        }
        log.info("[updateBaseDataElementStatusByClaimTask]更新基准数据元状态成功");
    }
}

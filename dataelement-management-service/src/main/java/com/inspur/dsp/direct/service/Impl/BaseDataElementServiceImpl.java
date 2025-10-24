package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.common.utils.CollectionUtils;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dao.DataElementContactMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.dbentity.DataElementContact;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TaskTypeEnums;
import com.inspur.dsp.direct.service.BaseDataElementService;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaseDataElementServiceImpl implements BaseDataElementService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;
    private final CommonService commonService;
    private final DataElementContactMapper dataElementContactMapper;

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

        // 判断认领任务状态, 任务中存在  ConfirmationTaskEnums.PENDING_CLAIMED 状态的,退出方法, 因为只要有一个认领任务未完成,则不能更新状态
        long count = claimTasks.stream().filter(task -> ConfirmationTaskEnums.PENDING_CLAIMED.getCode().equals(task.getStatus())).count();
        if (count > 0) {
            log.warn("有认领任务未完成,退出方法[updateBaseDataElementStatusByClaimTask]");
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

    /**
     * 数据元已定源后,查询数源单位的联系人信息,记录在data_element_contact表中
     *
     * @param dataid
     */
    @Override
    public void insertDataElementContact(String dataid) {
        // 查询基准数据元
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
        // 判断不为空且状态为已定源
        if (baseDataElement != null && StatusEnums.DESIGNATED_SOURCE.getCode().equals(baseDataElement.getStatus())) {
            // 取出数源单位code
            String sourceUnitCode = baseDataElement.getSourceUnitCode();
            // 查询数源单位的信息
            OrganizationUnit sourceUnit = commonService.getOrgInfoByOrgCode(sourceUnitCode);
            // 构建基准数据元联系人信息保存实体
            DataElementContact dataElementContact = DataElementContact.builder()
                    .dataid(UUID.randomUUID().toString())
                    .baseDataelementDataid(dataid)
                    .contactname(sourceUnit.getContactName())
                    .contacttel(sourceUnit.getContactPhone())
                    .build();
            // 保存联系人信息
            dataElementContactMapper.insert(dataElementContact);
            log.info("[insertDataElementContact]保存数据元联系人信息成功");
            // 退出方法
            return;
        }
        log.warn("基准数据元不存在或状态不是已定源,退出方法[insertDataElementContact]");
    }
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.constant.Constants;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dao.DomainDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.dbentity.OrganizationUnit;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.GetPendingApprovalPageDto;
import com.inspur.dsp.direct.entity.excel.ConfirmationExcel;
import com.inspur.dsp.direct.entity.excel.PaExcel;
import com.inspur.dsp.direct.entity.vo.GetPendingApprovalPageVo;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.PaPageSortFieldEnums;
import com.inspur.dsp.direct.enums.RecordSourceTypeEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.enums.TaskTypeEnums;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.VerifiedDsService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerifiedDsServiceImpl implements VerifiedDsService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;
    private final DomainDataElementMapper domainDataElementMapper;
    private final SourceEventRecordMapper sourceEventRecordMapper;
    private final CommonService commonService;

    @Override
    public Page<GetPendingApprovalPageVo> getPaPage(GetPendingApprovalPageDto dto) {
        // 排序处理
        String sortSql = PaPageSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
        Page<GetPendingApprovalPageVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<GetPendingApprovalPageVo> vos;
        if (StatusEnums.PENDING_APPROVAL.getCode().equals(dto.getAuditStatus())) {
            // 查询待核定数据
            vos = baseDataElementMapper.selectPaPage(page, dto, sortSql);
        } else {
            // 查询已定源数据
            vos = baseDataElementMapper.selectConfirmedPage(page, dto, sortSql);
        }
        // 数据填充
        vos.forEach(vo -> {
            // 状态详情
            vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        });
        page.setRecords(vos);
        return page;
    }

    @Override
    public void exportPaData(GetPendingApprovalPageDto dto, HttpServletResponse response) {
        try {
            // 排序处理
            String sortSql = PaPageSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());
            List<GetPendingApprovalPageVo> vos;
            if ("pending_approval".equals(dto.getAuditStatus())) {
                // 查询待核定数据
                vos = baseDataElementMapper.selectPaPage(null, dto, sortSql);
                // 转为待核定导出对象
                List<PaExcel> paExcelList = new ArrayList<>();
                for (int i = 0, vosSize = vos.size(); i < vosSize; i++) {
                    GetPendingApprovalPageVo vo = vos.get(i);
                    PaExcel build = PaExcel.builder()
                            .index(i + 1)
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .statusDesc(StatusEnums.getDescByCode(vo.getStatus()))
                            .paUnitName(vo.getPaUnitName())
                            .sendDate(vo.getSendDate())
                            .build();
                    paExcelList.add(build);
                }
                // 使用EasyExcel导出
                commonService.exportExcelData(paExcelList, response,"待核定基准数据元清单", PaExcel.class);
            } else {
                // 查询已定源数据
                vos = baseDataElementMapper.selectConfirmedPage(null, dto, sortSql);
                // 转为已定源导出对象
                List<ConfirmationExcel> confirmationExcels = new ArrayList<>();
                for (int i = 0; i < vos.size(); i++) {
                    GetPendingApprovalPageVo vo = vos.get(i);
                    ConfirmationExcel build = ConfirmationExcel.builder()
                            .index(i + 1)
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .collectunitqty(vo.getCollectunitqty())
                            .statusDesc(StatusEnums.getDescByCode(vo.getStatus()))
                            .sendDate(vo.getSendDate())
                            .sourceUnitName(vo.getSourceUnitName())
                            .confirmDate(vo.getConfirmDate())
                            .build();
                    confirmationExcels.add(build);
                }
                // 使用EasyExcel导出
                commonService.exportExcelData(confirmationExcels, response,"已定源基准数据元清单", ConfirmationExcel.class);
            }
        } catch (Exception e) {
            log.error("导出数据失败", e);
            throw new RuntimeException("导出数据失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchVerification(List<String> dataids) {
        // 校验参数集合
        if (dataids == null || dataids.isEmpty()) {
            throw new RuntimeException("请选择要核定的基准数据元");
        }

        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 查询所有基准数据元信息
        List<BaseDataElement> baseList = baseDataElementMapper.selectBatchIds(dataids);

        // 查询已认领的任务
        List<ConfirmationTask> claimedTasks = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(ConfirmationTaskEnums.CLAIMED.getCode(), dataids);

        // 查询已确认的任务
        List<ConfirmationTask> confirmedTasks = confirmationTaskMapper.selectAllByStatusAndBaseDataelementDataidIn(ConfirmationTaskEnums.CONFIRMED.getCode(), dataids);

        // 合并任务列表
        List<ConfirmationTask> taskList = new ArrayList<>();
        taskList.addAll(claimedTasks);
        taskList.addAll(confirmedTasks);

        // 处理taskList为Map格式
        Map<String, ConfirmationTask> taskMap = taskList.stream()
                .collect(Collectors.toMap(ConfirmationTask::getBaseDataelementDataid, task -> task, (existing, replacement) -> existing));

        // 新建更新基准数据元集合
        List<BaseDataElement> updateBaseData = new ArrayList<>();

        // 新建定源事件记录集合
        List<SourceEventRecord> addEventRecord = new ArrayList<>();

        // 遍历baseList，组装基准数据元更新数据，创建定源事件记录表数据
        for (BaseDataElement baseInfo : baseList) {
            // 从taskMap取出关联的任务对象
            ConfirmationTask task = taskMap.get(baseInfo.getDataid());

            if (task != null) {
                // 创建基准数据元更新对象
                BaseDataElement updateBase = BaseDataElement.builder()
                        .dataid(baseInfo.getDataid())
                        .status(StatusEnums.DESIGNATED_SOURCE.getCode())
                        .lastModifyDate(new Date())
                        .confirmDate(new Date())
                        .lastModifyAccount(userInfo.getAccount())
                        .sourceUnitCode(task.getProcessingUnitCode())
                        .sourceUnitName(task.getProcessingUnitName())
                        .build();
                updateBaseData.add(updateBase);

                // 创建定源事件记录对象
                String sourceType = Constants.EMPTY_STRING;
                if (TaskTypeEnums.CONFIRMATION_TASK.getCode().equals(task.getTasktype())) {
                    sourceType = RecordSourceTypeEnums.CONFIRMATION_APPROVAL.getCode();
                }
                if (TaskTypeEnums.CLAIM_TASK.getCode().equals(task.getTasktype())) {
                    sourceType = RecordSourceTypeEnums.CLAIM_APPROVAL.getCode();
                }
                // 获取部门信息, 获取部门联系电话, 获取部门联系人
                OrganizationUnit unit = commonService.getOrgInfoByOrgCode(userInfo.getOrgCode());

                SourceEventRecord eventRecord = SourceEventRecord.builder()
                        .recordId(UUID.randomUUID().toString())
                        .dataElementId(baseInfo.getDataid())
                        .dataElementName(baseInfo.getName())
                        .sourceType(sourceType)
                        .sourceDate(new Date())
                        .operatorAccount(userInfo.getAccount())
                        .sourceUnitCode(task.getProcessingUnitCode())
                        .sourceUnitName(task.getProcessingUnitName())
                        .sendUnitCode(userInfo.getOrgCode())
                        .sendUnitName(userInfo.getOrgName())
                        .contactPhone(unit.getContactPhone())
                        .contactName(userInfo.getName())
                        .build();
                addEventRecord.add(eventRecord);
            }
        }

        // 批量更新基准数据元信息
        baseDataElementMapper.updateById(updateBaseData);

        // 批量插入定源记录
        sourceEventRecordMapper.insert(addEventRecord);
    }
}
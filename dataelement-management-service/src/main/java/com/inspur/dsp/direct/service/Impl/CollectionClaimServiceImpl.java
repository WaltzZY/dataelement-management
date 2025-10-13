package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.CollectionClaim.CollectionBaseDataElementMapper;
import com.inspur.dsp.direct.dao.CollectionClaim.CollectionGetDataPendingAndProcessedSourceMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.ClaimOrRejectDTO;
import com.inspur.dsp.direct.entity.dto.GetDataPendingAndProcessedSourceDTO;
import com.inspur.dsp.direct.entity.excel.CollectionProcessedExcel;
import com.inspur.dsp.direct.entity.excel.CollectionProcessingExcel;
import com.inspur.dsp.direct.entity.vo.GetDataPendingAndProcessedSourceVO;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.PapsSortFieldEnums;
import com.inspur.dsp.direct.service.BaseDataElementService;
import com.inspur.dsp.direct.service.CollectionClaimService;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 采集方认领服务实现类
 */
@Service
@RequiredArgsConstructor
public class CollectionClaimServiceImpl implements CollectionClaimService {

    private final CollectionGetDataPendingAndProcessedSourceMapper getDataPendingAndProcessedSourceMapper;

    private final ConfirmationTaskMapper confirmationTaskMapper;

    private final CollectionBaseDataElementMapper baseDataElementMapper;

    private final BaseDataElementService baseDataElementService;

    private final CommonService commonService;

    @Override
    public Page<GetDataPendingAndProcessedSourceVO> getDataPendingAndProcessedSource(GetDataPendingAndProcessedSourceDTO dto) {

        // 获取当前登录用户
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String orgCode = userInfo.getOrgCode();

        String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());

        Page<GetDataPendingAndProcessedSourceVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        if (dto.getSendDateBegin() != null) {
            dto.setSendDateBegin(dto.getSendDateBegin().toLocalDate().atStartOfDay());
        }
        if (dto.getSendDateEnd() != null) {
            dto.setSendDateEnd(dto.getSendDateEnd().toLocalDate().plusDays(1).atStartOfDay());
        }
        if (dto.getProcessDateBegin() != null) {
            dto.setProcessDateBegin(dto.getProcessDateBegin().toLocalDate().atStartOfDay());
        }
        if (dto.getProcessDateEnd() != null) {
            dto.setProcessDateEnd(dto.getProcessDateEnd().toLocalDate().plusDays(1).atStartOfDay());
        }

        List<GetDataPendingAndProcessedSourceVO> dataList = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(page,dto,orgCode, orderBySql);

        for (GetDataPendingAndProcessedSourceVO vo : dataList) {
            vo.setTaskStatusDesc(ConfirmationTaskEnums.getDescByCode(vo.getTaskStatus()));
        }


        return page.setRecords(dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimOrReject(ClaimOrRejectDTO dto) {
        // 1.获取当前登陆人信息，方便更新任务的处理人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        Date now = new Date();
        // 2.遍历dto中的认领任务map,处理数据
        Map<String, String> idTaskId = dto.getIdTaskId();
        idTaskId.forEach((taskId, dataid) -> {
            // ct后五个字段 + status更新 同步改xml
            ConfirmationTask task = new ConfirmationTask();
            task.setTaskId(taskId);
            task.setProcessingDate(now);
            task.setProcessorAccount(userInfo.getAccount());
            task.setProcessorName(userInfo.getName());
            // 3.根据前端传入的operation,判断是认领还是拒绝
            String operation = dto.getOperation();
            if (ConfirmationTaskEnums.CLAIMED.getCode().equals(operation)) {
                task.setProcessingResult("同意认领");
                task.setStatus(ConfirmationTaskEnums.CLAIMED.getCode());
            } else {
                task.setProcessingOpinion(dto.getInstruction());
                task.setProcessingResult("不同意认领");
                task.setStatus(ConfirmationTaskEnums.NOT_CLAIMED.getCode());
            }
            // 4.更新任务
            confirmationTaskMapper.updateById(task);
            // 5.调用公共的认领后基准数据元状态处理方法
            baseDataElementService.updateBaseDataElementStatusByClaimTask(dataid);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportData(GetDataPendingAndProcessedSourceDTO dto, HttpServletResponse response) {
        try {

            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
            String orgCode = userInfo.getOrgCode();

            String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());

            if (dto.getStatus() != null && dto.getStatus().contains("pending_claimed")) {
                List<GetDataPendingAndProcessedSourceVO> collectionPending = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(null,dto,orgCode, orderBySql);

                AtomicInteger i = new AtomicInteger(0); // 导出列表序号
                List<CollectionProcessingExcel> processingExcelList = collectionPending.stream().map(vo -> {
                    return CollectionProcessingExcel.builder()
                            .seq(String.valueOf(i.incrementAndGet()))
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .type(vo.getDatatype())
                            .status(ConfirmationTaskEnums.getDescByCode(vo.getTaskStatus()))
                            .sendDate(vo.getSend_date())
                            .build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(processingExcelList, response, "待认领基准数据元清单", CollectionProcessingExcel.class);

            }else if (dto.getStatus() != null && dto.getStatus().contains("claimed") && dto.getStatus().contains("not_claimed")){
                List<GetDataPendingAndProcessedSourceVO> collectionProcessed = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(null,dto,orgCode, orderBySql);

                AtomicInteger j = new AtomicInteger(0); // 导出列表序号
                List<CollectionProcessedExcel> processedExcelList = collectionProcessed.stream().map(vo -> {
                    return CollectionProcessedExcel.builder()
                            .seq(String.valueOf(j.incrementAndGet()))
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .type(vo.getDatatype())
                            .status(ConfirmationTaskEnums.getDescByCode(vo.getTaskStatus()))
                            .sendDate(vo.getSend_date())
                            .processDate(vo.getProcessingDate())
                            .build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(processedExcelList, response, "已处理基准数据元（认领型）清单", CollectionProcessedExcel.class);
            }
        }catch (Exception e) {  throw new RuntimeException("导出数据失败");}
    }
}
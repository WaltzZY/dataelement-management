package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.CollectionClaim.CollectionBaseDataElementMapper;
import com.inspur.dsp.direct.dao.CollectionClaim.CollectionConfirmationTaskMapper;
import com.inspur.dsp.direct.dao.CollectionClaim.CollectionGetDataPendingAndProcessedSourceMapper;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.ClaimOrRejectDTO;
import com.inspur.dsp.direct.entity.dto.GetDataPendingAndProcessedSourceDTO;
import com.inspur.dsp.direct.entity.excel.CollectionProcessedExcel;
import com.inspur.dsp.direct.entity.excel.CollectionProcessingExcel;
import com.inspur.dsp.direct.entity.vo.GetDataPendingAndProcessedSourceVO;
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
import java.util.stream.Collectors;

import static com.inspur.dsp.direct.enums.StatusEnums.CLAIMED;

/**
 * 采集方认领服务实现类
 */
@Service
@RequiredArgsConstructor
public class CollectionClaimServiceImpl implements CollectionClaimService {

    private final CollectionGetDataPendingAndProcessedSourceMapper getDataPendingAndProcessedSourceMapper;

    private final CollectionConfirmationTaskMapper confirmationTaskMapper;

    private final CollectionBaseDataElementMapper baseDataElementMapper;

    private final BaseDataElementService baseDataElementService;

    private final CommonService commonService;

    @Override
    public Page<GetDataPendingAndProcessedSourceVO> getDataPendingAndProcessedSource(GetDataPendingAndProcessedSourceDTO dto) {

        // 获取当前登录用户
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        String orgCode = userInfo.getOrgCode();

        Page<GetDataPendingAndProcessedSourceVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<GetDataPendingAndProcessedSourceVO> dataList = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(page,dto,orgCode);
        return page.setRecords(dataList);
    }

    @Override
    public void claimOrReject(ClaimOrRejectDTO dto) {
        try {

            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

            Date now = new Date();
            // 遍历处理每个任务ID
            for (String taskId : dto.getIdTaskId().keySet()) {


                // ct后五个字段 + status更新 同步改xml
                ConfirmationTask task = new ConfirmationTask();

                task.setTaskId(taskId);
                task.setProcessingDate(now);
                // TODO : 处理结果放什么
                task.setProcessingResult("");
                task.setProcessingOpinion(dto.getInstruction());
                task.setProcessorAccount(userInfo.getAccount());
                task.setProcessorName(userInfo.getName());
                task.setStatus(CLAIMED.getCode());
                task.setProcessingOpinion(dto.getInstruction());

                confirmationTaskMapper.updateConfirmationTaskStatus(task);

                baseDataElementService.updateBaseDataElementStatusByClaimTask(taskId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportData(GetDataPendingAndProcessedSourceDTO dto, HttpServletResponse response) {
        try {

            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
            String orgCode = userInfo.getOrgCode();

            if (dto.getStatus() != null && dto.getStatus().contains("pending")) {
                List<GetDataPendingAndProcessedSourceVO> collectionPending = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(null,dto,orgCode);

                List<CollectionProcessingExcel> processingExcelList = collectionPending.stream().map(vo -> {
                    return CollectionProcessingExcel.builder()
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .type(vo.getDatatype())
                            .status(vo.getStatus())
                            .sendDate(vo.getSend_date())
                            .build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(processingExcelList, response, "采集方待认领-待处理数据元列表", CollectionProcessingExcel.class);

            }else if (dto.getStatus() != null && dto.getStatus().contains("claimed") && dto.getStatus().contains("not_claimed")){
                List<GetDataPendingAndProcessedSourceVO> collectionProcessed = getDataPendingAndProcessedSourceMapper.getDataPendingAndProcessedData(null,dto,orgCode);

                List<CollectionProcessedExcel> processedExcelList = collectionProcessed.stream().map(vo -> {
                    return CollectionProcessedExcel.builder()
                            .name(vo.getName())
                            .definition(vo.getDefinition())
                            .type(vo.getDatatype())
                            .status(vo.getStatus())
                            .sendDate(vo.getSend_date())
                            .processDate(vo.getProcessing_date())
                            .build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(processedExcelList, response, "采集方待认领-已处理数据元列表", CollectionProcessedExcel.class);
            }
        }catch (Exception e) {  throw new RuntimeException("导出数据失败");}
    }
}
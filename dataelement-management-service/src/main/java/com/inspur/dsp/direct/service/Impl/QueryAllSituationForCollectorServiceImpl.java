package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.QueryAllSituationForCollectorExportDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.QueryAllSituationForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 采集方查询整体情况服务实现类
 *
 * @author system
 */
@Slf4j
@Service
public class QueryAllSituationForCollectorServiceImpl implements QueryAllSituationForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private CommonService commonService;

    /**
     * 分页查询列表数据
     */
    @Override
    public Page<DataElementWithTaskVo> getAllSituationList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        Page<DataElementWithTaskVo> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());
        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        baseDataElementSearchDTO.setOrgCode(orgCode);
        List<String> statusList = baseDataElementSearchDTO.getStatusList();
        if (statusList != null && !statusList.isEmpty()) {
            List<String>[] lists = StatusUtil.buildStatusConditions(statusList);
            List<String> baseList = lists[0];
            List<String> taskList = lists[1];
            baseDataElementSearchDTO.setBaseStatusList(baseList);
            baseDataElementSearchDTO.setTaskStatusList(taskList);
        }
        List<DataElementWithTaskVo> baseDataElementList = baseDataElementMapper.getDetermineResultListWithOrganiser(page, baseDataElementSearchDTO);
        // 校验结果
        if (CollectionUtils.isEmpty(baseDataElementList)) {
            page.setRecords(Collections.emptyList());
        }
        for (DataElementWithTaskVo dataElementWithTaskVo : baseDataElementList) {
            String status = dataElementWithTaskVo.getStatus();
            String statusChinese = StatusUtil.getStatusChinese(status);
            dataElementWithTaskVo.setStatusChinese(statusChinese);
        }
        return page.setRecords(baseDataElementList);
    }


//    /**
//     * 查询基准数据详情数据
//     */
//    @Override
//    public BaseDataElementDTO getAllSituationDetail(BaseDataElementSearchDTO baseDataElementSearchDTO) {
//        // 查询基准数据元详情
//        BaseDataElement baseDataElement = baseDataElementMapper.getAllSituationDetail(baseDataElementSearchDTO);
//        // 如果实体为空，返回空对象
//        if (baseDataElement == null) {
//            return new BaseDataElementDTO();
//        }
//        BaseDataElementDTO baseDataElementDTO = new BaseDataElementDTO();
//        BeanUtils.copyProperties(baseDataElement, baseDataElementDTO);
//
//        // 查询关联的确认任务
//        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.getTaskByDataId(baseDataElementSearchDTO);
//        // 如果确认任务不为空，设置到基准数据元中
//        if (!CollectionUtils.isEmpty(confirmationTaskList)) {
//            baseDataElementDTO.setConfirmationTaskDTOList(Collections.EMPTY_LIST);
//        }
//        List<ConfirmationTaskDTO> confirmationTaskDTOList = new ArrayList<>();
//        for (ConfirmationTask confirmationTask : confirmationTaskList) {
//            ConfirmationTaskDTO confirmationTaskDTO = new ConfirmationTaskDTO();
//            BeanUtils.copyProperties(confirmationTask, confirmationTaskDTO);
//            confirmationTaskDTOList.add(confirmationTaskDTO);
//        }
//        baseDataElementDTO.setConfirmationTaskDTOList(confirmationTaskDTOList);
//        return baseDataElementDTO;
//    }
//
//    /**
//     * 查询基准数据详情数据（带记录详情）
//     */
//    @Override
//    public BaseDataElementDTO getAllSituationWithRecordDetail(BaseDataElementSearchDTO baseDataElementSearchDTO) {
//        // TODO: 需要获取dataId
//
//        // 查询基准数据元详情
//        BaseDataElement baseDataElement = baseDataElementMapper.getAllSituationDetail(baseDataElementSearchDTO);
//        // 如果实体为空，返回空对象
//        if (baseDataElement == null) {
//            return new BaseDataElementDTO();
//        }
//        BaseDataElementDTO baseDataElementDTO = new BaseDataElementDTO();
//        BeanUtils.copyProperties(baseDataElement, baseDataElementDTO);
//
//        // 查询关联的确认任务
//        List<ConfirmationTask> confirmationTaskList = confirmationTaskMapper.getTaskByDataId(baseDataElementSearchDTO);
//        // 如果确认任务不为空，设置到基准数据元中
//        if (!CollectionUtils.isEmpty(confirmationTaskList)) {
//            baseDataElementDTO.setConfirmationTaskDTOList(Collections.EMPTY_LIST);
//        }
//        List<ConfirmationTaskDTO> confirmationTaskDTOList = new ArrayList<>();
//        for (ConfirmationTask confirmationTask : confirmationTaskList) {
//            ConfirmationTaskDTO confirmationTaskDTO = new ConfirmationTaskDTO();
//            BeanUtils.copyProperties(confirmationTask, confirmationTaskDTO);
//            confirmationTaskDTOList.add(confirmationTaskDTO);
//        }
//        // 查询事件记录详情
//        SourceEventRecord sourceEventRecord = sourceEventRecordMapper.getEventRecordByDataId(baseDataElementSearchDTO);
//        // 如果事件记录不为空，设置到基准数据元中
//        if (sourceEventRecord == null) {
//            baseDataElementDTO.setSourceEventRecordDTO(new SourceEventRecordDTO());
//        } else {
//            SourceEventRecordDTO sourceEventRecordDTO = new SourceEventRecordDTO();
//            BeanUtils.copyProperties(sourceEventRecord, sourceEventRecordDTO);
//            baseDataElementDTO.setSourceEventRecordDTO(sourceEventRecordDTO);
//        }
//        return baseDataElementDTO;
//    }

    /**
     * 下载数据
     */
    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {

        // 获取当前登录用户信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        baseDataElementSearchDTO.setOrgCode(orgCode);
        List<String> statusList = baseDataElementSearchDTO.getStatusList();
        if (statusList != null && !statusList.isEmpty()) {
            List<String>[] lists = StatusUtil.buildStatusConditions(statusList);
            List<String> baseList = lists[0];
            List<String> taskList = lists[1];
            baseDataElementSearchDTO.setBaseStatusList(baseList);
            baseDataElementSearchDTO.setTaskStatusList(taskList);
        }
        List<DataElementWithTaskVo> dataElementWithTaskVoList = baseDataElementMapper.getDetermineResultListWithOrganiser(baseDataElementSearchDTO);
        // 校验结果
        if (CollectionUtils.isEmpty(dataElementWithTaskVoList)) {
            log.error("导出错误!");
            return;
        }
        List<QueryAllSituationForCollectorExportDTO> exportDTOList = new ArrayList<>();
        for (DataElementWithTaskVo dataElementWithTaskVo : dataElementWithTaskVoList) {
            QueryAllSituationForCollectorExportDTO exportDTO = new QueryAllSituationForCollectorExportDTO();
            String statusChinese = StatusUtil.getStatusChinese(dataElementWithTaskVo.getStatus());
            exportDTO.setDefinition(dataElementWithTaskVo.getDefinition());
            exportDTO.setName(dataElementWithTaskVo.getName());
            exportDTO.setStatus(statusChinese);
            Date date = new Date(0);
            exportDTO.setSendDate(dataElementWithTaskVo.getSendDate() == null ? date : dataElementWithTaskVo.getSendDate());
            exportDTO.setReceiveDate(dataElementWithTaskVo.getReceiveDate() == null ? date : dataElementWithTaskVo.getReceiveDate());
            exportDTO.setProcessDate(dataElementWithTaskVo.getProcessingDate() == null ? date : dataElementWithTaskVo.getProcessingDate());
            exportDTO.setDataType(dataElementWithTaskVo.getDataType());
            exportDTOList.add(exportDTO);
        }

        try {
            commonService.exportExcelData(exportDTOList, response, "组织方-已定源数据", QueryAllSituationForCollectorExportDTO.class);
        } catch (IOException e) {
            log.error("导出数据[组织方-已定源数据]失败", e);
            throw new RuntimeException("导出数据[组织方-已定源数据]失败");
        }

    }
}
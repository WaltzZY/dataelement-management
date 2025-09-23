package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.service.QueryAllSituationForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 采集方查询整体情况服务实现类
 *
 * @author system
 */
@Service
public class QueryAllSituationForCollectorServiceImpl implements QueryAllSituationForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

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

        List<String>[] lists = StatusUtil.buildStatusConditions(statusList);
        List<String> baseList = lists[0];
        List<String> taskList = lists[1];
        baseDataElementSearchDTO.setBaseStatusList(baseList);
        baseDataElementSearchDTO.setTaskStatusList(taskList);
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
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO) {
//        // 查询数据
//        List<BaseDataElement> dataList = baseDataElementMapper.getAllSituationList(baseDataElementSearchDTO);
//
//        // 校验结果
//        if (CollectionUtils.isEmpty(dataList)) {
//            throw new IllegalArgumentException("查询结果为空");
//        }
//
//        // 转换为导出对象
//        List<DataExport> exportList = new ArrayList<>();
//        for (BaseDataElement element : dataList) {
//            // 查询事件记录详情
//            BaseDataElementSearchDTO searchDTO = new BaseDataElementSearchDTO();
//            searchDTO.setDataId(element.getDataid());
//            SourceEventRecord sourceEventRecord = sourceEventRecordMapper.getEventRecordByDataId(searchDTO);
//
//            // 构建导出对象
//            DataExport dataExport = new DataExport();
//            dataExport.setDataId(element.getDataid());
//            dataExport.setName(element.getName());
//            dataExport.setDefinition(element.getDefinition());
//            dataExport.setDatatype(element.getDatatype());
//            dataExport.setSendDate(element.getSendDate() != null ? element.getSendDate() : null);
//
//            // TODO: 设置采集单位数量，需要根据实际业务逻辑确定
//            if (sourceEventRecord != null) {
//                // dataExport.setCollectunitqty(sourceEventRecord.getCollectUnitQty());
//            }
//            exportList.add(dataExport);
//        }
//
//        // 使用EasyExcel导出
//        try {
//            // TODO: 需要配置HttpServletResponse的下载头信息
//            // EasyExcel.write(response.getOutputStream(), DataExport.class)
//            //         .sheet("数据导出")
//            //         .doWrite(exportList);
//        } catch (Exception e) {
//            throw new RuntimeException("数据导出失败", e);
//        }
        return;
    }
}
package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.RefuseDto;
import com.inspur.dsp.direct.entity.vo.CollectDataInfoVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import com.inspur.dsp.direct.enums.CollSortFieldEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.service.CollectService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CollectServiceImpl implements CollectService {

    private final BaseDataElementMapper baseDataElementMapper;
    private final ConfirmationTaskMapper confirmationTaskMapper;

    /**
     * 获取确认数据元列表
     *
     * @param dto
     * @return
     */
    @Override
    public Page<GetCollectDataVo> getCollectDataElementPage(CollectDataElementPageDto dto) {
        // 获取当前登录人信息
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 拿到当前登录人的部门code
        String organCode = userInfo.getOrgCode();
        // 排序sql
        String sortSql = CollSortFieldEnums.getSortFieldSql(dto.getSortField());
        // 查询 base_data_element 表和 domain_data_element 表和 confirmation_task 确认任务表
        // domain_data_element 表确认要显示的内容, base_data_element 表确认数据元的信息和状态, confirmation_task 表确认任务信息,接收时间,处理时间
        Page<GetCollectDataVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<GetCollectDataVo> data = baseDataElementMapper.getCollectData(page, dto, sortSql, organCode);
        return page.setRecords(data);
    }

    /**
     * 拒绝/通过成为数源单位
     *
     * @param dto
     */
    @Override
    public void refuse(RefuseDto dto) {
        // 校验数据元是否存在
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(dto.getDataid());
        if (Objects.isNull(baseDataElement)) {
            throw new RuntimeException("数据元不存在!!!");
        }
        String handleStatus = dto.getHandleStatus();
        String dataStatus = "";
        if (StatusEnums.PENDING_APPROVAL.getCode().equals(handleStatus)) {
            // 待核定状态的数据元就是已确认了
            dataStatus = StatusEnums.PENDING_APPROVAL.getCode();
        }
        if (StatusEnums.REJECTED.getCode().equals(handleStatus)) {
            dataStatus = StatusEnums.REJECTED.getCode();
        }
        if (!StringUtils.hasText(dataStatus)) {
            throw  new RuntimeException("请选择处理状态!");
        }
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 更新数据元状态
        baseDataElement.setStatus(dataStatus);
        baseDataElement.setLastModifyAccount(userInfo.getAccount());
        baseDataElement.setLastModifyDate(new Date());
        baseDataElementMapper.updateById(baseDataElement);
        // 更新数据元确认状态
        ConfirmationTask confirmationTask = confirmationTaskMapper.selectFirstByBaseDataelementDataid(dto.getDataid());
        confirmationTask.setStatus(dataStatus);
        confirmationTask.setProcessingDate(new Date());
        confirmationTask.setProcessingResult(dto.getOpinion());
        confirmationTask.setProcessingOpinion(dto.getOpinion());
        confirmationTask.setProcessorAccount(userInfo.getAccount());
        confirmationTask.setProcessorName(userInfo.getName());
        confirmationTaskMapper.updateById(confirmationTask);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public CollectDataInfoVo info(String id) {
        // 查询数据元详情
        BaseDataElement baseDataElement = baseDataElementMapper.selectById(id);
        if (Objects.isNull(baseDataElement)) {
            throw new IllegalArgumentException("数据元不存在!");
        }
        // 查询数据元任务详情
        ConfirmationTask confirmationTask = confirmationTaskMapper.selectFirstByBaseDataelementDataid(id);
        // 构建响应数据
        CollectDataInfoVo vo = new CollectDataInfoVo();
        vo.setDataElementId(baseDataElement.getDataElementId());
        vo.setDataFormat(baseDataElement.getDataFormat());
        vo.setDataid(baseDataElement.getDataid());
        vo.setDatatype(baseDataElement.getDatatype());
        vo.setDefinition(baseDataElement.getDefinition());
        vo.setName(baseDataElement.getName());
        vo.setPublishDate(baseDataElement.getPublishDate());
        vo.setRefuseReason(confirmationTask.getProcessingOpinion());
        vo.setRemarks(baseDataElement.getRemarks());
        vo.setSendDate(confirmationTask.getSendDate());
        vo.setSenderName(confirmationTask.getSenderName());
        vo.setSourceUnitCode(baseDataElement.getSourceUnitCode());
        vo.setSourceUnitName(baseDataElement.getSourceUnitName());
        vo.setStatus(baseDataElement.getStatus());
        vo.setStatusDesc(StatusEnums.getDescByCode(baseDataElement.getStatus()));
        vo.setTaskId(confirmationTask.getTaskId());
        vo.setValueDomain(baseDataElement.getValueDomain());
        return vo;
    }
}

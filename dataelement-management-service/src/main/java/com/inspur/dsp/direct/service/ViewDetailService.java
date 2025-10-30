package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.entity.dto.FlowNodeDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.entity.vo.GetCollectUnitVo;
import com.inspur.dsp.direct.entity.vo.GetConfirmationTaskVo;
import com.inspur.dsp.direct.entity.vo.GetDuPontInfoVo;

import java.util.List;

public interface ViewDetailService {
    GetDuPontInfoVo getDuPontInfo(String dataId);

    BaseDataElement getElementDetail(String dataId);

    SourceEventRecord getSourceEventRecord(String dataId);

    List<FlowNodeDTO> getFlowInfo(String dataId);

    List<GetCollectUnitVo> getCollectUnitList(String dataId);
    // List<DomainDataElement> getCollectUnitList(String dataId);

    BaseDataElement getElementDetailWithTask(String dataId);

    GetConfirmationTaskVo getConfirmationTask(String dataId);


    DataElementWithTaskVo getElementDetailWithStatus(String dataId);

}
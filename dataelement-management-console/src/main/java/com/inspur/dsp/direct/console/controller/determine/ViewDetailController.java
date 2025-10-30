package com.inspur.dsp.direct.console.controller.determine;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.SourceEventRecord;
import com.inspur.dsp.direct.entity.dto.FlowNodeDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.entity.vo.GetCollectUnitVo;
import com.inspur.dsp.direct.entity.vo.GetConfirmationTaskVo;
import com.inspur.dsp.direct.entity.vo.GetDuPontInfoVo;
import com.inspur.dsp.direct.service.ViewDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/viewCommon")
public class ViewDetailController {

    @Autowired
    private ViewDetailService viewDetailService;

    @RespAdvice
    @GetMapping("/getDuPontInfo/{dataId}")
    public GetDuPontInfoVo getDuPontInfo(@PathVariable("dataId") String dataId) {
        return viewDetailService.getDuPontInfo(dataId);
    }

    @RespAdvice
    @GetMapping("/getElementDetail/{dataId}")
    public BaseDataElement getElementDetail(@PathVariable("dataId") String dataId) {
        return viewDetailService.getElementDetail(dataId);
    }

    @RespAdvice
    @GetMapping("/getElementDetailWithTask/{dataId}")
    public BaseDataElement getElementDetailWithTask(@PathVariable("dataId") String dataId) {
        return viewDetailService.getElementDetailWithTask(dataId);
    }

    @RespAdvice
    @GetMapping("/getElementDetailWithStatus/{dataId}")
    public DataElementWithTaskVo getElementDetailWithStatus(@PathVariable("dataId") String dataId) {
        return viewDetailService.getElementDetailWithStatus(dataId);
    }


    @RespAdvice
    @GetMapping("/getConfirmationTask/{dataId}")
    public GetConfirmationTaskVo getTask(@PathVariable("dataId") String dataId) {
        return viewDetailService.getConfirmationTask(dataId);
    }


    @RespAdvice
    @GetMapping("/getSourceEventRecord/{dataId}")
    public SourceEventRecord getSourceEventRecord(@PathVariable("dataId") String dataId) {
        return viewDetailService.getSourceEventRecord(dataId);
    }


    // @RespAdvice
    // @GetMapping("/getCollectUnitList/{dataId}")
    // public List<DomainDataElement> getCollectUnitList(@PathVariable("dataId") String dataId) {
    //     return viewDetailService.getCollectUnitList(dataId);
    // }

    @RespAdvice
    @GetMapping("/getCollectUnitList/{dataId}")
    public List<GetCollectUnitVo> getCollectUnitList(@PathVariable("dataId") String dataId) {
        return viewDetailService.getCollectUnitList(dataId);
    }


    @RespAdvice
    @GetMapping("/getFlowInfo/{dataId}")
    public List<FlowNodeDTO> getFlowInfo(@PathVariable("dataId") String dataId) {
        return viewDetailService.getFlowInfo(dataId);
    }

}
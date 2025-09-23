package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import com.inspur.dsp.direct.entity.dto.BatchNegotiationDto;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.service.NegotiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/negotiation")
@Slf4j
@RequiredArgsConstructor
public class NegotiationController {

    private final NegotiationService negotiationService;

    /**
     * 获取所有待协商的基准数据元
     */
    @PostMapping("/getNegotiationDataElementList")
    @RespAdvice
    public Page<NegotiationDataElementVO> getNegotiationDataElementList(@RequestBody NegotiationParmDTO dto) {
        return negotiationService.getNegotiationDataElementList(dto);
    }

    /**
     * 批量发起协商
     */
    @PostMapping("/submitBatchNegotiation")
    @RespAdvice
    public void submitBatchNegotiation(@RequestBody @Validated BatchNegotiationDto dto) {
        negotiationService.submitBatchNegotiation(dto);
    }

    /**
     * 单条发起协商
     */
    @RespAdvice
    @PostMapping("/submitSingleNegotiation")
    public void submitSingleNegotiation(@RequestBody @Validated SingleNegotiationDto dto) {
        negotiationService.submitSingleNegotiation(dto);
    }

    /**
     * 获取协商记录信息及其详细信息
     */
    @RespAdvice
    @GetMapping("/getNegotiationRecordInfo")
    public NegotiationRecord getNegotiationRecordInfo(@RequestParam("dataid") String dataid) {
        return negotiationService.getNegotiationRecordInfo(dataid);
    }

    /**
     * 确定单条协商记录的数源单位
     */
    @PostMapping("/submitSingleNegotiationResult")
    @RespAdvice
    public void submitSingleNegotiationResult(@RequestParam("dataid") String dataid, @RequestParam("orgCode") String orgCode) {
        negotiationService.submitSingleNegotiationResult(dataid, orgCode);
    }

    /**
     * 导入多条协商的数源单位
     */
    @PostMapping("/importNegotiationResult")
    @RespAdvice
    public ImportNegotiationReturnDTO importNegotiationResult(@RequestParam("file") MultipartFile file) {
        return negotiationService.importNegotiationResult(file);
    }

    /**
     * 导出协商列表
     */
    @PostMapping("/exportNegotiationList")
    @RespAdvice
    public void exportNegotiationList(@RequestBody NegotiationParmDTO dto,
                                    @RequestParam("exportFlag") String exportFlag,
                                    HttpServletResponse response) {
        negotiationService.exportNegotiationList(dto, exportFlag, response);
    }
}
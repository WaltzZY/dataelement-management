package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.BatchNegotiationDto;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationFailDetailDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationResultDto;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.entity.vo.NegotiationRecordInfoVo;
import com.inspur.dsp.direct.enums.TemplateTypeEnums;
import com.inspur.dsp.direct.service.NegotiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 007-009
 */

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
    @GetMapping("/getNegotiationRecordInfo/{dataid}")
    public NegotiationRecordInfoVo getNegotiationRecordInfo(@PathVariable("dataid") String dataid) {
        return negotiationService.getNegotiationRecordInfo(dataid);
    }

    /**
     * 确定单条协商记录的数源单位
     */
    @PostMapping("/submitSingleNegotiationResult")
    @RespAdvice
    public void submitSingleNegotiationResult(@RequestBody @Validated SingleNegotiationResultDto dto) {
        negotiationService.submitSingleNegotiationResult(dto);
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
                                    HttpServletResponse response) {
        negotiationService.exportNegotiationList(dto, response);
    }

    /**
     * 导出导入失败清单（录入协商结果）
     */
    @PostMapping("/exportNegotiationFailList")
    @RespAdvice
    public void exportNegotiationFailList(@RequestBody List<ImportNegotiationFailDetailDTO> failDetails,
                                         HttpServletResponse response) {
        negotiationService.exportNegotiationFailList(failDetails, response);
    }

    /**
     * 下载导入定源结果模板
     * @param response HttpServletResponse对象
     */
    @GetMapping("/downloadImportDatasourceResultTemplate")
    public void downloadImportDatasourceResultTemplate(HttpServletResponse response) {
        log.info("下载导入定源结果模板开始");
        negotiationService.downloadImportTemplate(TemplateTypeEnums.IMPORT_DATASOURCE_RESULT, response);
        log.info("下载导入定源结果模板结束");
    }
}
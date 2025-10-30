package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.BatchNegotiationDto;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationFailDetailDTO;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationResultDto;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.entity.vo.NegotiationRecordInfoVo;
import com.inspur.dsp.direct.enums.TemplateTypeEnums;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface NegotiationService {

    Page<NegotiationDataElementVO> getNegotiationDataElementList(NegotiationParmDTO negDTO);

    void submitBatchNegotiation(BatchNegotiationDto dto);

    void submitSingleNegotiation(SingleNegotiationDto dto);

    NegotiationRecordInfoVo getNegotiationRecordInfo(String dataid);

    void submitSingleNegotiationResult(SingleNegotiationResultDto dto);

    ImportNegotiationReturnDTO importNegotiationResult(MultipartFile file);

    void exportNegotiationList(NegotiationParmDTO dto, HttpServletResponse response);

    void exportNegotiationFailList(List<ImportNegotiationFailDetailDTO> failDetails, HttpServletResponse response);

    /**
     * 下载导入模板
     * 根据模板类型下载对应的Excel模板文件
     *
     * @param templateType 模板类型枚举
     * @param response HttpServletResponse对象
     */
    void downloadImportTemplate(TemplateTypeEnums templateType, HttpServletResponse response);
}
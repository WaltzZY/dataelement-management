package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.BatchNegotiationDto;
import com.inspur.dsp.direct.entity.dto.ImportNegotiationReturnDTO;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationDto;
import com.inspur.dsp.direct.entity.dto.SingleNegotiationResultDto;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import com.inspur.dsp.direct.entity.vo.NegotiationRecordInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface NegotiationService {

    Page<NegotiationDataElementVO> getNegotiationDataElementList(NegotiationParmDTO negDTO);

    void submitBatchNegotiation(BatchNegotiationDto dto);

    void submitSingleNegotiation(SingleNegotiationDto dto);

    NegotiationRecordInfoVo getNegotiationRecordInfo(String dataid);

    void submitSingleNegotiationResult(SingleNegotiationResultDto dto);

    ImportNegotiationReturnDTO importNegotiationResult(MultipartFile file);

    void exportNegotiationList(NegotiationParmDTO dto, HttpServletResponse response);
}
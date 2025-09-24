package com.inspur.dsp.direct.console.controller.organisersClaim;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.GetDataElementDTO;
import com.inspur.dsp.direct.entity.vo.ClaimDataElementVO;
import com.inspur.dsp.direct.service.OrganisersClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/initiate-claim")
@Slf4j
@RequiredArgsConstructor
public class OrganisersClaimController {


    private final OrganisersClaimService organisersClaimService;

    /**
     * 查询数据元列表
     * @param dto 查询条件（状态、开始时间、结束时间、关键字）
     * @return 数据元列表
     */
    @PostMapping("/getDataElement")
    @RespAdvice
    public Page<ClaimDataElementVO> getDataElement(@RequestBody GetDataElementDTO dto) {
        return organisersClaimService.getDataElement(dto);
    }

    /**
     * 发起认领
     * @param ids 数据元ID列表
     * @return 操作结果
     */
    @PostMapping("/organisers-initateclaim")
    @RespAdvice
    public void startBatchClaim(@RequestBody List<String> ids) {
        organisersClaimService.startBatchClaim(ids);
    }

    /**
     * 导出
     * @param dto (状态、开始时间、结束时间、关键字）
     * @return 无
     */
    @RespAdvice
    @PostMapping("/exportData")
    public void exportPaData(@RequestBody @Validated GetDataElementDTO dto, HttpServletResponse response) {
        organisersClaimService.exportData(dto, response);
    }
}
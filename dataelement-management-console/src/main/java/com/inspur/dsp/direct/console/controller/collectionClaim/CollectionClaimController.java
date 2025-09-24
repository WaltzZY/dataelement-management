package com.inspur.dsp.direct.console.controller.collectionClaim;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.ClaimOrRejectDTO;
import com.inspur.dsp.direct.entity.dto.GetDataPendingAndProcessedSourceDTO;
import com.inspur.dsp.direct.entity.vo.GetDataPendingAndProcessedSourceVO;
import com.inspur.dsp.direct.service.CollectionClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方认领控制器
 */
@RestController
@RequestMapping("/collection-claim")
@Slf4j
@RequiredArgsConstructor
public class CollectionClaimController {

    private final CollectionClaimService collectionClaimService;

    /**
     * 查询待处理和已处理数据元列表
     * @param dto 查询条件DTO
     * @return 数据元列表
     */
    @PostMapping("/GetDataPendingSource")
    @RespAdvice
    public Page<GetDataPendingAndProcessedSourceVO> getDataPendingAndProcessedSource(
            @RequestBody GetDataPendingAndProcessedSourceDTO dto) {
        return collectionClaimService.getDataPendingAndProcessedSource(dto);
    }

    /**
     * 认领或不认领数据元
     * @param dto 认领或拒绝DTO
     * @return 操作结果
     */
    @PostMapping("/claim-reject")
    @RespAdvice
    @Transactional

    public void claimOrReject(@RequestBody ClaimOrRejectDTO dto) {
        collectionClaimService.claimOrReject(dto);
    }

    /**
     * 导出
     * @param dto (状态、开始时间、结束时间、关键字）
     * @return 无
     */
    @RespAdvice
    @PostMapping("/exportData")
    public void exportPaData(@RequestBody @Validated GetDataPendingAndProcessedSourceDTO dto, HttpServletResponse response) {
        collectionClaimService.exportData(dto, response);
    }

}
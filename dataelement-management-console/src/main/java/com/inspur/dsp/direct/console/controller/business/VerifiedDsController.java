package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.ExportPaDataDto;
import com.inspur.dsp.direct.entity.dto.GetPendingApprovalPageDto;
import com.inspur.dsp.direct.entity.vo.GetPendingApprovalPageVo;
import com.inspur.dsp.direct.service.VerifiedDsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 负责核定数源单位相关操作接口
 */
@RestController
@RequestMapping("/verifiedDs")
@Slf4j
@RequiredArgsConstructor
public class VerifiedDsController {

    private final VerifiedDsService verifiedDsService;

    /**
     * 005-组织方-待核定 分页查询待核定数据
     * 006-组织方-已定源 分页查询已定源数据
     */
    @RespAdvice
    @PostMapping("/getPaPage")
    public Page<GetPendingApprovalPageVo> getPaPage(@RequestBody GetPendingApprovalPageDto dto) {
        return verifiedDsService.getPaPage(dto);
    }

    /**
     * 005-组织方-待核定-导出
     * 006-组织方-已定源-导出
     */
    @RespAdvice
    @PostMapping("/exportPaData")
    public void exportPaData(@RequestBody ExportPaDataDto dto, HttpServletResponse response) {
        verifiedDsService.exportPaData(dto, response);
    }

    /**
     * 批量核定
     */
    @RespAdvice
    @PostMapping("/batchVerification")
    public void batchVerification(@RequestBody List<String> dataids) {
        verifiedDsService.batchVerification(dataids);
    }
}
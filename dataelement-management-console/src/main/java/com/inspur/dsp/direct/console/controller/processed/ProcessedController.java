package com.inspur.dsp.direct.console.controller.processed;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.GetProcessedDataElementDTO;
import com.inspur.dsp.direct.entity.vo.GetProcessedDataElementVO;
import com.inspur.dsp.direct.service.ProcessedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
/**
 * 018
 */

/**
 * 处理组织方查看已处理相关HTTP请求
 * 包括数据元列表查询、查看详情等操作，不涉及具体业务逻辑实现
 */
@RestController
@RequestMapping("processed")
@Slf4j
@RequiredArgsConstructor
public class ProcessedController {


    private final ProcessedService processedService;

    /**
     * 查看已处理的数据元列表
     *
     * @param dto 查询条件（状态、开始时间、结束时间、关键字）
     * @return 已处理数据元列表
     */
    @PostMapping("/getProcessedDataElement")
    @RespAdvice
    public Page<GetProcessedDataElementVO> getProcessedDataElement(@RequestBody GetProcessedDataElementDTO dto) {
        return processedService.getProcessedDataElement(dto);
    }

    /**
     * 导出
     * @param dto (状态、开始时间、结束时间、关键字）
     * @return 无
     */
    @RespAdvice
    @PostMapping("/exportData")
    public void exportPaData(@RequestBody @Validated GetProcessedDataElementDTO dto, HttpServletResponse response) {
        processedService.exportData(dto, response);
    }
}
package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.AddNegotiationDto;
import com.inspur.dsp.direct.entity.dto.GetDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.ManualSourceDto;
import com.inspur.dsp.direct.entity.dto.VerifiedDataSourceDto;
import com.inspur.dsp.direct.entity.vo.DataElementInfoVo;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.SelectSourceUnitVo;
import com.inspur.dsp.direct.service.OrganisersService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组织方接口
 */
@RestController
@RequestMapping("/organisers")
@Slf4j
@RequiredArgsConstructor
public class OrganisersController {

    private final OrganisersService organisersService;

    /**
     * 001-组织方列表
     * @param dto
     * @return
     */
    @PostMapping("/getDataElementPage")
    @RespAdvice
    public Page<DataElementPageInfoVo> getDataElementPage(@RequestBody GetDataElementPageDto dto) {
        return organisersService.getDataElementPage(dto);
    }

    /**
     * 001-批量发起定源
     */
    @PostMapping("/batchInitiateSource")
    @RespAdvice
    public void batchInitiateSource(@RequestBody List<String> deIds) {
        organisersService.batchInitiateSource(deIds);
    }

    /**
     * 001-导出
     * 使用easyexcel将当前页面查询出来的数据进行全量导出
     * @param dto
     */
    @PostMapping("/exportDataElement")
    @RespAdvice
    public void exportDataElement(@RequestBody GetDataElementPageDto dto, HttpServletResponse response) {
        organisersService.exportDataElement(dto, response);
    }

    /**
     * 001-003-发起协商
     */
    @PostMapping("/initiateNegotiation")
    @RespAdvice
    public void initiateNegotiation(@RequestBody @Validated AddNegotiationDto dto) {
        organisersService.initiateNegotiation(dto);
    }

    /**
     * 001-004-手动定源-选择单位
     */
    @PostMapping("/selectSourceUnit")
    @RespAdvice
    private List<SelectSourceUnitVo> selectSourceUnit(@RequestParam("unitName") String unitName) {
        return organisersService.selectSourceUnit(unitName);
    }

    /**
     * 001-005-核定数源
     */
    @PostMapping("/verifiedDataSource")
    @RespAdvice
    public void verifiedDataSource(@RequestBody @Validated VerifiedDataSourceDto dto) {
        organisersService.verifiedDataSource(dto);
    }

    /**
     * 001-1-数据元详情
     */
    @GetMapping("/info/{dataid}")
    @RespAdvice
    public DataElementInfoVo info(@PathVariable("dataid") String dataid) {
        return organisersService.info(dataid);
    }

    /**
     * 001-004-手动定源
     */
    @PostMapping("/manualSource")
    @RespAdvice
    public void manualSource(@RequestBody @Validated ManualSourceDto dto) {
        organisersService.manualSource(dto);
    }
}

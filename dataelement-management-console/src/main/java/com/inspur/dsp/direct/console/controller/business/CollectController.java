package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.RefuseDto;
import com.inspur.dsp.direct.entity.vo.CollectDataInfoVo;
import com.inspur.dsp.direct.entity.vo.CollectUnitVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import com.inspur.dsp.direct.service.CollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * 014-015
 */

/**
 * 采集方
 */
@RestController
@RequestMapping("/collect")
@Slf4j
@RequiredArgsConstructor
public class CollectController {


    private final CollectService collectService;

    /**
     * 002-采集方-列表
     */
    @PostMapping("/getDataElementPage")
    @RespAdvice
    public Page<GetCollectDataVo> getDataElementPage(@RequestBody CollectDataElementPageDto dto) {
        return collectService.getCollectDataElementPage(dto);
    }

    /**
     * 导出数据
     */
    @PostMapping("/exportData")
    @RespAdvice
    public void exportData(@RequestBody CollectDataElementPageDto dto, HttpServletResponse response) {
        collectService.exportData(dto, response);
    }

    /**
     * 002-001、002-拒绝/通过成为数源单位 改成批量处理
     */
    @PostMapping("/refuse")
    @RespAdvice
    public void refuse(@RequestBody @Validated RefuseDto dto) {
        collectService.refuse(dto);
    }

    /**
     * 002-1-详情
     */
    @GetMapping("/info/{id}")
    @RespAdvice
    public CollectDataInfoVo info(@PathVariable String id) {
        return collectService.info(id);
    }

    /**
     * 根据基准数据元id查询采集单位列表
     */
    @GetMapping("/getCollectUnitList/{id}")
    @RespAdvice
    public List<CollectUnitVo> getCollectUnitList(@PathVariable String id) {
        return collectService.getCollectUnitList(id);
    }

}

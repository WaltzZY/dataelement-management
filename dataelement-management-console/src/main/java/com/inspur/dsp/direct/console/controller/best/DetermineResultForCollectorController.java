package com.inspur.dsp.direct.console.controller.best;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.BaseDataElementDTO;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.service.DetermineResultForCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方查看已定源结果控制器
 * 职责：处理前端页面通过REST API调用的已定源结果相关请求
 */
@RestController
@RequestMapping("/determineResult/collector")
public class DetermineResultForCollectorController {

    @Autowired
    private DetermineResultForCollectorService determineResultForCollectorService;

    /**
     * 分页查询已定源结果列表数据
     * 使用范围：019
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @return 基准数据元列表
     */
    @PostMapping("/list")
    public Page<BaseDataElement> getDetermineResultList(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO) {
        try {
            return determineResultForCollectorService.getDetermineResultList(baseDataElementSearchDTO);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("数据元不存在", e);
        }
    }

    /**
     * 导出已定源结果数据
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @param response                 HTTP响应对象
     */
    @PostMapping("/download")
    public void download(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {
        try {
//            determineResultForCollectorService.download(baseDataElementSearchDTO, response);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("数据元不存在", e);
        } catch (Exception e) {
            throw new RuntimeException("重复发起定源", e);
        }
    }
}
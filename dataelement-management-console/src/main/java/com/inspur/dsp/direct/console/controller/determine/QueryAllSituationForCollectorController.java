package com.inspur.dsp.direct.console.controller.determine;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.service.QueryAllSituationForCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方查询整体情况控制器
 * 处理组织方相关的HTTP请求，包括数据元列表查询、发起定源、协商、核定等操作
 * 不涉及具体业务逻辑实现
 *
 * @author system
 */
@RestController
@RequestMapping("/situation/collector")
public class QueryAllSituationForCollectorController {

    @Autowired
    private QueryAllSituationForCollectorService queryAllSituationForCollectorService;

    /**
     * 分页查询列表数据
     * 使用范围：020
     *
     * @param baseDataElementSearchDTO 查询条件
     * @return 基准数据元列表
     */
    @PostMapping("/getAllSituationList")
    @RespAdvice
    public Page<DataElementWithTaskVo> getAllSituationList(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO) {
        return queryAllSituationForCollectorService.getAllSituationList(baseDataElementSearchDTO);
    }

    /**
     * 下载数据
     *
     * @param baseDataElementSearchDTO 查询条件
     * @param response                 HTTP响应
     */
    @PostMapping("/download")
    public void download(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {
        queryAllSituationForCollectorService.download(baseDataElementSearchDTO, response);
    }
}
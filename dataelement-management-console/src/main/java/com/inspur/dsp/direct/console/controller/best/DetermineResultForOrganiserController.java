package com.inspur.dsp.direct.console.controller.best;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.GetDetermineResultListDTO;
import com.inspur.dsp.direct.entity.vo.GetDetermineResultVo;
import com.inspur.dsp.direct.service.DetermineResultForOrganiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 组织方查看已定源结果控制器
 * 处理组织方相关的HTTP请求，包括数据元列表查询、发起定源、协商、核定等操作
 *
 * @author system
 * @date 2025-01-25
 */
@RestController
@RequestMapping("/determineResult/organiser")
public class DetermineResultForOrganiserController {

    @Autowired
    private DetermineResultForOrganiserService determineResultForOrganiserService;

    /**
     * 分页查询已定源结果列表数据
     * 使用范围：012
     *
     * @param  dto 查询参数
     * @return 基准数据元列表
     */
    @PostMapping("/list")
    @RespAdvice
    public Page<GetDetermineResultVo> getDetermineResultList(@RequestBody GetDetermineResultListDTO dto) {
        return determineResultForOrganiserService.getDetermineResultList(dto);
    }

    /**
     * 导出已定源结果数据
     *
     * @param dto 查询参数
     */
    @PostMapping("/download")
    public void download(@RequestBody GetDetermineResultListDTO dto, HttpServletResponse response) {
        try {
           determineResultForOrganiserService.download(dto,response);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("数据元不存在", e);
        } catch (Exception e) {
            throw new RuntimeException("业务异常", e);
        }
    }
}
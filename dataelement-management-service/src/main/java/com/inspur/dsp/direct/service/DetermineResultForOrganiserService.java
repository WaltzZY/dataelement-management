package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetDetermineResultListDTO;
import com.inspur.dsp.direct.entity.vo.GetDetermineResultVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 组织方查看已定源结果服务接口
 *
 * @author system
 * @date 2025-01-25
 */
public interface DetermineResultForOrganiserService {

    /**
     * 分页查询已定源结果列表数据
     *
     * @param dto 查询参数
     * @return 基准数据元列表
     */
    Page<GetDetermineResultVo> getDetermineResultList(GetDetermineResultListDTO dto);

    /**
     * 导出已定源结果数据
     *
     * @param dto 查询参数
     * @param response
     */
    void download(GetDetermineResultListDTO dto, HttpServletResponse response);
}
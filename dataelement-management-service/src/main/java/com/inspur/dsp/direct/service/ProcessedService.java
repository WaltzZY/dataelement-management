package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetProcessedDataElementDTO;
import com.inspur.dsp.direct.entity.vo.GetProcessedDataElementVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 已处理数据元服务接口
 */
public interface ProcessedService {

    /**
     * 获取已处理的数据元列表
     * @param dto 查询条件
     * @return 已处理数据元列表
     */
    Page<GetProcessedDataElementVO> getProcessedDataElement(GetProcessedDataElementDTO dto);

    /**
     * 导出已处理的数据元列表
     * @param dto 查询条件
     * @param response HTTP响应
     */
    void exportData(GetProcessedDataElementDTO dto, HttpServletResponse response);
}

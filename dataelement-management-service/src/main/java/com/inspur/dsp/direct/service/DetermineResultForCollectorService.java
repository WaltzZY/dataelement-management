package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方查看已定源结果服务接口
 */
public interface DetermineResultForCollectorService {

    /**
     * 获取指定已定源结果列表数据
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @return 基准数据元列表
     */
    Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO);

    /**
     * 导出指定已定源结果数据
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @param response                 HTTP响应对象
     */
    void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response);





}
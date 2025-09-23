package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.ClaimOrRejectDTO;
import com.inspur.dsp.direct.entity.dto.GetDataPendingAndProcessedSourceDTO;
import com.inspur.dsp.direct.entity.vo.GetDataPendingAndProcessedSourceVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方认领服务接口
 */
public interface CollectionClaimService {

    /**
     * 获取待处理和已处理数据源
     * @param dto 查询条件
     * @return 数据元列表
     */
    Page<GetDataPendingAndProcessedSourceVO> getDataPendingAndProcessedSource(GetDataPendingAndProcessedSourceDTO dto);

    /**
     * 认领或拒绝
     * @param dto 认领或拒绝信息
     * @return 操作结果
     */
    void claimOrReject(ClaimOrRejectDTO dto);

    /**
     * 导出待处理或已处理的数据元列表
     * @param dto 查询条件
     * @param response HTTP响应
     */
    void exportData(GetDataPendingAndProcessedSourceDTO dto, HttpServletResponse response);

}
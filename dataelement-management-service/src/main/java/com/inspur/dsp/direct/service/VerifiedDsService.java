package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetPendingApprovalPageDto;
import com.inspur.dsp.direct.entity.vo.GetPendingApprovalPageVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 核定数源单位服务接口
 */
public interface VerifiedDsService {

    /**
     * 分页查询待核定基准数据元列表
     * @param dto 查询条件
     * @return 分页数据
     */
    Page<GetPendingApprovalPageVo> getPaPage(GetPendingApprovalPageDto dto);

    /**
     * 导出待核定基准数据元列表
     * @param dto 查询条件
     * @param response HTTP响应
     */
    void exportPaData(GetPendingApprovalPageDto dto, HttpServletResponse response);

    /**
     * 批量发起核定, 单条核定
     * @param dataids 数据ID列表
     */
    void batchVerification(List<String> dataids);
}
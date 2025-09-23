package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetDataElementDTO;
import com.inspur.dsp.direct.entity.vo.ClaimDataElementVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface OrganisersClaimService {

    /**
     * 查询数据元列表
     * @param dto 查询条件
     * @return 数据元列表
     */
    Page<ClaimDataElementVO> getDataElement(GetDataElementDTO dto);

    /**
     * 发起认领
     * @param ids 数据元ID列表
     * @return 操作结果
     */
    void startBatchClaim(List<String> ids);

    /**
     * 导出待定源或认领中的数据元列表
     * @param dto 查询条件
     * @param response HTTP响应
     */
    void exportData(GetDataElementDTO dto, HttpServletResponse response);
}
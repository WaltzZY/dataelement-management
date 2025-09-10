package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.AddNegotiationDto;
import com.inspur.dsp.direct.entity.dto.GetDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.ManualSourceDto;
import com.inspur.dsp.direct.entity.dto.VerifiedDataSourceDto;
import com.inspur.dsp.direct.entity.vo.DataElementInfoVo;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.SelectSourceUnitVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface OrganisersService {

    /**
     * 组织方获取数据元分页列表
     * @param dto
     * @return
     */
    Page<DataElementPageInfoVo> getDataElementPage(GetDataElementPageDto dto);

    /**
     * 批量发起定源
     * @param deIds
     */
    void batchInitiateSource(List<String> deIds);

    /**
     * 导出数据元
     * @param dto
     * @param response
     */
    void exportDataElement(GetDataElementPageDto dto, HttpServletResponse response);

    /**
     * 组织方发起定源协商
     * @param dto
     */
    void initiateNegotiation(AddNegotiationDto dto);

    /**
     * 获取数据源单位列表,模糊查询部门信息
     * @param unitName
     * @return
     */
    List<SelectSourceUnitVo> selectSourceUnit(String unitName);

    /**
     * 核定数源
     * @param dto
     */
    void verifiedDataSource(VerifiedDataSourceDto dto);

    /**
     * 获取数据元详情
     * @param dataid
     * @return
     */
    DataElementInfoVo info(String dataid);

    /**
     * 手动定源
     * @param dto
     */
    void manualSource(ManualSourceDto dto);
}

package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.RefuseDto;
import com.inspur.dsp.direct.entity.vo.CollectDataInfoVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;

import javax.servlet.http.HttpServletResponse;

public interface CollectService {

    /**
     * 获取数据元列表
     * @param dto
     * @return
     */
    Page<GetCollectDataVo> getCollectDataElementPage(CollectDataElementPageDto dto);

    /**
     * 拒绝/通过成为数源单位
     * @param dto
     */
    void refuse(RefuseDto dto);

    /**
     * 查询详情
     * @param id
     * @return
     */
    CollectDataInfoVo info(String id);

    /**
     * 导出数据
     * @param dto
     * @param response
     */
    void exportData(CollectDataElementPageDto dto, HttpServletResponse response);
}

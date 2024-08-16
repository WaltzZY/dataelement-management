package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.entity.dto.GetDetailedListDTO;
import com.inspur.dsp.direct.entity.vo.GetDetailedListVO;

/**
 * 基准数据元表
 */
public interface DataElementBaseService extends IService<DataElementBase> {

    /**
     * 查询清单列表
     * @param dto
     * @return
     */
    Page<GetDetailedListVO> getDetailedList(GetDetailedListDTO dto);
}

package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.entity.dto.GetDetailedCountDTO;
import com.inspur.dsp.direct.entity.dto.GetDetailedListDTO;
import com.inspur.dsp.direct.entity.vo.DetailedCountVO;
import com.inspur.dsp.direct.entity.vo.GetDetailedListVO;
import com.inspur.dsp.direct.entity.vo.RegionAndOrgan;

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

    /**
     * 查询清单列表汇总
     * @param dto
     * @return
     */
    DetailedCountVO getDetailedCount(GetDetailedCountDTO dto);

    /**
     * 获取一级行政区和国家部委
     * @return
     */
    RegionAndOrgan regionAndOrgan();
}

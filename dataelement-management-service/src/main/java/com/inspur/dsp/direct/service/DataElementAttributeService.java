package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementAttribute;
import com.inspur.dsp.direct.entity.vo.DataElementAttributeVO;

import java.util.List;

/**
 * 基准数据元属性集表
 */
public interface DataElementAttributeService extends IService<DataElementAttribute> {

    /**
     * 查询基准数据元关联属性
     * @param dataElementId 数据元id
     * @return
     */
    List<DataElementAttributeVO> getDataElementAttributeByDataElementId(String dataElementId);
}

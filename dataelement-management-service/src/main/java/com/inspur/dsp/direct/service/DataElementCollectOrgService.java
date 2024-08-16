package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementCollectorg;
import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;

import java.util.List;

/**
 * 数据元所指数据采集单位表
 */
public interface DataElementCollectOrgService extends IService<DataElementCollectorg> {

    List<DataElementCollectOrgVO> getList(String dataElementId);
}

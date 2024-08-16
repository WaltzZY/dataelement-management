package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementDataItem;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;

import java.util.List;

/**
 * 数据元和数据资源数据项关联表
 */
public interface DataElementDataItemService extends IService<DataElementDataItem> {

    List<DataElementDataItemVO> getList(String dataElementName);
}

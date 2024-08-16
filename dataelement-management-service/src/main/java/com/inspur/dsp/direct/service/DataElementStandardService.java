package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementStandard;
import com.inspur.dsp.direct.entity.vo.DataElementStandardVO;

import java.util.List;

/**
 * 数据元相关标准表
 */
public interface DataElementStandardService extends IService<DataElementStandard> {

    List<DataElementStandardVO> getList(String dataElementId);
}

package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementSample;
import com.inspur.dsp.direct.entity.vo.DataElementSampleVO;

import java.util.List;

/**
 * 数据元样例表
 */
public interface DataElementSampleService extends IService<DataElementSample> {

    List<DataElementSampleVO> getList(String dataElementId);
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementSampleDao;
import com.inspur.dsp.direct.dbentity.business.DataElementSample;
import com.inspur.dsp.direct.service.DataElementSampleService;
import org.springframework.stereotype.Service;
/**
 * 数据元样例表
 */
@Service
public class DataElementSampleServiceImpl  extends ServiceImpl<DataElementSampleDao, DataElementSample> implements DataElementSampleService {
}

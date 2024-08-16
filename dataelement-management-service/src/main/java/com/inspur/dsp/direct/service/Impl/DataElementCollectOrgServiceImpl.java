package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementCollectorgDao;
import com.inspur.dsp.direct.dbentity.business.DataElementCollectorg;
import com.inspur.dsp.direct.service.DataElementCollectOrgService;
import org.springframework.stereotype.Service;

/**
 * 数据元所指数据采集单位表
 */
@Service
public class DataElementCollectOrgServiceImpl extends ServiceImpl<DataElementCollectorgDao, DataElementCollectorg> implements DataElementCollectOrgService {
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementAttributeDao;
import com.inspur.dsp.direct.dbentity.business.DataElementAttribute;
import com.inspur.dsp.direct.service.DataElementAttributeService;
import org.springframework.stereotype.Service;

/**
 * 基准数据元属性集表
 */
@Service
public class DataElementAttributeServiceImpl extends ServiceImpl<DataElementAttributeDao, DataElementAttribute> implements DataElementAttributeService {
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementBaseDao;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.service.DataElementBaseService;
import org.springframework.stereotype.Service;

/**
 * 基准数据元表
 */
@Service
public class DataElementBaseServiceImpl  extends ServiceImpl<DataElementBaseDao, DataElementBase> implements DataElementBaseService {
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementSceneDao;
import com.inspur.dsp.direct.dbentity.business.DataElementScene;
import com.inspur.dsp.direct.service.DataElementSceneService;
import org.springframework.stereotype.Service;
/**
 * 数据元关联场景表
 */
@Service
public class DataElementSceneServiceImpl extends ServiceImpl<DataElementSceneDao, DataElementScene> implements DataElementSceneService {
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementCollectorgDao;
import com.inspur.dsp.direct.dbentity.business.DataElementCollectorg;
import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;
import com.inspur.dsp.direct.service.DataElementCollectOrgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据元所指数据采集单位表
 */
@Service
public class DataElementCollectOrgServiceImpl extends ServiceImpl<DataElementCollectorgDao, DataElementCollectorg> implements DataElementCollectOrgService {

    @Resource
    private DataElementCollectorgDao dataElementCollectorgDao;

    @Override
    public List<DataElementCollectOrgVO> getList(String dataElementId) {
        List<DataElementCollectOrgVO> dataList = dataElementCollectorgDao.getList(dataElementId);
        return dataList;
    }
}

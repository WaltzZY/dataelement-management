package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementStandardDao;
import com.inspur.dsp.direct.dbentity.business.DataElementStandard;
import com.inspur.dsp.direct.entity.vo.DataElementStandardVO;
import com.inspur.dsp.direct.service.DataElementStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据元相关标准表
 */
@Service
public class DataElementStandardServiceImpl extends ServiceImpl<DataElementStandardDao, DataElementStandard> implements DataElementStandardService {

    @Resource
    private DataElementStandardDao dataElementStandardDao;

    @Override
    public List<DataElementStandardVO> getList(String dataElementId) {
        List<DataElementStandardVO> dataList = dataElementStandardDao.getList(dataElementId);
        return dataList;
    }
}

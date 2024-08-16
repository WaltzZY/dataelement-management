package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementDataItemDao;
import com.inspur.dsp.direct.dbentity.business.DataElementDataItem;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;
import com.inspur.dsp.direct.service.DataElementDataItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据元和数据资源数据项关联表
 */
@Service
public class DataElementDataItemServiceImpl  extends ServiceImpl<DataElementDataItemDao, DataElementDataItem> implements DataElementDataItemService {

    @Resource
    private DataElementDataItemDao dataElementDataItemDao;

    @Override
    public List<DataElementDataItemVO> getList(String dataElementName) {
        List<DataElementDataItemVO> dataList = dataElementDataItemDao.getList(dataElementName);
        return dataList;
    }
}

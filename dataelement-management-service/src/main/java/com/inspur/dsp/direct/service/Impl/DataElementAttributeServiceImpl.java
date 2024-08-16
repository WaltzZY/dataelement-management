package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementAttributeDao;
import com.inspur.dsp.direct.dbentity.business.DataElementAttribute;
import com.inspur.dsp.direct.entity.vo.DataElementAttributeVO;
import com.inspur.dsp.direct.service.DataElementAttributeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基准数据元属性集表
 */
@Service
public class DataElementAttributeServiceImpl extends ServiceImpl<DataElementAttributeDao, DataElementAttribute> implements DataElementAttributeService {
    @Resource
    private DataElementAttributeDao dataElementAttributeDao;

    /**
     * 查询基准数据元关联属性
     * @param dataElementId
     * @return
     */
    @Override
    public List<DataElementAttributeVO> getDataElementAttributeByDataElementId(String dataElementId) {
        return dataElementAttributeDao.getDataElementAttributeByDataElementId(dataElementId);
    }

}

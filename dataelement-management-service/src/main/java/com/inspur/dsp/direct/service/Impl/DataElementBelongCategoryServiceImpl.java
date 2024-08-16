package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementBelongCategoryDao;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基准数据元所属类别表
 */
@Service
public class DataElementBelongCategoryServiceImpl extends ServiceImpl<DataElementBelongCategoryDao, DataElementBelongCategory> implements DataElementBelongCategoryService {
    @Resource
    private DataElementBelongCategoryDao dataElementBelongCategoryDao;
    /**
     * 查询基准数据元分类
     *
     * @param dataElementId 基准数据元id
     * @return
     */
    @Override
    public List<ClassIfiCationMethodVO> getCategoryByDataElementId(String dataElementId) {
        return dataElementBelongCategoryDao.getCategoryByDataElementId(dataElementId);
    }
    /**
     * 查询分类
     * @return
     */
    @Override
    public List<ClassIfiCationMethodVO> getCategorylist() {
        return dataElementBelongCategoryDao.getCategorylist();
    }
}

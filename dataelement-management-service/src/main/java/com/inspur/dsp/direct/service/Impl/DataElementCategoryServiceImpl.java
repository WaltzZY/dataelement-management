package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementCategoryDao;
import com.inspur.dsp.direct.dbentity.business.DataElementCategory;
import com.inspur.dsp.direct.service.DataElementCategoryService;
import org.springframework.stereotype.Service;

/**
 * 基准数据元分类代码表
 */
@Service
public class DataElementCategoryServiceImpl extends ServiceImpl<DataElementCategoryDao, DataElementCategory> implements DataElementCategoryService {
}

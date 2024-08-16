package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementBelongCategoryDao;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import org.springframework.stereotype.Service;

/**
 * 基准数据元所属类别表
 */
@Service
public class DataElementBelongCategoryServiceImpl extends ServiceImpl<DataElementBelongCategoryDao, DataElementBelongCategory> implements DataElementBelongCategoryService {
}

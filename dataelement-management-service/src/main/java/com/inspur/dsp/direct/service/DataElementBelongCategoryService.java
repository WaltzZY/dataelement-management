package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;

import java.util.List;

/**
 * 基准数据元所属类别表
 */
public interface DataElementBelongCategoryService extends IService<DataElementBelongCategory> {
    /**
     * 查询基准数据元分类
     *
     * @param dataElementId 基准数据元id
     * @return
     */
    List<ClassIfiCationMethodVO> getCategoryByDataElementId(String dataElementId);
    /**
     * 查询分类
     * @return
     */
    List<ClassIfiCationMethodVO> getCategorylist();
}

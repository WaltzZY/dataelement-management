package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;

import java.util.List;

import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementBelongCategoryDao extends BaseMapper<DataElementBelongCategory> {
    int updateBatch(List<DataElementBelongCategory> list);

    int updateBatchSelective(List<DataElementBelongCategory> list);

    int batchInsert(@Param("list") List<DataElementBelongCategory> list);

    int deleteByPrimaryKeyIn(List<String> list);

    /**
     * 查询基准数据元分类
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
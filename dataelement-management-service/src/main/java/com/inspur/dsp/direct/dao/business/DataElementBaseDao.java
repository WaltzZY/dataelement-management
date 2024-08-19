package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import java.util.List;

import com.inspur.dsp.direct.entity.dto.GetDetailedCountDTO;
import com.inspur.dsp.direct.entity.dto.GetDetailedListDTO;
import com.inspur.dsp.direct.entity.vo.GetDetailedListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataElementBaseDao extends BaseMapper<DataElementBase> {
    int updateBatch(List<DataElementBase> list);

    int updateBatchSelective(List<DataElementBase> list);

    int batchInsert(@Param("list") List<DataElementBase> list);

    int deleteByPrimaryKeyIn(List<String> list);

    Page<GetDetailedListVO> getDetailedList(Page<?> page, @Param("dto") GetDetailedListDTO dto);

    List<DataElementBase> getDetailedCount(@Param("dto") GetDetailedCountDTO dto);

    Integer getCollectOrgCount(@Param("dto") GetDetailedCountDTO dto);
}
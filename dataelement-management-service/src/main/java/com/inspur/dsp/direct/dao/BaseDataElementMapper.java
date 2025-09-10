package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.GetDataElementPageDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseDataElementMapper extends BaseMapper<BaseDataElement> {
    /**
     * 组织方获取数据元分页列表
     *
     * @param page
     * @param dto
     * @return
     */
    List<DataElementPageInfoVo> getDataElementPage(Page page, @Param("dto") GetDataElementPageDto dto, @Param("sortSql") String sortSql);

    List<DataElementPageInfoVo> getDataElementPage(@Param("dto") GetDataElementPageDto dto, @Param("sortSql") String sortSql);

    /**
     * 获取确认方数据元详情
     *
     * @return
     */
    List<GetCollectDataVo> getCollectData(Page page, @Param("dto") CollectDataElementPageDto dto, @Param("sortSql") String sortSql, @Param("organCode") String organCode);
}
package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.CollectDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.GetDataElementPageDto;
import com.inspur.dsp.direct.entity.dto.GetPendingApprovalPageDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.entity.vo.GetCollectDataVo;
import com.inspur.dsp.direct.entity.vo.GetPendingApprovalPageVo;
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

    /**
     * 分页查询待核定基准数据元列表
     *
     * @param page 分页参数
     * @param dto  查询条件
     * @return 待核定数据列表
     */
    List<GetPendingApprovalPageVo> selectPaPage(@Param("page") Page<?> page, @Param("dto") GetPendingApprovalPageDto dto, @Param("sortSql") String sortSql);

    /**
     * 分页查询已定源基准数据元列表
     *
     * @param page 分页参数
     * @param dto  查询条件
     * @return 已定源数据列表
     */
    List<GetPendingApprovalPageVo> selectConfirmedPage(@Param("page") Page<?> page, @Param("dto") GetPendingApprovalPageDto dto, @Param("sortSql") String sortSql);


    /**
     * 分页查询已定源结果列表
     *
     * @param baseDataElementSearchDTO 查询参数
     * @return 基准数据元列表
     */
    List<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO);

    /**
     * 查询整体情况列表（用于采集方）
     *
     * @param baseDataElementSearchDTO 查询参数
     * @return 基准数据元列表
     */
    List<BaseDataElement> getAllSituationList(BaseDataElementSearchDTO baseDataElementSearchDTO, @Param("orgCode") String orgCode);


    List<DataElementWithTaskVo> getDetermineResultListWithOrganiser(Page page, @Param("base") BaseDataElementSearchDTO baseDataElementSearchDTO);


}
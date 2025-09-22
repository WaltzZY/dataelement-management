package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;

/**
 * 采集方查询整体情况服务接口
 *
 * @author system
 */
public interface QueryAllSituationForCollectorService {

    /**
     * 分页查询列表数据
     *
     * @param baseDataElementSearchDTO 查询条件
     * @return 基准数据元列表
     */
    Page<DataElementWithTaskVo> getAllSituationList(BaseDataElementSearchDTO baseDataElementSearchDTO);
//
//    /**
//     * 查询基准数据详情数据
//     *
//     * @param baseDataElementSearchDTO 查询条件
//     * @return 基准数据元详情
//     */
//    BaseDataElementDTO getAllSituationDetail(BaseDataElementSearchDTO baseDataElementSearchDTO);
//
//    /**
//     * 查询基准数据详情数据（带记录详情）
//     *
//     * @param baseDataElementSearchDTO 查询条件
//     * @return 基准数据元详情（包含记录详情）
//     */
//    BaseDataElementDTO getAllSituationWithRecordDetail(BaseDataElementSearchDTO baseDataElementSearchDTO);
//

    /**
     * 下载数据
     *
     * @param baseDataElementSearchDTO 查询条件
     * @return 基准数据元列表
     */
    void download(BaseDataElementSearchDTO baseDataElementSearchDTO);
}
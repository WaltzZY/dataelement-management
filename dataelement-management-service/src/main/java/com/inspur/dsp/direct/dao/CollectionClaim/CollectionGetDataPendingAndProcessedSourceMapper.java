package com.inspur.dsp.direct.dao.CollectionClaim;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetDataPendingAndProcessedSourceDTO;
import com.inspur.dsp.direct.entity.vo.GetDataPendingAndProcessedSourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取待处理和已处理数据源Mapper
 */
@Mapper
public interface CollectionGetDataPendingAndProcessedSourceMapper {

//    /**
//     * 查询待处理数据
//     * @param dto 查询条件
//     * @return 待处理数据列表
//     */
//    List<GetDataPendingAndProcessedSourceVO> selectPendingData(@Param("dto") GetDataPendingAndProcessedSourceDTO dto);
//
//    /**
//     * 查询已处理数据
//     * @param dto 查询条件
//     * @return 已处理数据列表
//     */
//    List<GetDataPendingAndProcessedSourceVO> selectProcessedData(@Param("dto") GetDataPendingAndProcessedSourceDTO dto);

    /**
     * 查询待处理或已处理数据
     * @param dto 查询条件
     * @return 数据列表
     */
     List<GetDataPendingAndProcessedSourceVO> getDataPendingAndProcessedData(@Param("page") Page<GetDataPendingAndProcessedSourceVO> page, @Param("dto") GetDataPendingAndProcessedSourceDTO dto, @Param("orgCode") String orgCode);

}

package com.inspur.dsp.direct.dao.Processed;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetProcessedDataElementDTO;
import com.inspur.dsp.direct.entity.vo.GetProcessedDataElementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取待处理和已处理数据源Mapper
 */
@Mapper
public interface ProcessedGetDataPendingAndProcessedSourceMapper {

    /**
     * 获取已处理的数据元列表
     * @param dto 查询条件
     * @return 已处理数据元列表
     */
    Page<GetProcessedDataElementVO> getProcessedDataElement(Page<GetProcessedDataElementVO> page, @Param("dto") GetProcessedDataElementDTO dto, @Param("orgCode") String orgCode);

    List<GetProcessedDataElementVO> getProcessedDataElement(@Param("dto") GetProcessedDataElementDTO dto, @Param("orgCode") String orgCode);


}

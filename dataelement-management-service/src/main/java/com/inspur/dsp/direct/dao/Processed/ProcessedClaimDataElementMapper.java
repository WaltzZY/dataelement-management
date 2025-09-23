package com.inspur.dsp.direct.dao.Processed;

import com.inspur.dsp.direct.entity.vo.ClaimDataElementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 认领数据元Mapper
 */
@Mapper
public interface ProcessedClaimDataElementMapper {

    /**
     * 根据状态查询base_data_element表
     * @param status 状态
     * @return 符合条件的数据元列表
     */
    List<ClaimDataElementVO> selectBaseDataElementByStatus(@Param("status") String status);
}
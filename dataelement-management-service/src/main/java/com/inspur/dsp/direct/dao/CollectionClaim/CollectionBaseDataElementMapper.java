package com.inspur.dsp.direct.dao.CollectionClaim;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 基准数据元Mapper
 */
@Mapper
public interface CollectionBaseDataElementMapper {

    /**
     * 根据ID更新状态
     * @param dataId 数据ID
     * @param status 状态
     * @return 更新行数
     */
    int updateStatusById(@Param("dataId") String dataId,@Param("status") String status);
}

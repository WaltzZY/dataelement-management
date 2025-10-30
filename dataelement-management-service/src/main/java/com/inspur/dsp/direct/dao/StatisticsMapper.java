// StatisticsMapper.java
package com.inspur.dsp.direct.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatisticsMapper {
    
    Long getCommonStatusNumForOrganiser(@Param("status") String status);
    
    Long getPendingSourceConfirmQty(@Param("status") String status);
    
    Long getPendingSourceClaimQty(@Param("status") String status);
    
    Long getCommonStatusNumForCollector(@Param("orgCode") String orgCode, @Param("status") String status);
    
    Long getToBeProcessedQty(@Param("orgCode") String orgCode);
    
    Long getProcessedQty(@Param("orgCode") String orgCode);
    
    Long getCommonStatusNumForCollectorWithTask(@Param("orgCode") String orgCode, @Param("status") String status);

    Long getMyDesignatedSourceQty(@Param("orgCode") String orgCode);


}
package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceModifyLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceInterfaceModifyLogDao extends BaseMapper<ServiceInterfaceModifyLog> {
    int updateBatch(List<ServiceInterfaceModifyLog> list);

    int updateBatchSelective(List<ServiceInterfaceModifyLog> list);

    int batchInsert(@Param("list") List<ServiceInterfaceModifyLog> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
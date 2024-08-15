package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceBaseInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceInterfaceBaseInfoDao extends BaseMapper<ServiceInterfaceBaseInfo> {
    int updateBatch(List<ServiceInterfaceBaseInfo> list);

    int updateBatchSelective(List<ServiceInterfaceBaseInfo> list);

    int batchInsert(@Param("list") List<ServiceInterfaceBaseInfo> list);

    int deleteByPrimaryKeyIn(List<String> list);
}
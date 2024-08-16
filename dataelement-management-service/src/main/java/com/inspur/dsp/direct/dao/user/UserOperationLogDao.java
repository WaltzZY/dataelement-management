package com.inspur.dsp.direct.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserOperationLogDao extends BaseMapper<UserOperationLog> {
    int updateBatch(List<UserOperationLog> list);

    int updateBatchSelective(List<UserOperationLog> list);

    int batchInsert(@Param("list") List<UserOperationLog> list);

    int deleteByPrimaryKeyIn(List<String> list);

    IPage<UserOperationLog> getList(IPage<UserOperationLog> page);
}
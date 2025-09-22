package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ConfirmationTaskMapper extends BaseMapper<ConfirmationTask> {
    default ConfirmationTask selectFirstByBaseDataelementDataid(String baseDataelementDataid) {
        LambdaQueryWrapper<ConfirmationTask> myQuery = Wrappers.lambdaQuery(ConfirmationTask.class);
        myQuery.eq(ConfirmationTask::getBaseDataelementDataid, baseDataelementDataid);
        return selectOne(myQuery);
    }

    /**
     * 查询任务,条件为任务类型和数据元id和处理单位code和状态
     *
     * @param tasktype              任务类型
     * @param baseDataelementDataid 数据元id
     * @param processingUnitCode    处理单位code
     * @param status                状态
     * @return
     */
    ConfirmationTask selectFirstByTasktypeAndBaseDataelementDataidAndStatusAndProcessingUnitCode(@Param("tasktype") String tasktype, @Param("baseDataelementDataid") String baseDataelementDataid, @Param("status") String status, @Param("processingUnitCode") String processingUnitCode);

    List<ConfirmationTask> selectAllByStatusAndBaseDataelementDataidIn(@Param("status") String status, @Param("baseDataelementDataidCollection") Collection<String> baseDataelementDataidCollection);

    /**
     * 查询任务列表,条件为任务类型和数据元id
     *
     * @param tasktype              任务类型
     * @param baseDataelementDataid 数据元id
     * @return
     */
    List<ConfirmationTask> selectAllByTasktypeAndBaseDataelementDataid(@Param("tasktype") String tasktype, @Param("baseDataelementDataid") String baseDataelementDataid);

}
package com.inspur.dsp.direct.dao.CollectionClaim;

import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 确认任务Mapper
 */
@Mapper
public interface CollectionConfirmationTaskMapper {

    /**
     * 根据ID查询确认任务
     * @param taskId 任务ID
     * @return 确认任务
     */
    ConfirmationTask selectById(@Param("taskId") String taskId);

    /**
     * 根据数据元ID查询确认任务列表
     * @param dataElementDataId 数据元ID
     * @return 确认任务列表
     */
    List<ConfirmationTask> selectConfirmationTaskByDataElementDataId(@Param("dataElementDataId") String dataElementDataId);

    /**
     * 根据ID更新确认任务
     * @param task 确认任务对象
     * @return 更新行数
     */
    int updateConfirmationTaskStatus(ConfirmationTask task);
}

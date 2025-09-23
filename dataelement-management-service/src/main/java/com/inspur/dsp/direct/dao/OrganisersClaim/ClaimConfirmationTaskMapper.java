package com.inspur.dsp.direct.dao.OrganisersClaim;

import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClaimConfirmationTaskMapper {

    /**
     * 插入认领任务
     * @param confirmationTask 认领任务对象
     * @return 影响行数
     */
    int insertById(ConfirmationTask confirmationTask);
}
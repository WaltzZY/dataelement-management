package com.inspur.dsp.direct.service;

public interface BaseDataElementService {


    /**
     * 认领任务处理完成后,更新基准数据元状态信息
     */
    void updateBaseDataElementStatusByClaimTask(String dataid);
}

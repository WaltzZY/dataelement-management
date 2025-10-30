package com.inspur.dsp.direct.service;

public interface BaseDataElementService {


    /**
     * 认领任务处理完成后,更新基准数据元状态信息
     */
    void updateBaseDataElementStatusByClaimTask(String dataid);

    /**
     * 数据元已定源后,查询数源单位的联系人信息,记录在data_element_contact表中
     */
    void insertDataElementContact(String dataid);
}

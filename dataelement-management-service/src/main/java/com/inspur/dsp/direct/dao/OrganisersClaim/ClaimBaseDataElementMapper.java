package com.inspur.dsp.direct.dao.OrganisersClaim;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface ClaimBaseDataElementMapper {

    /**
     * 更新发送日期
     * @param dataid 数据元ID
     * @param sendDate 发送日期
     * @return 影响行数
     */
    int updateSendDate(@Param("dataid") String dataid, @Param("sendDate") LocalDateTime sendDate);
}

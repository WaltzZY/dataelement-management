package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.util.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 获取已定源列表参数
 */
@Data
public class GetDetermineResultListDTO {
    /**
     * 分页参数
     */
    private Integer pageSize = 10;
    /**
     * 当前页
     */
    private Integer pageNum = 1;
    /**
     * 基准数据元名称,定义模糊参数
     */
    private String keyword;
    /**
     * 数源单位统一社会信用代码
     */
    private List<String> sourceUnitCodeList;
    /**
     * 发起时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateBegin;
    /**
     * 发起时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateEnd;
    /**
     * 定源时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateBegin;
    /**
     * 定源时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateEnd;

    public void setSendDateBegin(Date sendDateBegin) {
        this.sendDateBegin = DateUtils.getStartOfDay(sendDateBegin);
    }

    public void setSendDateEnd(Date sendDateEnd) {
        this.sendDateEnd = DateUtils.getEndOfDay(sendDateEnd);
    }

    public void setConfirmDateBegin(Date confirmDateBegin) {
        this.confirmDateBegin = DateUtils.getStartOfDay(confirmDateBegin);
    }

    public void setConfirmDateEnd(Date confirmDateEnd) {
        this.confirmDateEnd = DateUtils.getEndOfDay(confirmDateEnd);
    }
}
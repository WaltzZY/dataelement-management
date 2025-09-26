package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.util.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetDataElementPageDto {

    /**
     * 采集单位code集合
     */
    private List<String> collectUnitCodeList;
    /**
     * 发起时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateEnd;
    /**
     * 发起时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateBegin;
    /**
     * 页码
     */
    private long pageNum = 1;
    /**
     * 每页条数
     */
    private long pageSize = 10;
    /**
     * 模糊搜索：基准数据元名称、定义、数源单位、采集单位
     */
    private String keyword;
    /**
     * 定源时间结束
     */

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateEnd;
    /**
     * 定源时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmDateBegin;
    /**
     * 数据元状态集合
     */
    private List<String> statusList;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序方式
     */
    private String sortOrder;

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

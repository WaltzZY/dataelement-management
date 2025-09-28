package com.inspur.dsp.direct.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.util.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CollectDataElementPageDto {
    /**
     * 审核状态，用于切换“待处理”，“已处理”
     */
    private String auditStatus;
    /**
     * 处理时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date processDateEnd;
    /**
     * 处理时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date processDateBegin;
    /**
     * 页码
     */
    private long pageNum;
    /**
     * 每页条数
     */
    private long pageSize;
    /**
     * 接收时间结束
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateEnd;
    /**
     * 接收时间开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDateBegin;
    /**
     * 基准数据元名称、定义
     */
    private String keyword;
    /**
     * 状态集合，状态
     */
    private List<String> statusList;
    /**
     * 排序字段：数据类型：datatype，状态：status，接收时间：receiveTime
     */
    private String sortField;
    /**
     * 排序方式：AES，DESC
     */
    private String sortOrder;

    public void setSendDateBegin(Date sendDateBegin) {
        this.sendDateBegin = DateUtils.getStartOfDay(sendDateBegin);
    }

    public void setSendDateEnd(Date sendDateEnd) {
        this.sendDateEnd = DateUtils.getEndOfDay(sendDateEnd);
    }

    public void setProcessDateBegin(Date processDateBegin) {
        this.processDateBegin = DateUtils.getStartOfDay(processDateBegin);
    }

    public void setProcessDateEnd(Date processDateEnd) {
        this.processDateEnd = DateUtils.getEndOfDay(processDateEnd);
    }
}

package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class RefuseDto {
    /**
     * 数据行唯一标识
     */
    private List<String> dataid;
    /**
     * 处理状态  pending_approval 确认, rejected 拒绝
     */
    private String handleStatus;
    /**
     * 反馈意见
     */
    private String opinion;
}

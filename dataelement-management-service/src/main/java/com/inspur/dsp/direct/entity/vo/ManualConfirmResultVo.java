package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 手动定源结果VO
 * 封装手动定源操作的响应数据
 * 
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManualConfirmResultVo {

    /**
     * 操作是否成功
     */
    private Boolean success;

    /**
     * 操作结果消息
     */
    private String message;

    /**
     * 数据行唯一标识
     */
    private String dataid;

    /**
     * 数源单位统一社会信用代码
     */
    private String sourceUnitCode;

    /**
     * 确认时间
     */
    private Date confirmTime;
}
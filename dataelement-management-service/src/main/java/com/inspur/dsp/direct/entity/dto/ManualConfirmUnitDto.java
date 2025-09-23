package com.inspur.dsp.direct.entity.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 手动定源请求DTO
 * 封装手动定源的请求参数
 * 
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManualConfirmUnitDto {

    /**
     * 数据元唯一标识
     */
    @NotBlank(message = "数据元唯一标识不能为空")
    private String dataid;

    /**
     * 数源单位ID（前端选择传入）
     */
    @NotBlank(message = "数源单位ID不能为空")
    private String sourceUnitId;
}
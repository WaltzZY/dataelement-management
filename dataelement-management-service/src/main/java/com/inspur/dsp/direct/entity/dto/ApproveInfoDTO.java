package com.inspur.dsp.direct.entity.dto;

import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 审核环节DTO
 * 
 * @author system
 * @since 2025
 */
@Data
public class ApproveInfoDTO {
    
    /**
     * 审核信息列表
     */
    @NotEmpty(message = "审核信息列表不能为空")
    @Valid
    private List<ApproveInfoOB> list;
}
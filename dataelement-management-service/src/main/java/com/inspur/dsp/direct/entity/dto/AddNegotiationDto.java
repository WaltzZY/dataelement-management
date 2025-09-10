package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AddNegotiationDto {
    /**
     * 元数据id，ID 编号
     */
    @NotBlank(message = "元数据id不能为空")
    private String dataid;
    /**
     * 协商事宜
     */
    @NotBlank(message = "协商事宜不能为空")
    private String negotiateDesc;
    /**
     * 协商对象id集合
     */
    private List<NegotiationObj> negotiationUnitCodes;
}

package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

@Data
public class GetDeptSearchDto {

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 组织名称
     */
    private String organName;
}

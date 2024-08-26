package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

@Data
public class GovDeptDTO {
    /**
     * 部门名称模糊
     */
    private String name;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;
}

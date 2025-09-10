package com.inspur.dsp.direct.httpentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织名称模糊查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganByNameLikeDto {

    // 当前页
    private Integer pageNum;
    // 每页大小
    private Integer pageSize;
    // 组织名称
    private String organName;

}

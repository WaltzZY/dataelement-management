package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDeptSearchVo {

    /**
     * 总数
     */
    private Long total;
    /**
     * 数据
     */
    private List<DeptSearchVo> rows;
}

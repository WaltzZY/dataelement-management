package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedCountVO {
    /**
     * 数据元个数
     */
    private long dataelementCount;
    /**
     * 关联单位数量
     */
    private long deptCount;
    /**
     * 待定源数量
     */
    private long status01;
    /**
     * 待定标数量
     */
    private long status02;
    /**
     * 已发布数量
     */
    private long status03;
    /**
     * 已废弃数量
     */
    private long status04;
}

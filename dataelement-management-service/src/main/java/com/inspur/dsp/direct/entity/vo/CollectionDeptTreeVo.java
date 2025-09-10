package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

@Data
public class CollectionDeptTreeVo {

    /**
     * 区划或部门code
     */
    private String code;
    /**
     * 区划或部门id，ID 编号
     */
    private String id;
    /**
     * 区划或部门的name
     */
    private String name;
    /**
     * region:区划，organ：部门
     */
    private String type;
}

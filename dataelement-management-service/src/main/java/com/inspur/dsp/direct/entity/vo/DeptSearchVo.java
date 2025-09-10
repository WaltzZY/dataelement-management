package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

@Data
public class DeptSearchVo {

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
     * 名称的全路径，举例：浙江省杭州市高新区
     */
    private String namePath;
    /**
     * region:区划，organ：部门
     */
    private String type;
}

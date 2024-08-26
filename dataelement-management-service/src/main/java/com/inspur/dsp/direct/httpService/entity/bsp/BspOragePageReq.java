package com.inspur.dsp.direct.httpService.entity.bsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * bsp部门分页请求实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BspOragePageReq {

    /**
     * 区域编码,默认值为000000000000,中华人民共和国
     */
    private String regionCode = "000000000000";
    /**
     * 页码
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 1读取本级以及下级区划数据，0读本级区划数据
     */
    private String dataPower = "1";
    /**
     * 组织名称模糊
     */
    private String name;
}

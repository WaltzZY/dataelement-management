package com.inspur.dsp.direct.httpService.entity.bsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * bsp部门分页返回实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BspOrganPageResp {

    private int totalRow;
    private int total;
    private String code;
    private int size;
    private List<BspOrganPageColumnsResp> array;
    private int totalPage;
    private int page;
    private int state;

}

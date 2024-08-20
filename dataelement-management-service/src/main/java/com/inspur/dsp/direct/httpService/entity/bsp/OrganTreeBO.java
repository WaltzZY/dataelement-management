package com.inspur.dsp.direct.httpService.entity.bsp;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class OrganTreeBO {

    private List<OrganInfo> organ;

    private Integer max;
}

package com.inspur.dsp.direct.httpService.entity.catalog;

import lombok.Data;

@Data
public class GroupsResp {
    private String cata_id;
    private String group_code;
    private String group_id;
    private String group_name;
    private Integer link_id;
    private Integer link_type;
    private Integer status;
    private Long update_time;
}

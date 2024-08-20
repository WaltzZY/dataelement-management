package com.inspur.dsp.direct.httpService.entity.catalog;

import lombok.Data;

@Data
public class CatalogDetailsColumnResp {


    private String cata_id;
    private String column_id;
    private Long create_time;
    private String data_format;
    private String name_cn;
    private Integer order_id;
    private Integer privacy_data;
    private String sensitive_level;
    private String share_condition;
    private String share_condition_type;
    private String system_id;
    private String system_name;
}

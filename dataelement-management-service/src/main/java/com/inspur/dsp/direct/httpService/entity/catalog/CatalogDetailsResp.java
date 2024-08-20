package com.inspur.dsp.direct.httpService.entity.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogDetailsResp {

    private int api_count;
    private int apply_count;
    private String belong_area;
    private String cata_code;
    private String cata_group_id;
    private String cata_group_name;
    private String cata_id;
    private String cata_title;
    private String cata_version;
    private String catalog_sources;
    private String catalog_type;
    private String certification_type;
    private List<CatalogDetailsColumnResp> columns;
    private long create_time;
    private String creator_id;
    private String creator_name;
    private String credit_code;
    private String description;
    private String duty_type;
    private String field_desc;
    private String field_type;
    private int file_count;
    private int file_down_count;
    private int folder_count;
    private List<GroupsResp> groups;
    private int hainan_visit_count;
    private String is_business;
    private int is_del;
    private String is_huanghe;
    private int is_publish;
    private String item;
    private String item_code;
    private String item_status;
    private String item_type;
    private String messageSource;
    private String net_type;
    private String open_condition;
    private String open_type;
    private String org_code;
    private String org_name;
    private String other_update_cycle;
    private long published_time;
    private String region_code;
    private String resource_format;
    private int revoke_status;
    private String secret_level;
    private String shared_condition;
    private String shared_type;
    private String shared_way;
    private int status;
    private int table_count;
    private String task_handle_item;
    private String update_cycle;
    private long update_time;
    private String use_desc;
    private String use_type;
    private int visit_count;
}

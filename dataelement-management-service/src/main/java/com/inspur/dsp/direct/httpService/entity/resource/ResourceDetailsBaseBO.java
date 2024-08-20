package com.inspur.dsp.direct.httpService.entity.resource;

import lombok.Data;

@Data
public class ResourceDetailsBaseBO {

    /**
     * 接口资源字段 + 其他资源共有字段
     */
    private Integer allow_proxy;
    private Integer apply_count;
    private Integer authorization_type;
    private String batch;
    private Integer browse_count;
    private String cata_id;
    private String creator_id;
    private String creator_name;
    private Integer historyversion;
    private String Integererface_type;
    private Integer is_del;
    private String net_type;
    private String open_condition;
    private String open_type;
    private String org_id;
    private String org_name;
    private String owner_org_id;
    private String owner_org_name;
    private Integer privacy_data;
    private Long publish_date;
    private String region_code;
    private Long register_date;
    private Integer requiredfile;
    private String res_code;
    private String res_desc;
    private String res_id;
    private String res_name;
    private String res_sources;
    private String res_type;
    private String res_version;
    private Integer revoke_status;
    private String share_condition;
    private String share_type;
    private Integer status;
    private String system_id;
    private String system_name;
    private Integer update_cycle;
    private Long validity_date;
    private String service_time;

    /**
     * 库表资源独有字段
     */
    private String area_ranger;
    private String empower_scene;
    private String file_store_type;
    private String interface_type;
    private String item_name;

}

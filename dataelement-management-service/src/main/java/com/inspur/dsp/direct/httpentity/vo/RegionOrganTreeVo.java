package com.inspur.dsp.direct.httpentity.vo;

public class RegionOrganTreeVo {

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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

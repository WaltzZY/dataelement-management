package com.inspur.dsp.direct.httpentity.vo;

import java.util.List;

public class GetOrganByNameLikeVo {

    /**
     * 总条数
     */
    private Long total;
    /**
     * 数据
     */
    private List<OrganInfoVo> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<OrganInfoVo> getData() {
        return data;
    }

    public void setData(List<OrganInfoVo> data) {
        this.data = data;
    }

    public GetOrganByNameLikeVo(Long total, List<OrganInfoVo> data) {
        this.total = total;
        this.data = data;
    }

    public GetOrganByNameLikeVo() {
    }
}

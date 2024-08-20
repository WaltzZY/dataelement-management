package com.inspur.dsp.direct.httpService;


import com.inspur.dsp.direct.httpService.entity.resource.ResourceDetailsBaseBO;

public interface ResourceService {

    /**
     * 查询资源基本信息
     * @param resId 资源id
     * @return
     */
    ResourceDetailsBaseBO getResourceByResId(String resId);

}

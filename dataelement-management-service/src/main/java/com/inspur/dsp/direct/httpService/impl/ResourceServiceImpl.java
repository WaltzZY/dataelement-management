package com.inspur.dsp.direct.httpService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.common.HttpClient;
import com.inspur.dsp.direct.httpService.ResourceService;
import com.inspur.dsp.direct.httpService.entity.resource.ResourceDetailsBaseBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    @Value("${svc.url.catalog:''}")
    private String catalogUrl;

    /**
     * 查询资源基本信息
     *
     * @param resId 资源id
     * @return
     */
    @Override
    public ResourceDetailsBaseBO getResourceByResId(String resId) {
        String resourceUrl = catalogUrl+"/restapi/resource/dubbo/getResourceByResId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("res_id", resId);
        JSONObject jsonObject = HttpClient.httpPostMethod(resourceUrl, JSONObject.toJSONString(params));
        return JSON.toJavaObject(jsonObject, ResourceDetailsBaseBO.class);
    }
}

package com.inspur.dsp.direct.httpService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.common.HttpClient;
import com.inspur.dsp.direct.httpService.CatalogService;
import com.inspur.dsp.direct.httpService.entity.catalog.CatalogDetailsResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    @Value("${svc.url.catalog}")
    private String catalogUrl;

    /**
     * 查询目录详情
     *
     * @param params
     * @return
     */
    @Override
    public CatalogDetailsResp getCatalogById(String cataId) {
        String CatalogUrl = catalogUrl + "/restapi/open/catalog/dubbo/getCatalogById";
        HashMap<String, Object> params = new HashMap<>();
        params.put("cata_id", cataId);
        JSONObject jsonObject = HttpClient.httpPostMethod(CatalogUrl, JSONObject.toJSONString(params));
        return JSON.toJavaObject(jsonObject, CatalogDetailsResp.class);
    }
}

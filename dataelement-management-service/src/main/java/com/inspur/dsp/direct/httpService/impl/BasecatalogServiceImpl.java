package com.inspur.dsp.direct.httpService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.common.HttpClient;
import com.inspur.dsp.direct.httpService.BasecatalogService;
import com.inspur.dsp.direct.httpentity.dto.QueryCatalogByColumnNameDto;
import com.inspur.dsp.direct.httpentity.vo.QueryCatalogByColumnNameVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BasecatalogServiceImpl implements BasecatalogService {

    @Value("${svc.url.basecatalog}")
    private String basecatalogUrl;
    @Value("${svc.url.overallPortal}")
    private String overallPortalUrl;

    /**
     * 模糊查询基准数据元相关数据项
     *
     * @param dto
     * @return
     */
    @Override
    public List<QueryCatalogByColumnNameVo> getRelationCatalogItemLike(QueryCatalogByColumnNameDto dto) {
        String url = basecatalogUrl + "/restapi/getRelationCatalogItemLike";
        JSONObject jsonObject = HttpClient.httpPostMethod(url, JSON.toJSONString(dto));
        if (jsonObject != null && jsonObject.getInteger("code") == 200) {
            JSONArray data = jsonObject.getJSONArray("data");
            List<QueryCatalogByColumnNameVo> javaList = data.toJavaList(QueryCatalogByColumnNameVo.class);
            // 构建总门户目录详情访问地址
            for (QueryCatalogByColumnNameVo vo : javaList) {
                // http://10.110.26.178:8443/overallPortal/fusionRegionCatalog/dataDirectoryDetail?cataId=4d91c19f93fc469585463742625d0e49&org_code=111000000000132664&catalogName=20250918-%E7%B2%BE%E5%87%86%E6%8E%88%E6%9D%83%E7%9B%AE%E5%BD%95&basecatalogStructuretype=1
                String detailUrl = overallPortalUrl + "/fusionRegionCatalog/dataDirectoryDetail?cataId=" + vo.getCatalogId() + "&org_code=" + vo.getCatalogUnitCode() + "&catalogName=" + vo.getCatalogName();
                vo.setCatalogDetailUrl(detailUrl);
            }
            return javaList;
        }
        return Collections.emptyList();
    }

    /**
     * 获取目录总门户预览地址
     *
     * @param catalogId
     * @return
     */
    @Override
    public String getCatalogPreviewUrl(String catalogId, String catalogUnitCode, String catalogName) {
        return overallPortalUrl + "/fusionRegionCatalog/dataDirectoryDetail?cataId=" + catalogId + "&org_code=" + catalogUnitCode + "&catalogName=" + catalogName;
    }
}

package com.inspur.dsp.direct.httpService;

import com.inspur.dsp.direct.httpService.entity.catalog.CatalogDetailsResp;

public interface CatalogService {

    /**
     * 查询目录详情
     * @param cataId 目录id
     * @return
     */
    CatalogDetailsResp getCatalogById(String cataId);

}

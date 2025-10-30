package com.inspur.dsp.direct.httpService;

import com.inspur.dsp.direct.httpentity.dto.QueryCatalogByColumnNameDto;
import com.inspur.dsp.direct.httpentity.vo.QueryCatalogByColumnNameVo;

import java.util.List;

public interface BasecatalogService {

    /**
     * 模糊查询基准数据元相关数据项
     * @param dto
     * @return
     */
    List<QueryCatalogByColumnNameVo> getRelationCatalogItemLike(QueryCatalogByColumnNameDto dto);

    /**
     * 获取目录总门户预览地址
     * @param catalogId 目录id
     * @param catalogUnitCode 目录部门编码
     * @param catalogName 目录名称
     * @return
     */
    String getCatalogPreviewUrl(String catalogId, String catalogUnitCode, String catalogName);
}

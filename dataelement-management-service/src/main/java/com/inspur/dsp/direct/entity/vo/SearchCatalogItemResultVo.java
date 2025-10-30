package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import java.util.List;

/**
 * 查询目录数据项结果VO
 */
@Data
public class SearchCatalogItemResultVo {

    private List<CatalogItemVo> items;
    
    private int pageNum;
    
    private int pageSize;
    
    private long total;
    
    private int pages;
}
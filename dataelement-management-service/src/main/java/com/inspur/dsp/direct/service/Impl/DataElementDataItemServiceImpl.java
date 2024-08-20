package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementDataItemDao;
import com.inspur.dsp.direct.dbentity.business.DataElementDataItem;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;
import com.inspur.dsp.direct.entity.vo.DataItemInfoVO;
import com.inspur.dsp.direct.exception.CustomException;
import com.inspur.dsp.direct.httpService.CatalogService;
import com.inspur.dsp.direct.httpService.ResourceService;
import com.inspur.dsp.direct.httpService.entity.catalog.CatalogDetailsColumnResp;
import com.inspur.dsp.direct.httpService.entity.catalog.CatalogDetailsResp;
import com.inspur.dsp.direct.httpService.entity.resource.ResourceDetailsBaseBO;
import com.inspur.dsp.direct.service.DataElementDataItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据元和数据资源数据项关联表
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataElementDataItemServiceImpl extends ServiceImpl<DataElementDataItemDao, DataElementDataItem> implements DataElementDataItemService {

    private final DataElementDataItemDao dataElementDataItemDao;
    private final ResourceService resourceService;
    private final CatalogService catalogService;

    @Override
    public List<DataElementDataItemVO> getList(String dataElementName) {
        List<DataElementDataItemVO> dataList = dataElementDataItemDao.getList(dataElementName);
        return dataList;
    }

    /**
     * 获取数据资源详情
     *
     * @param dataResourceId
     * @return
     */
    @Override
    public DataItemInfoVO getDataItemInfo(String dataResourceId) {
        // 查询资源详情
        ResourceDetailsBaseBO resourceByResId = resourceService.getResourceByResId(dataResourceId);
        if (Objects.isNull(resourceByResId)) {
            throw new CustomException("资源不存在");
        }
        // 查询目录详情
        CatalogDetailsResp catalogInfo = catalogService.getCatalogById(resourceByResId.getCata_id());
        if (Objects.isNull(catalogInfo)) {
            throw new CustomException("目录不存在");
        }
        // 封装数据
        DataItemInfoVO dataItemInfoVO = new DataItemInfoVO();
        dataItemInfoVO.setCateId(catalogInfo.getCata_id());
        dataItemInfoVO.setCateName(catalogInfo.getCata_title());
        dataItemInfoVO.setDataResourceId(resourceByResId.getRes_id());
        dataItemInfoVO.setDataResourceName(resourceByResId.getRes_name());
        List<CatalogDetailsColumnResp> columns = catalogInfo.getColumns();
        // 数据项列表
        List<String> items = Optional.ofNullable(columns)
                .map(l -> l.stream().map(CatalogDetailsColumnResp::getName_cn)
                        .filter(StringUtils::hasText).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        dataItemInfoVO.setItem(items);
        dataItemInfoVO.setOrgCode(catalogInfo.getOrg_code());
        dataItemInfoVO.setOrgName(catalogInfo.getOrg_name());
        return dataItemInfoVO;
    }
}

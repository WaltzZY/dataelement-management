package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.DataElementBaseDao;
import com.inspur.dsp.direct.dao.business.DataElementBelongCategoryDao;
import com.inspur.dsp.direct.dao.business.DataElementCollectorgDao;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.dbentity.business.DataElementBelongCategory;
import com.inspur.dsp.direct.dbentity.business.DataElementCollectorg;
import com.inspur.dsp.direct.entity.bo.DataElementCategoryInfoBO;
import com.inspur.dsp.direct.entity.dto.GetDetailedCountDTO;
import com.inspur.dsp.direct.entity.dto.GetDetailedListDTO;
import com.inspur.dsp.direct.entity.vo.DetailedCountVO;
import com.inspur.dsp.direct.entity.vo.GetDetailedListVO;
import com.inspur.dsp.direct.enums.DataElementStatusEnum;
import com.inspur.dsp.direct.httpService.BSPService;
import com.inspur.dsp.direct.service.DataElementBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 基准数据元表
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataElementBaseServiceImpl extends ServiceImpl<DataElementBaseDao, DataElementBase> implements DataElementBaseService {

    private final DataElementBaseDao dataElementBaseDao;
    private final DataElementBelongCategoryDao belongCategoryDao;
    private final DataElementCollectorgDao collectorgDao;
    private final BSPService bspService;

    /**
     * 查询清单列表
     *
     * @param dto
     * @return
     */
    @Override
    public Page<GetDetailedListVO> getDetailedList(GetDetailedListDTO dto) {
        Page<GetDetailedListVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<GetDetailedListVO> detailedList = dataElementBaseDao.getDetailedList(page, dto);
        List<GetDetailedListVO> records = detailedList.getRecords();
        List<String> ids = records.stream().map(GetDetailedListVO::getDataElementId).collect(Collectors.toList());
        // 查询数据元id和数据元分类code
        List<DataElementCategoryInfoBO> categoryList = belongCategoryDao.selectDataElementIdAndCategoryNameByDataElementIdIn(ids);
        // map key:dataElementId value:dataElementCategoryNames
        Map<String, List<String>> idCategoryMap = Optional.ofNullable(categoryList)
                .map(list ->
                        list.stream().filter(Objects::nonNull)
                                // 按数据元id对数据进行分组
                                .collect(Collectors.groupingBy(DataElementCategoryInfoBO::getDataElementId
                                        , Collectors.collectingAndThen(Collectors.toList(),
                                                // 对分组后的集合进行二次处理,只保留catalogueName不为空的数据
                                                l -> l.stream().map(DataElementCategoryInfoBO::getDataElementCategoryName)
                                                        .filter(StringUtils::hasText).collect(Collectors.toList())))
                                )).orElse(new HashMap<>());
        // 查询数据元id和数据元组织code
        List<DataElementCollectorg> dataElementCollectorgs = collectorgDao.selectDataElementIdAndOrgCodeByDataElementIdIn(ids);
        // map key:dataElementId value:dataElementOrgCodes
        Map<String, List<String>> idOrgMap = Optional.ofNullable(dataElementCollectorgs)
                .map(list -> list.stream().filter(Objects::nonNull)
                        // 按数据元id对数据进行分组
                        .collect(Collectors.groupingBy(DataElementCollectorg::getDataElementId,
                                // 对分组后的集合进行二次处理,只保留orgCode不为空的数据
                                Collectors.collectingAndThen(Collectors.toList(), l -> l.stream()
                                        .map(DataElementCollectorg::getDataElementCollectOrgName)
                                        .filter(StringUtils::hasText).collect(Collectors.toList()))
                        ))).orElse(new HashMap<>());

        // 查询数据类型
        Map<String, String> dataTypeDictMap = bspService.getDataTypeDictMap();
        // 封装数据
        records.forEach(vo -> {
            // 状态
            vo.setDataElementStatus(DataElementStatusEnum.getNameByCode(vo.getDataElementStatus()));
            // 分类
            List<String> dataElementCategoryCodes = idCategoryMap.get(vo.getDataElementId());
            vo.setCategoryList(!CollectionUtils.isEmpty(dataElementCategoryCodes) ? dataElementCategoryCodes : new ArrayList<>());
            // 组织
            List<String> dataElementOrgCodes = idOrgMap.get(vo.getDataElementId());
            vo.setDataElementCollectOrgNames(!CollectionUtils.isEmpty(dataElementOrgCodes) ? dataElementOrgCodes : new ArrayList<>());
            // 数据类型
            vo.setDataElementDataType(dataTypeDictMap.get(vo.getDataElementDataType()));
        });
        return detailedList;
    }

    /**
     * 查询清单列表汇总
     *
     * @param dto
     * @return
     */
    @Override
    public DetailedCountVO getDetailedCount(GetDetailedCountDTO dto) {
        // 初始化响应对象
        DetailedCountVO vo = new DetailedCountVO(0, 0, 0, 0, 0, 0);
        // 查询清单列表汇总
        List<DataElementBase> detailedCount = dataElementBaseDao.getDetailedCount(dto);
        if (CollectionUtils.isEmpty(detailedCount)) {
            return vo;
        }
        // 数据元个数
        vo.setDataelementCount(detailedCount.size());
        // 查询关联单位个数
        Integer collectOrgCount = dataElementBaseDao.getCollectOrgCount(dto);
        vo.setDeptCount(collectOrgCount);
        // 各状态数据元个数
        Map<String, Integer> statusCountMap = detailedCount.stream()
                .filter(l -> StringUtils.hasText(l.getDataElementStatus()))
                .collect(Collectors.groupingBy(DataElementBase::getDataElementStatus
                        , Collectors.collectingAndThen(Collectors.toList(), List::size
                        )));
        statusCountMap.forEach((k, v) -> {
            if (DataElementStatusEnum.WAIT_SOURCE.getCode().equals(k)) {
                vo.setStatus01(v);
            } else if (DataElementStatusEnum.WAIT_STANDARD.getCode().equals(k)) {
                vo.setStatus02(v);
            } else if (DataElementStatusEnum.PUBLISHED.getCode().equals(k)) {
                vo.setStatus03(v);
            } else if (DataElementStatusEnum.DISCARDED.getCode().equals(k)) {
                vo.setStatus04(v);
            }
        });
        return vo;
    }
}

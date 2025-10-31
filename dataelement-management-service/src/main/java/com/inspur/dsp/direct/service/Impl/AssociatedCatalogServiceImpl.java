package com.inspur.dsp.direct.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.DataElementCatalogRelationMapper;
import com.inspur.dsp.direct.dao.DomainDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.DataElementCatalogRelation;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;
import com.inspur.dsp.direct.entity.vo.CatalogAssociatedDTO;
import com.inspur.dsp.direct.entity.vo.DataElementCatalogRelationVO;
import com.inspur.dsp.direct.httpService.BasecatalogService;
import com.inspur.dsp.direct.service.AssociatedCatalogService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AssociatedCatalogServiceImpl implements AssociatedCatalogService {

    private static final String URL = "/restapi/getRelationCatalogItemLike";

    @Value("${svc.url.basecatalog}")
    private String basecatalogUrl;

    @Value("${svc.url.overallPortal}")
    private String overallPortalUrl;

    @Autowired
    private BasecatalogService basecatalogService;

    @Autowired
    private DataElementCatalogRelationMapper dataElementCatalogRelationMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private DomainDataElementMapper domainDataElementMapper;

    @Override
    public List<DataElementCatalogRelationVO> getAssociatedDataSourceCatalog(String sourceUnitCode, String baseDataId) {
        try {
            List<DataElementCatalogRelation> relations = dataElementCatalogRelationMapper.getAssociatedDataSourceCatalog(sourceUnitCode, baseDataId);
            if (relations == null || relations.isEmpty()) {
                return new ArrayList<>();
            }
            List<DataElementCatalogRelationVO> result = new ArrayList<>();
            for (DataElementCatalogRelation relation : relations) {
                DataElementCatalogRelationVO vo = new DataElementCatalogRelationVO();
                BeanUtils.copyProperties(relation, vo);
                String detailUrl = getUrl(vo);
                vo.setDetailUrl(detailUrl);
                result.add(vo);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("数据查询失败", e);
        }
    }


    @Override
    public List<DataElementCatalogRelationVO> getAssociatedCollectorCatalog(String sourceUnitCode, String baseDataId) {
        try {
            List<DataElementCatalogRelation> relations = dataElementCatalogRelationMapper.getAssociatedCollectorCatalog(sourceUnitCode, baseDataId);

            if (relations == null || relations.isEmpty()) {
                return new ArrayList<>();
            }

            List<DataElementCatalogRelationVO> result = new ArrayList<>();
            for (DataElementCatalogRelation relation : relations) {
                DataElementCatalogRelationVO vo = new DataElementCatalogRelationVO();
                BeanUtils.copyProperties(relation, vo);
                String detailUrl = getUrl(vo);
                vo.setDetailUrl(detailUrl);
                result.add(vo);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("数据查询失败", e);
        }
    }

    @Override
    public String cancelCatalogAssociation(String dataid) {
        try {
            Boolean result = dataElementCatalogRelationMapper.cancelCatalogAssociation(dataid);
            return result ? "取消目录关联成功" : "取消目录关联失败";
        } catch (Exception e) {
            throw new RuntimeException("目录删除失败", e);
        }
    }

    @Override
    public List<AssociatedDataItemListVO> getAssociatedDataItemList(String keyword, List<String> orgCodeList) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("name", keyword != null ? keyword : "");
            param.put("orgCodes", orgCodeList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);
            return getItemList(request);
        } catch (Exception e) {
            throw new RuntimeException("调用接口失败", e);
        }
    }


    @Override
    public List<AssociatedDataItemListVO> getAssociatedDataItemForCollectorList(String keyword, List<String> orgCodeList) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("name", keyword != null ? keyword : "");
            param.put("orgCodes", orgCodeList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);
            return getItemList(request);
        } catch (Exception e) {
            throw new RuntimeException("调用接口失败", e);
        }
    }

    @Override
    public List<AssociatedDataItemListVO> getAssociatedDataItemWithAllList(String keyword, String dataid) {
        try {
            List<String> baseOrgCodes = baseDataElementMapper.getBaseDataForOrgCode(dataid);
            List<String> domainOrgCodes = domainDataElementMapper.getDomainDataForOrgCode(dataid);

            List<String> orgCodeList = new ArrayList<>();
            if (baseOrgCodes != null) orgCodeList.addAll(baseOrgCodes);
            if (domainOrgCodes != null) orgCodeList.addAll(domainOrgCodes);

            Map<String, Object> param = new HashMap<>();
            param.put("name", keyword != null ? keyword : "");
            param.put("orgCodes", orgCodeList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);
            return getItemWithFlagList(request);
        } catch (Exception e) {
            throw new RuntimeException("调用接口失败", e);
        }
    }

    @Override
    public String saveCatalogAssociation(List<CatalogAssociatedDTO> catalogList) {
        try {
            for (CatalogAssociatedDTO catalog : catalogList) {
                DataElementCatalogRelation existingRelation = dataElementCatalogRelationMapper.findRelationExist(catalog.getInfoItemId(), catalog.getDataElementId());

                if (existingRelation == null) {
                    // 新增操作
                    DataElementCatalogRelation newRelation = new DataElementCatalogRelation();
                    BeanUtils.copyProperties(catalog, newRelation);
                    newRelation.setDataid(UUID.randomUUID().toString());
                    UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
                    // 获取登录人账户
                    newRelation.setCreateAccount(userInfo.getAccount());
                    newRelation.setCreateDate(new Date());
                    // 设置创建账号等信息
                    dataElementCatalogRelationMapper.insert(newRelation);
                } else {
                    // 先删除再新增
                    dataElementCatalogRelationMapper.deleteCatalogAssociation(existingRelation.getDataid());

                    DataElementCatalogRelation newRelation = new DataElementCatalogRelation();
                    BeanUtils.copyProperties(catalog, newRelation);
                    UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
                    // 获取登录人账户
                    newRelation.setCreateAccount(userInfo.getAccount());
                    newRelation.setCreateDate(new Date());
                    newRelation.setDataid(UUID.randomUUID().toString());
                    dataElementCatalogRelationMapper.insert(newRelation);
                }
            }
            return "执行成功";
        } catch (Exception e) {
            throw new RuntimeException("保存目录关联失败", e);
        }
    }


    private String getUrl(DataElementCatalogRelationVO vo) {
        return overallPortalUrl + "/fusionRegionCatalog/dataDirectoryDetail?cataId=" + vo.getCatalogId() + "&org_code=" + vo.getCatalogUnitCode() + "&catalogName=" + vo.getCatalogName();
    }

    private List<AssociatedDataItemListVO> getItemList(HttpEntity<Map<String, Object>> request) {
        String result = restTemplate.postForObject(basecatalogUrl + URL, request, String.class);
        if (result == null) {
            return Collections.emptyList();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer code = jsonObject.getInteger("code");
        if (code != 200) {
            return Collections.emptyList();
        }
        JSONArray dataArray = jsonObject.getJSONArray("data");
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<AssociatedDataItemListVO> resultList = new ArrayList<>();
        for (Object datum : dataArray) {
            AssociatedDataItemListVO associatedDataItemListVO = JSONObject.parseObject(datum.toString(), AssociatedDataItemListVO.class);
            resultList.add(associatedDataItemListVO);
        }
        return resultList;
    }

    private List<AssociatedDataItemListVO> getItemWithFlagList(HttpEntity<Map<String, Object>> request) {
        String result = restTemplate.postForObject(basecatalogUrl + URL, request, String.class);
        if (result == null) {
            return Collections.emptyList();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer code = jsonObject.getInteger("code");
        if (code != 200) {
            return Collections.emptyList();
        }
        JSONArray dataArray = jsonObject.getJSONArray("data");
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<AssociatedDataItemListVO> resultList = new ArrayList<>();
        for (Object datum : dataArray) {
            AssociatedDataItemListVO associatedDataItemListVO = JSONObject.parseObject(datum.toString(), AssociatedDataItemListVO.class);
            String catalogUnitCode = associatedDataItemListVO.getCatalogUnitCode();
            QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("source_unit_code", catalogUnitCode);
            Long count = baseDataElementMapper.selectCount(queryWrapper);
            associatedDataItemListVO.setFlag(count == 0L ? 1 : 2);
            resultList.add(associatedDataItemListVO);
        }
        return resultList;
    }
}
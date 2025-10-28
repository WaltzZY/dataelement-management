package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.dao.*;
import com.inspur.dsp.direct.dbentity.*;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;
import com.inspur.dsp.direct.entity.vo.BaseDataAndOrganizationUnitVO;
import com.inspur.dsp.direct.entity.vo.ProcessMessageVO;
import com.inspur.dsp.direct.service.DataSourceFileCardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataSourceFileCardServiceImpl implements DataSourceFileCardService {


    @Value("${svc.url.overallPortal}")
    private String overallPortalUrl;


    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private DomainDataElementMapper domainDataElementMapper;

    @Autowired
    private OrganizationUnitMapper organizationUnitMapper;

    @Autowired
    private ProcessRecordMapper processRecordMapper;

    @Autowired
    private DataElementCatalogRelationMapper dataElementCatalogRelationMapper;

    @Override
    public BaseDataElement getBaseMessage(String dataid) {
        try {
            return baseDataElementMapper.selectById(dataid);
        } catch (Exception e) {
            log.error("获取基本信息失败，dataid: {}", dataid, e);
            throw new RuntimeException("数据查询失败", e);
        }
    }

    @Override
    public BaseDataAndOrganizationUnitVO getDataSourceUnit(String dataid) {
        try {
            BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
            if (baseDataElement == null) {
                throw new RuntimeException("当前数据不存在");
            }

            BaseDataAndOrganizationUnitVO vo = new BaseDataAndOrganizationUnitVO();
            vo.setDataid(baseDataElement.getDataid());
            vo.setSourceUnitCode(baseDataElement.getSourceUnitCode());
            vo.setSourceUnitName(baseDataElement.getSourceUnitName());

            // 查询组织单位信息
            OrganizationUnit organizationUnit = organizationUnitMapper.selectFirstByUnitCode(baseDataElement.getSourceUnitCode());
            if (organizationUnit != null) {
                vo.setUnitCode(organizationUnit.getUnitCode());
                vo.setUnitName(organizationUnit.getUnitName());
                vo.setContactName(organizationUnit.getContactName());
                vo.setContactPhone(organizationUnit.getContactPhone());
            }
            return vo;
        } catch (Exception e) {
            log.error("获取数源单位信息失败，dataid: {}", dataid, e);
            throw new RuntimeException("数据查询失败", e);
        }
    }

    @Override
    public List<DomainDataElement> getCollectUnit(String dataid) {
        try {

            BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
            List<DomainDataElement> resultFromBase = domainDataElementMapper.selectAllByBaseDataelementDataid(dataid);
            List<DomainDataElement> result = new ArrayList<>();
            if (baseDataElement != null && StringUtils.isNotBlank(baseDataElement.getSourceUnitCode())) {
                String sourceUnitCodeFromBase = baseDataElement.getSourceUnitCode();
                for (DomainDataElement domainDataElement : resultFromBase) {
                    String sourceUnitCode = domainDataElement.getSourceUnitCode();
                    if (!sourceUnitCodeFromBase.equals(sourceUnitCode)) {
                        result.add(domainDataElement);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取采集单位信息失败，dataid: {}", dataid, e);
            throw new RuntimeException("查询数据失败", e);
        }
    }

    @Override
    public List<ProcessMessageVO> getProcessMessage(String dataid) {
        try {
            List<ProcessMessageVO> resultList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 1. 从BaseDataElement获取处理信息
            BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
            if (baseDataElement != null && baseDataElement.getGeneratedatetime() != null) {
                ProcessMessageVO vo = new ProcessMessageVO();
                vo.setDataid(dataid);
                vo.setProcessName("数据生成");
                vo.setUnitName(baseDataElement.getSourceUnitName());
                vo.setProcessDate(dateFormat.format(baseDataElement.getGeneratedatetime()));
                resultList.add(vo);
            }

            // 2. 从SourceEventRecord获取处理信息
            SourceEventRecord sourceEventRecord = sourceEventRecordMapper.getProcessMessageForRecord(dataid);
            if (sourceEventRecord != null) {
                ProcessMessageVO vo = new ProcessMessageVO();
                vo.setDataid(dataid);
                vo.setProcessName("事件记录");
                vo.setUnitName(sourceEventRecord.getSendUnitName());
                vo.setProcessDate(dateFormat.format(sourceEventRecord.getSourceDate()));
                resultList.add(vo);
            }

            // 3. 从ProcessRecord获取处理信息
            ProcessRecord processRecord = processRecordMapper.getProcessRecoderForPublished(dataid);
            if (processRecord != null) {
                ProcessMessageVO vo = new ProcessMessageVO();
                vo.setDataid(dataid);
                vo.setProcessName("发布记录");
                vo.setUnitName(processRecord.getProcessunitname());
                vo.setProcessDate(dateFormat.format(processRecord.getProcessdatetime()));
                resultList.add(vo);
            }

            return resultList;
        } catch (Exception e) {
            log.error("获取处理信息失败，dataid: {}", dataid, e);
            throw new RuntimeException("调用接口失败", e);
        }
    }

    @Override
    public List<AssociatedDataItemListVO> getShareMessage(String dataid, String sourceUnitCode) {
        try {
            List<AssociatedDataItemListVO> resultList = new ArrayList<>();

            // 获取数源单位关联目录
            List<DataElementCatalogRelation> dataSourceRelations = dataElementCatalogRelationMapper.getAssociatedDataSourceCatalog(sourceUnitCode, dataid);
            if (dataSourceRelations != null) {
                for (DataElementCatalogRelation relation : dataSourceRelations) {
                    AssociatedDataItemListVO vo = convertToAssociatedDataItemVO(relation);
                    vo.setFlag(1); // 数源单位数据
                    resultList.add(vo);
                }
            }

            // 获取采集单位关联目录
            List<DataElementCatalogRelation> collectorRelations = dataElementCatalogRelationMapper.getAssociatedCollectorCatalog(sourceUnitCode, dataid);
            if (collectorRelations != null) {
                for (DataElementCatalogRelation relation : collectorRelations) {
                    AssociatedDataItemListVO vo = convertToAssociatedDataItemVO(relation);
                    vo.setFlag(2); // 采集单位数据
                    resultList.add(vo);
                }
            }

            return resultList;
        } catch (Exception e) {
            log.error("获取共享信息失败，dataid: {}, sourceUnitCode: {}", dataid, sourceUnitCode, e);
            throw new RuntimeException("调用接口失败", e);
        }
    }

    private AssociatedDataItemListVO convertToAssociatedDataItemVO(DataElementCatalogRelation relation) {
        AssociatedDataItemListVO vo = new AssociatedDataItemListVO();
        BeanUtils.copyProperties(relation, vo);
        // 设置URL地址
        String url = getUrl(vo);
        vo.setDetailUrl(url);
        return vo;
    }

    private String getUrl(AssociatedDataItemListVO vo) {
        return overallPortalUrl + "/fusionRegionCatalog/dataDirectoryDetail?cataId=" + vo.getCatalogId() + "&org_code=" + vo.getCatalogUnitCode() + "&catalogName=" + vo.getCatalogName();
    }

}
package com.inspur.dsp.direct.console.controller.determine;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.RequestCatalogAssociatedDTO;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;
import com.inspur.dsp.direct.entity.vo.CatalogAssociatedDTO;
import com.inspur.dsp.direct.entity.vo.DataElementCatalogRelationVO;
import com.inspur.dsp.direct.service.AssociatedCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associated/catalog")
public class AssociatedCatalogController {

    @Autowired
    private AssociatedCatalogService associatedCatalogService;

    /**
     * 已关联数源单位目录
     */
    @GetMapping("/dataSourceList")
    @RespAdvice
    public List<DataElementCatalogRelationVO> getAssociatedDataSourceCatalog(@RequestParam(value = "sourceUnitCode") String sourceUnitCode, @RequestParam(value = "baseDataId") String baseDataId) {
        return associatedCatalogService.getAssociatedDataSourceCatalog(sourceUnitCode, baseDataId);
    }

    /**
     * 已关联采集单位目录
     */
    @GetMapping("/collectorList")
    @RespAdvice
    public List<DataElementCatalogRelationVO> getAssociatedCollectorCatalog(@RequestParam(value = "sourceUnitCode") String sourceUnitCode, @RequestParam(value = "baseDataId") String baseDataId) {
        return associatedCatalogService.getAssociatedCollectorCatalog(sourceUnitCode, baseDataId);
    }

    /**
     * 取消目录关联
     */
    @PostMapping("/cancel")
    @RespAdvice
    public String cancelCatalogAssociation(@RequestParam(value = "dataid") String dataid) {
        return associatedCatalogService.cancelCatalogAssociation(dataid);
    }

    @PostMapping("/dataItem/list")
    @RespAdvice
    public List<AssociatedDataItemListVO> getAssociatedDataItemList(@RequestBody RequestCatalogAssociatedDTO requestCatalogAssociatedDTO) {
        String keyword = requestCatalogAssociatedDTO.getKeyword();
        List<String> orgCodeList = requestCatalogAssociatedDTO.getOrgCodeList();
        return associatedCatalogService.getAssociatedDataItemList(keyword, orgCodeList);
    }

    @PostMapping("/dataItem/collector/list")
    @RespAdvice
    public List<AssociatedDataItemListVO> getAssociatedDataItemForCollectorList(@RequestBody RequestCatalogAssociatedDTO requestCatalogAssociatedDTO) {
        String keyword = requestCatalogAssociatedDTO.getKeyword();
        List<String> orgCodeList = requestCatalogAssociatedDTO.getOrgCodeList();
        return associatedCatalogService.getAssociatedDataItemForCollectorList(keyword, orgCodeList);
    }

    @PostMapping("/dataItem/all/list")
    @RespAdvice
    public List<AssociatedDataItemListVO> getAssociatedDataItemWithAllList(@RequestBody RequestCatalogAssociatedDTO requestCatalogAssociatedDTO) {
        String keyword = requestCatalogAssociatedDTO.getKeyword();
        String dataid = requestCatalogAssociatedDTO.getDataid();
        return associatedCatalogService.getAssociatedDataItemWithAllList(keyword, dataid);
    }

    /**
     * 保存关联目录及数据项功能
     */
    @PostMapping("/save")
    @RespAdvice
    public String saveCatalogAssociation(@RequestBody List<CatalogAssociatedDTO> catalogAssociateList) {
        return associatedCatalogService.saveCatalogAssociation(catalogAssociateList);
    }
}
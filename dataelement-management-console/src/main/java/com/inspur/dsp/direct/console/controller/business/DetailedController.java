package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;
import com.inspur.dsp.direct.service.DataElementCollectOrgService;
import com.inspur.dsp.direct.service.DataElementDataItemService;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import com.inspur.dsp.direct.entity.vo.DataElementAttributeVO;
import com.inspur.dsp.direct.entity.vo.VocabularyVO;
import com.inspur.dsp.direct.service.DataElementAttributeService;
import com.inspur.dsp.direct.service.DataElementBaseService;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import com.inspur.dsp.direct.service.DataElementVocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import java.util.List;

/**
 * 基准数据元清单
 */
@Slf4j
@RestController
@RequestMapping("/detailed")
@RequiredArgsConstructor
public class DetailedController {

    private final DataElementBaseService dataElementBaseService;
    private final DataElementAttributeService dataElementAttributeService;
    private final DataElementVocabularyService dataElementVocabularyService;
    private final DataElementBelongCategoryService dataElementBelongCategoryService;

    /**
     * 数据元基本信息查询
     * @param dataElementId 数据元id
     * @return
     */
    @RespAdvice
    @GetMapping("/getDataelementInfo")
    public DataElementBase getDataelementInfo(@RequestParam("dataElementId")String dataElementId){
        DataElementBase dataElementBase = dataElementBaseService.getById(dataElementId);
        return dataElementBase;
    }

    /**
     * 属性信息
     * @param dataElementId 数据元id
     * @return
     */
    @RespAdvice
    @GetMapping("/attribute")
    public List<DataElementAttributeVO> attribute(@RequestParam("dataElementId")String dataElementId){
        List<DataElementAttributeVO> dataElementAttributeVOS = dataElementAttributeService.getDataElementAttributeByDataElementId(dataElementId);
        return dataElementAttributeVOS;
    }

    /**
     * 查询词汇
     * @param dataElementId 数据元id
     * @return
     */
    @RespAdvice
    @GetMapping("/getVocabularylist")
    public VocabularyVO getVocabularylist(@RequestParam("dataElementId")String dataElementId){
        VocabularyVO vocabularyVO = dataElementVocabularyService.getVocabularylist(dataElementId);
        return vocabularyVO;
    }

    /**
     * 查询分类
     * @param dataElementId 数据元id
     * @return
     */
    @RespAdvice
    @GetMapping("/getCategorylist")
    public List<ClassIfiCationMethodVO> getCategorylist(@RequestParam("dataElementId")String dataElementId){
        List<ClassIfiCationMethodVO> cationMethodVOS = dataElementBelongCategoryService.getCategoryByDataElementId(dataElementId);
        return cationMethodVOS;
    }



    @Resource
    private DataElementCollectOrgService dataElementCollectOrgService;
    @Resource
    private DataElementDataItemService dataElementDataItemService;

    @GetMapping("/getCollectOrgList")
    @RespAdvice
    @SysLog(title = "关联单位接口", modelName = "获取关联单位")
    public List<DataElementCollectOrgVO> getCollectOrgList(String dataElementId) {
        return dataElementCollectOrgService.getList(dataElementId);
    }

    @GetMapping("/getCollectOrgList")
    @RespAdvice
    @SysLog(title = "关联资源接口", modelName = "获取关联资源")
    public List<DataElementDataItemVO> getDataItemList(String dataElementId) {
        return dataElementDataItemService.getList(dataElementId);
    }
}

package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.vo.*;
import com.inspur.dsp.direct.service.*;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
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
    @Resource
    private DataElementCollectOrgService dataElementCollectOrgService;
    @Resource
    private DataElementDataItemService dataElementDataItemService;
    @Resource
    private DataElementSceneService dataElementSceneService;
    @Resource
    private DataElementSampleService dataElementSampleService;
    @Resource
    private DataElementStandardService dataElementStandardService;

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

    /**
     * 获取关联单位
     * @param dataElementId
     * @return
     */
    @GetMapping("/getCollectOrgList")
    @RespAdvice
    public List<DataElementCollectOrgVO> getCollectOrgList(String dataElementId) {
        return dataElementCollectOrgService.getList(dataElementId);
    }

    /**
     * 获取关联资源
     * @param dataElementName
     * @return
     */
    @GetMapping("/getDataItemList")
    @RespAdvice
    public List<DataElementDataItemVO> getDataItemList(String dataElementName) {
        return dataElementDataItemService.getList(dataElementName);
    }

    /**
     * 获取场景
     * @param dataElementId
     * @return
     */
    @GetMapping("/getSceneList")
    @RespAdvice
    public List<DataElementSceneVO> getSceneList(String dataElementId) {
        return dataElementSceneService.getList(dataElementId);
    }

    /**
     * 获取样例
     * @param dataElementId
     * @return
     */
    @GetMapping("/getSampleList")
    @RespAdvice
    public List<DataElementSampleVO> getSampleList(String dataElementId) {
        return dataElementSampleService.getList(dataElementId);
    }

    /**
     * 获取标准文件
     * @param dataElementId
     * @return
     */
    @GetMapping("/getStandardList")
    @RespAdvice
    public List<DataElementStandardVO> getStandardList(String dataElementId) {
        return dataElementStandardService.getList(dataElementId);
    }
}

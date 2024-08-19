package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.dto.GovDeptDTO;
import com.inspur.dsp.direct.entity.vo.GovDeptVO;
import com.inspur.dsp.direct.entity.vo.RegionAndOrgan;
import com.inspur.dsp.direct.httpService.BSPService;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetDetailedCountDTO;
import com.inspur.dsp.direct.entity.dto.GetDetailedListDTO;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import com.inspur.dsp.direct.entity.vo.DataElementAttributeVO;
import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;
import com.inspur.dsp.direct.entity.vo.DataElementSampleVO;
import com.inspur.dsp.direct.entity.vo.DataElementSceneVO;
import com.inspur.dsp.direct.entity.vo.DataElementStandardVO;
import com.inspur.dsp.direct.entity.vo.DetailedCountVO;
import com.inspur.dsp.direct.entity.vo.GetDetailedListVO;
import com.inspur.dsp.direct.entity.vo.VocabularyVO;
import com.inspur.dsp.direct.service.DataElementAttributeService;
import com.inspur.dsp.direct.service.DataElementBaseService;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import com.inspur.dsp.direct.service.DataElementCollectOrgService;
import com.inspur.dsp.direct.service.DataElementDataItemService;
import com.inspur.dsp.direct.service.DataElementSampleService;
import com.inspur.dsp.direct.service.DataElementSceneService;
import com.inspur.dsp.direct.service.DataElementStandardService;
import com.inspur.dsp.direct.service.DataElementVocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
    private final BSPService bspService;
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
        // 查询数据类型
        Map<String, String> dataTypeDictMap = bspService.getDataTypeDictMap();
        //设置数据类型
        if (Objects.nonNull(dataElementBase)){
            dataElementBase.setDataElementDataType(dataTypeDictMap.get(dataElementBase.getDataElementDataType()));
        }
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

    @PostMapping("/list")
    @SysLog(title = "基准数据元清单", modelName = "清单列表")
    @RespAdvice
    public Page<GetDetailedListVO> getDetailedList(@RequestBody @Validated GetDetailedListDTO dto) {
        return dataElementBaseService.getDetailedList(dto);
    }

    @PostMapping("/count")
    @SysLog(title = "基准数据元清单", modelName = "清单列表汇总")
    @RespAdvice
    public DetailedCountVO getDetailedCount(@RequestBody GetDetailedCountDTO dto) {
        return dataElementBaseService.getDetailedCount(dto);
    }

    @GetMapping("/regionAndOrgan")
    @SysLog(title = "基准数据元清单", modelName = "获取一级行政区和国家部委")
    @RespAdvice
    public RegionAndOrgan regionAndOrgan() {
        return dataElementBaseService.regionAndOrgan();
    }

    @PostMapping("/govdept")
    @SysLog(title = "基准数据元清单", modelName = "分页模糊查询全国部门")
    @RespAdvice
    public Page<GovDeptVO> getGovDept(@RequestBody GovDeptDTO dto) {
        return dataElementBaseService.getGovDept(dto);
    }
}

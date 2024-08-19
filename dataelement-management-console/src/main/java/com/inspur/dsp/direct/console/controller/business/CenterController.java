package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.business.DataElementBase;
import com.inspur.dsp.direct.dbentity.business.DataElementVocabulary;
import com.inspur.dsp.direct.enums.DataElementStatusEnum;
import com.inspur.dsp.direct.service.DataElementBaseService;
import com.inspur.dsp.direct.service.DataElementVocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基准数据元中心
 */
@Slf4j
@RestController
@RequestMapping("/center")
@RequiredArgsConstructor
public class CenterController {
    private final DataElementBaseService dataElementBaseService;
    private final DataElementVocabularyService dataElementVocabularyService;

    /**
     * 查询数据元
     * @param searchValue 关键字
     * @return
     */
    @RespAdvice
    @GetMapping("/getList")
    public Page<DataElementBase> getList( @RequestParam int currentPage,
                                          @RequestParam int pageSize,
                                          @NotEmpty(message = "关键字不能为空") @RequestParam("searchValue")String searchValue){
        // 创建分页对象
        Page<DataElementBase> page = new Page<>(currentPage, pageSize);
        dataElementBaseService.lambdaQuery().eq(DataElementBase::getDataElementStatus, DataElementStatusEnum.PUBLISHED.getCode()).like(DataElementBase::getDataElementEnName, searchValue).or().like(DataElementBase::getDataElementDefinition, searchValue).
                select(DataElementBase::getDataElementId, DataElementBase::getDataElementEnName, DataElementBase::getDataElementDefinition,DataElementBase::getModifyDate).page(page);
        return page;
    }

    /**
     * 热点推荐
     * @return
     */
    @RespAdvice
    @GetMapping("/hotspot")
    public List<DataElementBase> hotspot(){
        List<DataElementBase> list = dataElementBaseService.lambdaQuery().eq(DataElementBase::getDataElementStatus, DataElementStatusEnum.PUBLISHED.getCode()).orderByDesc(DataElementBase::getModifyDate).last("limit 45").
                select(DataElementBase::getDataElementId, DataElementBase::getDataElementEnName, DataElementBase::getDataElementDefinition).list();
        return list;
    }
    /**
     * 相关推荐接口
     * 根据提供的关键字，查询并返回与之相关的一系列数据元素
     * 主要用于提供数据元素的相关推荐功能
     * @param searchValue 关键字，用于查询数据元素
     * @return 与关键字相关的数据元素列表
     */
    @RespAdvice
    @GetMapping("/related")
    public List<DataElementBase> related(@NotEmpty(message = "关键字不能为空") @RequestParam("searchValue")String searchValue){
        // 根据关键字查询数据元素词汇
        List<DataElementVocabulary> dataElementVocabularies = dataElementVocabularyService.lambdaQuery().eq(DataElementVocabulary::getDataElementName, searchValue).select(DataElementVocabulary::getVocabulary).list();
        List<DataElementBase> list = new ArrayList<>();
        // 如果查询结果不为空，则进一步查询相关的数据元素
        if (CollectionUtils.isNotEmpty(dataElementVocabularies)){
            // 提取查询结果中的词汇列表
            List<String> collect = dataElementVocabularies.stream().map(DataElementVocabulary::getVocabulary).collect(Collectors.toList());
            // 根据词汇列表查询数据元素，仅查询已发布状态的数据元素
            list = dataElementBaseService.lambdaQuery().eq(DataElementBase::getDataElementStatus, DataElementStatusEnum.PUBLISHED.getCode()).in(DataElementBase::getDataElementEnName,collect).
                    select(DataElementBase::getDataElementId, DataElementBase::getDataElementEnName, DataElementBase::getDataElementDefinition).list();
        }
        // 返回查询到的数据元素列表
        return list;
    }



}

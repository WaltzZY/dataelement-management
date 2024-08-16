package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共接口
 */
@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
    private final DataElementBelongCategoryService dataElementBelongCategoryService;

    /**
     * 查询分类
     * @return
     */
    @RespAdvice
    @GetMapping("/getCategoryList")
    public List<ClassIfiCationMethodVO> getCategoryList(){
        return dataElementBelongCategoryService.getCategorylist();
    }
}

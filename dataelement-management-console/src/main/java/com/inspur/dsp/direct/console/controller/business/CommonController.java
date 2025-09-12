package com.inspur.dsp.direct.console.controller.business;


import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.GetCollectionDeptTreeDto;
import com.inspur.dsp.direct.entity.dto.GetDeptSearchDto;
import com.inspur.dsp.direct.entity.vo.CollectionDeptTreeVo;
import com.inspur.dsp.direct.entity.vo.GetDeptSearchVo;
import com.inspur.dsp.direct.entity.vo.GetOrganInfoVo;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    /**
     * 001-001-选择采集/协商单位-树形结构返回
     * @param dto
     * @return
     */
    @PostMapping("/getCollectionDeptTree")
    @RespAdvice
    public List<CollectionDeptTreeVo> getCollectionDeptTree(@RequestBody GetCollectionDeptTreeDto dto) {
        return commonService.getCollectionDeptTree(dto);
    }

    /**
     * 001-001-选择采集/协商单位 - 搜索功能返回
     * @param dto
     * @return
     */
    @PostMapping("/getCollectionDeptSearch")
    @RespAdvice
    public GetDeptSearchVo getCollectionDeptSearch(@RequestBody GetDeptSearchDto dto) {
        return commonService.getCollectionDeptSearch(dto);
    }

    /**
     * 获取组织架构信息
     * @param code
     * @return
     */
    @GetMapping("/getOrganInfo/{code}")
    @RespAdvice
    public GetOrganInfoVo getOrganInfo(@PathVariable("code") String code) {
        return commonService.getOrganInfo(code);
    }
}

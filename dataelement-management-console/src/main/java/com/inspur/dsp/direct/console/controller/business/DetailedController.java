package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.vo.DataElementCollectOrgVO;
import com.inspur.dsp.direct.entity.vo.DataElementDataItemVO;
import com.inspur.dsp.direct.service.DataElementCollectOrgService;
import com.inspur.dsp.direct.service.DataElementDataItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基准数据元清单
 */
@Slf4j
@RestController
@RequestMapping("/detailed")
@RequiredArgsConstructor
public class DetailedController {

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

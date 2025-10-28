package com.inspur.dsp.direct.console.controller.determine;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;
import com.inspur.dsp.direct.entity.vo.BaseDataAndOrganizationUnitVO;
import com.inspur.dsp.direct.entity.vo.ProcessMessageVO;
import com.inspur.dsp.direct.service.DataSourceFileCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasource/fileCard")
public class DataSourceFileCardController {

    @Autowired
    private DataSourceFileCardService dataSourceFileCardService;

    /**
     * 基本信息
     */
    @GetMapping("/baseMessage/{dataid}")
    @RespAdvice
    public BaseDataElement getBaseMessage(@PathVariable String dataid) {
        return dataSourceFileCardService.getBaseMessage(dataid);
    }

    /**
     * 数源单位
     */
    @GetMapping("/datasourceUnit/{dataid}")
    @RespAdvice
    public BaseDataAndOrganizationUnitVO getDataSourceUnit(@PathVariable String dataid) {
        return dataSourceFileCardService.getDataSourceUnit(dataid);
    }

    /**
     * 采集单位
     */
    @GetMapping("/collectUnit/{dataid}")
    @RespAdvice
    public List<DomainDataElement> getCollectUnit(@PathVariable String dataid) {
        return dataSourceFileCardService.getCollectUnit(dataid);
    }

    /**
     * 处理信息
     */
    @GetMapping("/processMessage/{dataid}")
    @RespAdvice
    public List<ProcessMessageVO> getProcessMessage(@PathVariable String dataid) {
        return dataSourceFileCardService.getProcessMessage(dataid);
    }

    /**
     * 共享信息
     */
    @GetMapping("/shareMessage")
    @RespAdvice
    public List<AssociatedDataItemListVO> getShareMessage(@RequestParam(value = "dataid") String dataid, @RequestParam(value = "sourceUnitCode") String sourceUnitCode) {
        return dataSourceFileCardService.getShareMessage(dataid, sourceUnitCode);
    }
}
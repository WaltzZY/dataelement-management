package com.inspur.dsp.direct.console.controller.best;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.vo.DataElementWithTaskVo;
import com.inspur.dsp.direct.service.QueryAllSituationForCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 采集方查询整体情况控制器
 * 处理组织方相关的HTTP请求，包括数据元列表查询、发起定源、协商、核定等操作
 * 不涉及具体业务逻辑实现
 *
 * @author system
 */
@RestController
@RequestMapping("/situation/collector")
public class QueryAllSituationForCollectorController {

    @Autowired
    private QueryAllSituationForCollectorService queryAllSituationForCollectorService;

    /**
     * 分页查询列表数据
     * 使用范围：020
     *
     * @param baseDataElementSearchDTO 查询条件
     * @return 基准数据元列表
     */
    @PostMapping("/getAllSituationList")
    public Page<DataElementWithTaskVo> getAllSituationList(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO) {
        return queryAllSituationForCollectorService.getAllSituationList(baseDataElementSearchDTO);
    }

//    /**
//     * 查询基准数据详情数据
//     * 使用范围：020-1、020-2、020-3、020-4、020-7、020-8、020-9、020-10
//     *
//     * @param baseDataElementSearchDTO 查询条件，包含dataId
//     * @return 基准数据元详情
//     */
//    @PostMapping("/getAllSituationDetail")
//    public BaseDataElement getAllSituationDetail(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO) {
//        return queryAllSituationForCollectorService.getAllSituationDetail(baseDataElementSearchDTO);
//    }

//    /**
//     * 查询基准数据详情数据（带记录详情）
//     * 使用范围：020-5、020-6
//     *
//     * @param baseDataElementSearchDTO 查询条件，包含dataId
//     * @return 基准数据元详情（包含记录详情）
//     */
//    @PostMapping("/getAllSituationWithRecordDetail")
//    public BaseDataElementDTO getAllSituationWithRecordDetail(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO) {
//        return queryAllSituationForCollectorService.getAllSituationWithRecordDetail(baseDataElementSearchDTO);
//    }

    /**
     * 下载数据
     *
     * @param baseDataElementSearchDTO 查询条件
     * @param response                 HTTP响应
     */
    @PostMapping("/download")
    public void download(@RequestBody BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {
        queryAllSituationForCollectorService.download(baseDataElementSearchDTO, response);
    }
}
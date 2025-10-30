package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.FailureDetailVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.entity.vo.ImportDetermineResultVo;
import com.inspur.dsp.direct.entity.vo.DetermineResultFailureDetailVo;
import com.inspur.dsp.direct.enums.TemplateTypeEnums;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据元信息相关控制器
 * 页面编号：013
 *
 * @author Claude Code
 * @since 2025-09-22
 */
@Slf4j
@RestController
@RequestMapping("/business/allDataElement")
public class AlldataelementinfoController {

    @Autowired
    private AlldataelementinfoService alldataelementinfoService;

    /**
     * 数据元列表查询
     * @param queryDto 查询条件DTO
     * @return 数据元列表（通过@RespAdvice包装为统一响应格式）
     */
    @PostMapping("/getAllDataElementPage")
    @RespAdvice
    public Page<DataElementPageInfoVo> getAllDataElementPage(@RequestBody DataElementPageQueryDto queryDto) {
        log.info("数据元列表查询开始，查询条件：{}", queryDto);
        Page<DataElementPageInfoVo> result = alldataelementinfoService.getAllDataElementPage(queryDto);
        log.info("数据元列表查询结束，返回{}条记录", result.getRecords().size());
        return result;
    }

    /**
     * 导出数据元列表
     * @param queryDto 查询条件DTO（与getAllDataElementPage使用相同的查询条件）
     * @param response HttpServletResponse对象
     */
    @PostMapping("/exportDataElementList")
    @RespAdvice
    public void exportDataElementList(@RequestBody DataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("数据元列表导出开始，查询条件：{}", queryDto);
        alldataelementinfoService.exportDataElementList(queryDto, response);
        log.info("数据元列表导出结束");
    }

    /**
     * 上传定源文件
     * @param file 上传的Excel文件
     * @return 上传处理结果
     */
    @PostMapping("/uploadconfirmunitfile")
    @RespAdvice
    public UploadConfirmResultVo uploadconfirmunitfile(@RequestParam("file") MultipartFile file) {
        log.info("上传定源文件开始，文件名：{}, 文件大小：{} bytes", file.getOriginalFilename(), file.getSize());
        UploadConfirmResultVo result = alldataelementinfoService.uploadconfirmunitfile(file);
        log.info("上传定源文件结束，总记录数：{}, 成功：{}, 失败：{}",
                result.getTotal(), result.getSucessQty(), result.getFailQty());
        return result;
    }

    /**
     * 手动定源
     * 管理员手动为数据元指定数源单位
     *
     * @param confirmDto 手动定源请求DTO
     * @return 手动定源操作结果（通过@RespAdvice包装为统一响应格式）
     */
    @PostMapping("/manualConfirmUnit")
    @RespAdvice
    public void manualConfirmUnit(@RequestBody @Valid ManualConfirmUnitDto confirmDto) {
        log.info("手动定源开始，请求参数：{}", confirmDto);
        alldataelementinfoService.manualConfirmUnit(confirmDto);
        log.info("手动定源结束");
    }

    /**
     * 导入定数结果
     * @param file 上传的Excel文件
     * @return 导入处理结果
     */
    @PostMapping("/importDetermineResult")
    @RespAdvice
    public ImportDetermineResultVo importDetermineResult(@RequestParam("file") MultipartFile file) {
        log.info("导入定数结果开始，文件名：{}, 文件大小：{} bytes", file.getOriginalFilename(), file.getSize());
        ImportDetermineResultVo result = alldataelementinfoService.importDetermineResult(file);
        log.info("导入定数结果结束，总记录数：{}, 成功：{}, 失败：{}",
                result.getTotal(), result.getSuccessQty(), result.getFailQty());
        return result;
    }

    /**
     * 导出导入失败清单
     * @param failureDetails 失败详情列表
     * @param response HttpServletResponse对象
     */
    /*
    @PostMapping("/exportImportFailureList")
    public void exportImportFailureList(@RequestBody List<FailureDetailVo> failureDetails, HttpServletResponse response) {
        log.info("导出导入失败清单开始，失败记录数：{}", failureDetails.size());
        try {
            alldataelementinfoService.exportImportFailureList(failureDetails, response);
            log.info("导出导入失败清单结束");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("没有失败记录可导出", e);
        } catch (Exception e) {
            throw new RuntimeException("导出导入失败清单失败", e);
        }
    }
     */


    /**
     * 导出定数结果导入失败清单
     * @param failureDetails 定数结果失败详情列表
     * @param response HttpServletResponse对象
     */
    /*
    @PostMapping("/exportDetermineResultFailureList")
    public void exportDetermineResultFailureList(@RequestBody List<DetermineResultFailureDetailVo> failureDetails, HttpServletResponse response) {
        log.info("导出定数结果导入失败清单开始，失败记录数：{}", failureDetails.size());
        try {
            alldataelementinfoService.exportDetermineResultFailureList(failureDetails, response);
            log.info("导出定数结果导入失败清单结束");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("没有失败记录可导出", e);
        } catch (Exception e) {
            throw new RuntimeException("导出定数结果导入失败清单失败", e);
        }
    }
     */


    /**
     * 下载导入定数结果模板
     * @param response HttpServletResponse对象
     */
    @GetMapping("/downloadImportDetermineResultTemplate")
    public void downloadImportDetermineResultTemplate(HttpServletResponse response) {
        log.info("下载导入定数结果模板开始");
        alldataelementinfoService.downloadImportTemplate(TemplateTypeEnums.IMPORT_DETERMINE_RESULT, response);
        log.info("下载导入定数结果模板结束");
    }

    /**
     * 下载导入定源结果模板
     * @param response HttpServletResponse对象
     */
    @GetMapping("/downloadImportDatasourceResultTemplate")
    public void downloadImportDatasourceResultTemplate(HttpServletResponse response) {
        log.info("下载导入定源结果模板开始");
        alldataelementinfoService.downloadImportTemplate(TemplateTypeEnums.IMPORT_DATASOURCE_RESULT, response);
        log.info("下载导入定源结果模板结束");
    }
}
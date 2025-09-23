package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    public Page<DataElementPageInfoVo> getAllDataElementPage(@RequestBody DataElementPageQueryDto queryDto) {
        log.info("数据元列表查询开始，查询条件：{}", queryDto);
        Page<DataElementPageInfoVo> result = alldataelementinfoService.getAllDataElementPage(queryDto);
        log.info("数据元列表查询结束，返回{}条记录", result.getRecords().size());
        return result;
    }

    /**
     * 上传定源文件
     * @param file 上传的Excel文件
     * @return 上传处理结果
     */
    @PostMapping("/uploadconfirmunitfile")
    public UploadConfirmResultVo uploadconfirmunitfile(@RequestParam("file") MultipartFile file) {
        log.info("上传定源文件开始，文件名：{}, 文件大小：{} bytes", file.getOriginalFilename(), file.getSize());
        UploadConfirmResultVo result = alldataelementinfoService.uploadconfirmunitfile(file);
        log.info("上传定源文件结束，总记录数：{}, 成功：{}, 失败：{}",
                result.getTotalCount(), result.getSuccessCount(), result.getFailureCount());
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
    public void manualConfirmUnit(@RequestBody @Valid ManualConfirmUnitDto confirmDto) {
        log.info("手动定源开始，请求参数：{}", confirmDto);
        alldataelementinfoService.manualConfirmUnit(confirmDto);
        log.info("手动定源结束");
    }
}
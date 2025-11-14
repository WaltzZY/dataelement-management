package com.inspur.dsp.direct.console.controller.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.domain.Resp;
import com.inspur.dsp.direct.entity.dto.*;
import com.inspur.dsp.direct.entity.vo.*;
import com.inspur.dsp.direct.entity.RevisionComment;
import com.inspur.dsp.direct.service.DataElementStandardService;
import com.inspur.dsp.direct.service.FlowProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 数据元编制标准Controller
 * 页面编号：001系列(001-1至001-6)
 *
 * @author system
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/business/standard")
public class DataElementStandardController {

    @Autowired
    private DataElementStandardService dataElementStandardService;

    @Autowired
    private FlowProcessService flowProcessService;

    /**
     * 获取编制标准完整信息
     * 用于前端6个步骤页面的初始化
     */
    @GetMapping("/info")
    @RespAdvice
    public StandardCompleteInfoVo getStandardCompleteInfo(
            @RequestParam @NotBlank(message = "数据元ID不能为空") String dataid,
            @RequestParam @NotBlank(message = "数源单位统一社会信用代码不能为空") String sourceunitcode) {
        log.info("获取编制标准完整信息 - dataid: {}, sourceunitcode: {}", dataid, sourceunitcode);
        return dataElementStandardService.getStandardCompleteInfo(dataid, sourceunitcode);
    }

    /**
     * 查询可关联的目录数据项
     * 在"关联共享目录"弹窗中使用
     */
    @PostMapping("/catalog/search")
    @RespAdvice
    public SearchCatalogItemResultVo searchCatalogItems(@RequestBody @Valid SearchCatalogItemDto dto) {
        log.info("查询可关联的目录数据项 - dto: {}", dto);
        return dataElementStandardService.searchCatalogItems(dto);
    }

    /**
     * 保存目录-数据元关联
     * 弹窗中点击"确认"按钮时调用
     */
    @PostMapping("/catalog/associate")
    @RespAdvice
    public Resp<?> associateCatalog(@RequestBody @Valid AssociateCatalogDto dto) {
        log.info("保存目录-数据元关联 - dto: {}", dto);
        dataElementStandardService.associateCatalog(dto);
        return Resp.success();
    }

    /**
     * 查询目录-数据元关联列表
     * 用于页面刷新或弹窗关闭后的列表展示
     */
    @GetMapping("/catalog/list/{dataid}")
    @RespAdvice
    public List<AssociatedCatalogVo> getAssociatedCatalogs(
            @PathVariable @NotBlank(message = "数据元ID不能为空") String dataid,
            @RequestParam(required = false) String sourceOrgCode) {
        log.info("查询目录-数据元关联列表 - dataid: {}, sourceOrgCode: {}", dataid, sourceOrgCode);
        return dataElementStandardService.getAssociatedCatalogs(dataid, sourceOrgCode);
    }

    /**
     * 取消关联
     * 点击"取消关联"按钮时调用
     */
    @DeleteMapping("/catalog/{relationid}")
    @RespAdvice
    public Resp<?> cancelAssociation(@PathVariable @NotBlank(message = "关联关系ID不能为空") String relationid) {
        log.info("取消关联 - relationid: {}", relationid);
        dataElementStandardService.cancelAssociation(relationid);
        return Resp.success();
    }

    /**
     * 取消关联 (POST方法兼容)
     * 点击"取消关联"按钮时调用，兼容前端POST请求
     */
    @PostMapping("/catalog/{relationid}/cancel")
    @RespAdvice
    public Resp<?> cancelAssociationPost(@PathVariable @NotBlank(message = "关联关系ID不能为空") String relationid) {
        log.info("取消关联(POST) - relationid: {}", relationid);
        dataElementStandardService.cancelAssociation(relationid);
        return Resp.success();
    }

    /**
     * 上传标准规范文件（支持批量上传）
     * 仅接受PDF格式
     */
    @PostMapping("/file/standard")
    @RespAdvice
    public Resp<?> uploadStandardFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam @NotBlank(message = "数据元ID不能为空") String dataid) {
        log.info("上传标准规范文件 - dataid: {}, 文件数量: {}", dataid, files.length);
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                log.info("处理文件: {}", file.getOriginalFilename());
                dataElementStandardService.uploadFile(file, dataid, "standardfile");
            }
        }
        return Resp.success();
    }

    /**
     * 查询标准规范文件列表
     */
    @GetMapping("/file/standard/{dataid}")
    @RespAdvice
    public List<AttachmentFileVo> getStandardFiles(@PathVariable @NotBlank(message = "数据元ID不能为空") String dataid) {
        log.info("查询标准规范文件列表 - dataid: {}", dataid);
        return dataElementStandardService.getFileList(dataid, "standardfile");
    }

    /**
     * 删除标准规范文件
     */
    @PostMapping("/file/standard/{attachFileId}/delete")
    @RespAdvice
    public Resp<?> deleteStandardFile(@PathVariable @NotBlank(message = "附件ID不能为空") String attachFileId) {
        log.info("删除标准规范文件 - attachFileId: {}", attachFileId);
        dataElementStandardService.deleteFile(attachFileId);
        return Resp.success();
    }



    /**
     * 上传样例文件（支持批量上传）
     * 接受任意格式文件
     */
    @PostMapping("/file/example")
    @RespAdvice
    public Resp<?> uploadExampleFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam @NotBlank(message = "数据元ID不能为空") String dataid) {
        log.info("上传样例文件 - dataid: {}, 文件数量: {}", dataid, files.length);
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                log.info("处理文件: {}", file.getOriginalFilename());
                dataElementStandardService.uploadFile(file, dataid, "examplefile");
            }
        }
        return Resp.success();
    }

    /**
     * 查询样例文件列表
     */
    @GetMapping("/file/example/{dataid}")
    @RespAdvice
    public List<AttachmentFileVo> getExampleFiles(@PathVariable @NotBlank(message = "数据元ID不能为空") String dataid) {
        log.info("查询样例文件列表 - dataid: {}", dataid);
        return dataElementStandardService.getFileList(dataid, "examplefile");
    }

    /**
     * 删除样例文件
     */
    @PostMapping("/file/example/{attachFileId}/delete")
    @RespAdvice
    public Resp<?> deleteExampleFile(@PathVariable @NotBlank(message = "附件ID不能为空") String attachFileId) {
        log.info("删除样例文件 - attachFileId: {}", attachFileId);
        dataElementStandardService.deleteFile(attachFileId);
        return Resp.success();
    }

    /**
     * 保存编制信息
     * 保存编制标准的基本信息、联系人信息和属性信息，不改变状态
     */
    @PostMapping("/save")
    @RespAdvice
    public Resp<?> saveStandard(@RequestBody @Valid SaveStandardDto dto) {
        log.info("保存编制信息 - dto: {}", dto);
        dataElementStandardService.saveStandard(dto);
        return Resp.success();
    }

    /**
     * 提交审核
     * 保存所有信息并提交审核，状态流转到下一阶段
     */
    @PostMapping("/submit")
    @RespAdvice
    public Resp<SubmitResultVo> submitStandard(@RequestBody @Valid SaveStandardDto dto) {
        log.info("提交审核 - dto: {}", dto);
        SubmitResultVo result = dataElementStandardService.submitStandard(dto);
        return Resp.success(result);
    }

    /**
     * 获取编制标准完整信息
     * 通过查询条件获取待定标/待修订/已处理的数据元列表
     */
    @PostMapping("/getAllStandardListPage")
    @RespAdvice
    public Page<StandardDataElementPageInfoVo> getAllStandardListPage(@RequestBody @Valid StandardDataElementPageQueryDto queryDto) {
        log.info("获取编制标准完整信息 - queryDto: {}", queryDto);
        return dataElementStandardService.getAllStandardListPage(queryDto);
    }

    /**
     * 获取修订意见
     * 通过数据元ID获取修订意见
     */
    @GetMapping("/getRevisionCommentbydataid")
    @RespAdvice
    public RevisionComment getRevisionCommentbydataid(@RequestParam @NotBlank(message = "数据元ID不能为空") String dataid) {
        log.info("获取修订意见 - dataid: {}", dataid);
        return dataElementStandardService.getRevisionCommentbydataid(dataid);
    }

    /**
     * 提交复审
     * 保存所有信息并提交复审，状态流转到下一阶段
     */
    @PostMapping("/submitReExamination")
    @RespAdvice
    public Resp<SubmitResultVo> submitReExamination(@RequestBody @Valid SaveStandardDto dto) {
        log.info("提交复审 - dto: {}", dto);
        SubmitResultVo result = dataElementStandardService.submitReExamination(dto);
        return Resp.success(result);
    }

    /**
     * 导出待定标数据元列表
     */
    @PostMapping("/exportTodoDetermineList")
    public void exportTodoDetermineList(@RequestBody @Valid StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待定标数据元列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportTodoDetermineList(queryDto, response);
    }

    /**
     * 导出待修订数据元列表
     */
    @PostMapping("/exportTodoRevisedList")
    public void exportTodoRevisedList(@RequestBody @Valid StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待修订数据元列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportTodoRevisedList(queryDto, response);
    }

    /**
     * 导出定标阶段已处理数据元列表
     */
    @PostMapping("/exportSourcedoneStandardList")
    public void exportSourcedoneStandardList(@RequestBody @Valid StandardDataElementPageQueryDto queryDto, HttpServletResponse response) {
        log.info("导出定标阶段已处理数据元列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportSourcedoneStandardList(queryDto, response);
    }





    /**
     * 审核标准模块查询数据元列表
     * 支持数源单位（可选）、状态（可多选）、起止时间、关键词模糊匹配查询
     */
    @PostMapping("/auditDataElementList")
    @RespAdvice
    public List<AuditDataElementVo> auditDataElementList(@RequestBody @Valid AuditDataElementQueryDto queryDto) {
        log.info("审核标准模块查询数据元列表 - queryDto: {}", queryDto);
        return dataElementStandardService.auditDataElementList(queryDto);
    }

    /**
     * 审核标准
     * 支持单条审核和批量审核
     */
    @PostMapping("/appvoveStandard")
    @RespAdvice
    public String appvoveStandard(@RequestBody @Valid ApproveInfoDTO approveDTO) {
        log.info("审核标准 - approveDTO: {}", approveDTO);
        return dataElementStandardService.appvoveStandard(approveDTO);
    }

    /**
     * 导出待审核列表
     */
    @PostMapping("/exportPendingReviewList")
    public void exportPendingReviewList(@RequestBody @Valid AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待审核列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportPendingReviewList(queryDto, response);
    }

    /**
     * 导出征求意见列表
     */
    @PostMapping("/exportSolicitingOpinionsList")
    public void exportSolicitingOpinionsList(@RequestBody @Valid AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出征求意见列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportSolicitingOpinionsList(queryDto, response);
    }

    /**
     * 导出待复审列表
     */
    @PostMapping("/exportPendingReExaminationList")
    public void exportPendingReExaminationList(@RequestBody @Valid AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待复审列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportPendingReExaminationList(queryDto, response);
    }

    /**
     * 导出待发布列表
     */
    @PostMapping("/exportTodoReleasedList")
    public void exportTodoReleasedList(@RequestBody @Valid AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出待发布列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportTodoReleasedList(queryDto, response);
    }

    /**
     * 导出组织方已处理列表
     */
    @PostMapping("/exportDoneOrganizerList")
    public void exportDoneOrganizerList(@RequestBody @Valid AuditDataElementQueryDto queryDto, HttpServletResponse response) {
        log.info("导出组织方已处理列表 - queryDto: {}", queryDto);
        dataElementStandardService.exportDoneOrganizerList(queryDto, response);
    }
}
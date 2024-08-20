package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.dto.FileDownloadDTO;
import com.inspur.dsp.direct.entity.vo.ClassIfiCationMethodVO;
import com.inspur.dsp.direct.entity.vo.OrganInfoVO;
import com.inspur.dsp.direct.httpService.FileService;
import com.inspur.dsp.direct.httpService.entity.file.FileUploadResp;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.DataElementBelongCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileService fileService;
    private final CommonService commonService;

    /**
     * 查询分类
     * @return
     */
    @RespAdvice
    @GetMapping("/getCategoryList")
    public List<ClassIfiCationMethodVO> getCategoryList(){
        return dataElementBelongCategoryService.getCategorylist();
    }

    /**
     * rc文件下载
     */
    @Validated
    @GetMapping("/rcservice/doc")
    public ResponseEntity<byte[]> rcdownLoad(FileDownloadDTO fileDownloadDTO) {
        return fileService.rcdownLoad(fileDownloadDTO.getDocId(), fileDownloadDTO.getFileName());
    }

    /**
     * 上传文件
     **/
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public FileUploadResp upload(MultipartFile file) {
        return fileService.upload(file);
    }

    @GetMapping("/getOrganInfoByCode")
    @RespAdvice
    @SysLog(title = "公共方法", modelName = "获取部门详情")
    public OrganInfoVO getOrganInfoByCode(@RequestParam("organCode") String organCode) {
        return commonService.getOrganInfoByCode(organCode);
    }
}

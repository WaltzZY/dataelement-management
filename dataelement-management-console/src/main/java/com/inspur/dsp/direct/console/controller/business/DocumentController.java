package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.entity.dto.ServiceInterfaceDocumentDTO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentDetailVO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentListVO;
import com.inspur.dsp.direct.service.ServiceInterfaceBaseInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基准数据元清单
 */
@Slf4j
@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    @Resource
    private ServiceInterfaceBaseInfoService serviceInterfaceBaseInfoService;

    /**
     * 获取服务接口文档列表
     * @param serviceInterfaceDocumentDTO
     * @return
     */
    @PostMapping("/getList")
    @RespAdvice
    public List<ServiceInterfaceDocumentListVO> getList(ServiceInterfaceDocumentDTO serviceInterfaceDocumentDTO) {
        return serviceInterfaceBaseInfoService.getList(serviceInterfaceDocumentDTO);
    }

    /**
     * 获取服务接口文档详情
     * @param id
     * @return
     */
    @GetMapping("/getDetail")
    @RespAdvice
    public ServiceInterfaceDocumentDetailVO getDetail(String id) {
        return serviceInterfaceBaseInfoService.getDetail(id);
    }
}

package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceBaseInfo;
import com.inspur.dsp.direct.entity.dto.ServiceInterfaceDocumentDTO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentDetailVO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentListVO;

/**
 * 服务接口基本信息表
 */
public interface ServiceInterfaceBaseInfoService extends IService<ServiceInterfaceBaseInfo> {

    Page<ServiceInterfaceDocumentListVO> getList(ServiceInterfaceDocumentDTO serviceInterfaceDocumentDTO);

    ServiceInterfaceDocumentDetailVO getDetail(String id);
}

package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.ServiceInterfaceBaseInfoDao;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceBaseInfo;
import com.inspur.dsp.direct.entity.dto.ServiceInterfaceDocumentDTO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentDetailVO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentListVO;
import com.inspur.dsp.direct.service.ServiceInterfaceBaseInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务接口基本信息表
 */
@Service
public class ServiceInterfaceBaseInfoServiceImpl extends ServiceImpl<ServiceInterfaceBaseInfoDao, ServiceInterfaceBaseInfo> implements ServiceInterfaceBaseInfoService {

    @Resource
    private ServiceInterfaceBaseInfoDao serviceInterfaceBaseInfoDao;

    @Override
    public List<ServiceInterfaceDocumentListVO> getList(ServiceInterfaceDocumentDTO serviceInterfaceDocumentDTO) {
        Page page = new Page(serviceInterfaceDocumentDTO.getPageNum(), serviceInterfaceDocumentDTO.getPageSize());
        List<ServiceInterfaceDocumentListVO> dataList = serviceInterfaceBaseInfoDao.getList(page, serviceInterfaceDocumentDTO);
        return dataList;
    }

    @Override
    public ServiceInterfaceDocumentDetailVO getDetail(String id) {
        ServiceInterfaceDocumentDetailVO serviceInterfaceDocumentDetailVO = serviceInterfaceBaseInfoDao.getDetail(id);
        return serviceInterfaceDocumentDetailVO;
    }
}

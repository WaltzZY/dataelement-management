package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.ServiceInterfaceBaseInfoDao;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceBaseInfo;
import com.inspur.dsp.direct.service.ServiceInterfaceBaseInfoService;
import org.springframework.stereotype.Service;

/**
 * 服务接口基本信息表
 */
@Service
public class ServiceInterfaceBaseInfoServiceImpl extends ServiceImpl<ServiceInterfaceBaseInfoDao, ServiceInterfaceBaseInfo> implements ServiceInterfaceBaseInfoService {
}

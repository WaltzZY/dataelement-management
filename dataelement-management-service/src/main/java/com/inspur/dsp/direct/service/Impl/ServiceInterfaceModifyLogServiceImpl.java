package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.ServiceInterfaceModifyLogDao;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceModifyLog;
import com.inspur.dsp.direct.service.ServiceInterfaceModifyLogService;
import org.springframework.stereotype.Service;
/**
 * 服务接口修改记录信息表
 */
@Service
public class ServiceInterfaceModifyLogServiceImpl extends ServiceImpl<ServiceInterfaceModifyLogDao, ServiceInterfaceModifyLog> implements ServiceInterfaceModifyLogService {
}


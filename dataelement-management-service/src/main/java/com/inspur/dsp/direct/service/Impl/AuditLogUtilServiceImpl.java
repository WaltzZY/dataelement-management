package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.dao.user.UserOperationLogDao;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import com.inspur.dsp.direct.service.AuditLogUtilService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("auditLogUtilService")
public class AuditLogUtilServiceImpl implements AuditLogUtilService {

    @Resource
    private UserOperationLogDao userOperationLogDao;

    @Override
    public int addUserOperationLog(UserOperationLog userOperationLog) {
        return userOperationLogDao.insert(userOperationLog);
    }
}

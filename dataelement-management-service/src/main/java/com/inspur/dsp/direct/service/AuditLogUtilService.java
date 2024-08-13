package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.dbentity.user.UserOperationLog;

public interface AuditLogUtilService {
    int addUserOperationLog(UserOperationLog userOperationLog);
}

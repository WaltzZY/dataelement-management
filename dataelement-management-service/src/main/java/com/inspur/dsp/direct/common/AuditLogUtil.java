package com.inspur.dsp.direct.common;

import com.inspur.dsp.common.web.controller.BaseController;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import com.inspur.dsp.direct.service.AuditLogUtilService;
import com.inspur.dsp.direct.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

@Service
@Slf4j
public class AuditLogUtil extends BaseController {

    @Resource
    private AuditLogUtilService auditLogUtilService;

    /**
     * 操作日志
     * @param request
     * @param operationType:操作类型
     * @param pageTitle：访问页面名称
     * @param resultStatus：操作结果状态
     * @param exceptionInfo：异常信息
     * @param objData：操作目标数据
     * @return
     */
    public synchronized int addUserOperationLog(HttpServletRequest request, String operationType, String pageTitle, String resultStatus, String exceptionInfo, String objData) {
        try {
            UserOperationLog auditlog = new UserOperationLog();
            Map<String,Object> user = super.getCurrentUser();
            if (user!= null) {
                auditlog.setUserId(String.valueOf(user.get("ID")));
                auditlog.setUserName(String.valueOf(user.get("NAME")));
            }
            auditlog.setLogId(getUUID());
            auditlog.setOperationType(operationType);
            auditlog.setClientIp(SystemUtils.getIpAddr(request));
            auditlog.setClientBrowser(SystemUtils.getRequestBrowserInfo(request));
            auditlog.setClientSystem(SystemUtils.getRequestSystemInfo(request));
            auditlog.setPageUrl(request.getRequestURI());
            auditlog.setPageTitle(pageTitle);
            auditlog.setObjData(objData);
            auditlog.setResultStatus(resultStatus);
            auditlog.setExceptionInfo(exceptionInfo);
            auditlog.setCreateTime(Calendar.getInstance().getTime());
            // 添加日志
            return auditLogUtilService.addUserOperationLog(auditlog);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}

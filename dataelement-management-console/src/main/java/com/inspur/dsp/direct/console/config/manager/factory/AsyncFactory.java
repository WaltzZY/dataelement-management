package com.inspur.dsp.direct.console.config.manager.factory;

import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import com.inspur.dsp.direct.service.AuditLogUtilService;
import com.inspur.dsp.direct.util.SpringUtils;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory
{

    /**
     * 操作日志记录
     *
     * @param auditlog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final UserOperationLog auditlog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                SpringUtils.getBean(AuditLogUtilService.class).addUserOperationLog(auditlog);
            }
        };
    }
}


package com.inspur.dsp.direct.console.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import com.inspur.dsp.direct.enums.HttpMethod;
import com.inspur.dsp.direct.console.config.manager.AsyncManager;
import com.inspur.dsp.direct.console.config.manager.factory.AsyncFactory;
import com.inspur.dsp.direct.util.ServletUtils;
import com.inspur.dsp.direct.util.SystemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.inspur.dsp.direct.annotation.SysLog)")
    public void cutPoint() {
    }

    @Before(value = "cutPoint() && @annotation(sysLog)")
    public void doBefore(JoinPoint joinPoint, SysLog sysLog) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) Optional.ofNullable(RequestContextHolder.getRequestAttributes()).orElseThrow(() -> new RuntimeException("LogAspect -> doBefore() -> RequestContextHolder.getRequestAttributes()为空"));
        HttpServletRequest request = requestAttributes.getRequest();
        String requestURI = request.getRequestURI();
        log.debug("请求路径 - {}", requestURI);
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, SysLog controllerLog, Object jsonResult)
    {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SysLog controllerLog, Exception e)
    {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, SysLog controllerLog, final Exception e, Object jsonResult)
    {
        try
        {
            // *========数据库日志=========*//
            UserOperationLog auditlog = new UserOperationLog();
            auditlog.setLogId(UUID.randomUUID().toString().replaceAll("-", ""));
            // 请求的地址
            HttpServletRequest request = ServletUtils.getRequest();
            String ip = SystemUtils.getIpAddr(request);
            auditlog.setClientIp(ip);
            auditlog.setOperationType(controllerLog.modelName());
            auditlog.setPageUrl(ServletUtils.getRequest().getRequestURI());
            auditlog.setPageTitle(controllerLog.title());
            JSONObject user = (JSONObject)JSONObject.toJSON(ServletUtils.getRequest().getSession().getAttribute("_Dsp_Session_Token_"));
            auditlog.setClientBrowser(SystemUtils.getRequestBrowserInfo(request));
            auditlog.setClientSystem(SystemUtils.getRequestSystemInfo(request));
            auditlog.setCreateTime(Calendar.getInstance().getTime());
            if (user!= null) {
                auditlog.setUserId(String.valueOf(user.get("ID")));
                auditlog.setUserName(String.valueOf(user.get("NAME")));
            }
            if (e != null)
            {
                auditlog.setResultStatus("FAIL");
                auditlog.setExceptionInfo(StringUtils.substring(e.getMessage(), 0, 2000));
            }else {
                auditlog.setResultStatus("success");
            }
            // 设置方法名称
            String method = ServletUtils.getRequest().getMethod();
            if (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method))
            {
                String params = argsArrayToString(joinPoint.getArgs());
                auditlog.setObjData(StringUtils.substring(params, 0, 2000));
            }
            else
            {
                Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                auditlog.setObjData(StringUtils.substring(paramsMap.toString(), 0, 2000));
            }
            // 保存数据库
            AsyncManager.getMe().execute(AsyncFactory.recordOper(auditlog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (Objects.nonNull(o) && !isFilterObject(o))
                {
                    Object jsonObj = JSON.toJSON(o);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            Class<?> componentType = clazz.getComponentType();
            if (Objects.nonNull(componentType)) {
                return componentType.isAssignableFrom(MultipartFile.class);
            }
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}

package com.inspur.dsp.direct.console.autotest.scanner;

import com.inspur.dsp.direct.console.autotest.model.ApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * API接口扫描器
 * 扫描所有Controller，提取API接口信息
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Slf4j
@Component
public class ApiScanner {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 扫描所有API接口
     *
     * @return API信息列表
     */
    public List<ApiInfo> scanAllApis() {
        log.info("开始扫描API接口...");

        List<ApiInfo> apiInfoList = new ArrayList<>();

        // 获取所有带@RestController注解的Bean
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object controller = entry.getValue();
            Class<?> controllerClass = controller.getClass();

            // 处理CGLIB代理类
            if (controllerClass.getName().contains("$$")) {
                controllerClass = controllerClass.getSuperclass();
            }

            // 获取Controller级别的RequestMapping
            String baseMapping = getBaseMapping(controllerClass);

            // 扫描所有public方法
            for (Method method : controllerClass.getDeclaredMethods()) {
                ApiInfo apiInfo = extractApiInfo(controllerClass, method, baseMapping);
                if (apiInfo != null) {
                    apiInfoList.add(apiInfo);
                }
            }
        }

        log.info("API接口扫描完成，共扫描到 {} 个接口", apiInfoList.size());
        return apiInfoList;
    }

    /**
     * 提取API信息
     */
    private ApiInfo extractApiInfo(Class<?> controllerClass, Method method, String baseMapping) {
        // 检查方法是否有映射注解
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        GetMapping getMapping = AnnotatedElementUtils.findMergedAnnotation(method, GetMapping.class);
        PostMapping postMapping = AnnotatedElementUtils.findMergedAnnotation(method, PostMapping.class);
        PutMapping putMapping = AnnotatedElementUtils.findMergedAnnotation(method, PutMapping.class);
        DeleteMapping deleteMapping = AnnotatedElementUtils.findMergedAnnotation(method, DeleteMapping.class);

        String[] paths = null;
        String httpMethod = "GET";

        if (requestMapping != null) {
            paths = requestMapping.value().length > 0 ? requestMapping.value() : requestMapping.path();
            RequestMethod[] methods = requestMapping.method();
            if (methods.length > 0) {
                httpMethod = methods[0].name();
            }
        } else if (getMapping != null) {
            paths = getMapping.value().length > 0 ? getMapping.value() : getMapping.path();
            httpMethod = "GET";
        } else if (postMapping != null) {
            paths = postMapping.value().length > 0 ? postMapping.value() : postMapping.path();
            httpMethod = "POST";
        } else if (putMapping != null) {
            paths = putMapping.value().length > 0 ? putMapping.value() : putMapping.path();
            httpMethod = "PUT";
        } else if (deleteMapping != null) {
            paths = deleteMapping.value().length > 0 ? deleteMapping.value() : deleteMapping.path();
            httpMethod = "DELETE";
        }

        // 如果没有映射注解，跳过
        if (paths == null || paths.length == 0) {
            return null;
        }

        // 构建完整路径
        String apiPath = buildFullPath(baseMapping, paths[0]);

        // 解析方法参数
        List<ApiInfo.ParameterInfo> parameters = extractParameters(method);

        // 获取请求参数类型（通常是第一个参数）
        String requestParamType = null;
        if (method.getParameterCount() > 0) {
            Parameter firstParam = method.getParameters()[0];
            // 过滤掉HttpServletResponse等特殊参数
            if (!isSpecialParameter(firstParam.getType())) {
                requestParamType = firstParam.getType().getName();
            }
        }

        // 获取返回值类型
        String returnType = method.getReturnType().getName();

        return ApiInfo.builder()
                .apiPath(apiPath)
                .httpMethod(httpMethod)
                .controllerClass(controllerClass.getSimpleName())
                .methodName(method.getName())
                .requestParamType(requestParamType)
                .returnType(returnType)
                .parameters(parameters)
                .description(generateDescription(controllerClass, method))
                .hasTestCases(false)
                .testCaseCount(0)
                .build();
    }

    /**
     * 获取Controller基础路径
     */
    private String getBaseMapping(Class<?> controllerClass) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
        if (requestMapping != null) {
            String[] paths = requestMapping.value().length > 0 ? requestMapping.value() : requestMapping.path();
            if (paths.length > 0) {
                return paths[0];
            }
        }
        return "";
    }

    /**
     * 构建完整路径
     */
    private String buildFullPath(String baseMapping, String methodMapping) {
        if (baseMapping == null) {
            baseMapping = "";
        }
        if (methodMapping == null) {
            methodMapping = "";
        }

        // 标准化路径
        if (!baseMapping.startsWith("/")) {
            baseMapping = "/" + baseMapping;
        }
        if (!methodMapping.startsWith("/") && !baseMapping.endsWith("/")) {
            methodMapping = "/" + methodMapping;
        }

        String fullPath = baseMapping + methodMapping;
        // 去除重复的斜杠
        fullPath = fullPath.replaceAll("/+", "/");

        return fullPath;
    }

    /**
     * 提取方法参数信息
     */
    private List<ApiInfo.ParameterInfo> extractParameters(Method method) {
        List<ApiInfo.ParameterInfo> parameterInfos = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            // 跳过特殊参数
            if (isSpecialParameter(parameter.getType())) {
                continue;
            }

            String annotation = "";
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                annotation = "@RequestBody";
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                annotation = "@RequestParam";
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                annotation = "@PathVariable";
            }

            parameterInfos.add(ApiInfo.ParameterInfo.builder()
                    .name(parameter.getName())
                    .type(parameter.getType().getSimpleName())
                    .required(parameter.isAnnotationPresent(RequestBody.class))
                    .annotation(annotation)
                    .build());
        }

        return parameterInfos;
    }

    /**
     * 判断是否为特殊参数（如HttpServletResponse）
     */
    private boolean isSpecialParameter(Class<?> paramType) {
        String typeName = paramType.getName();
        return typeName.startsWith("javax.servlet") ||
                typeName.startsWith("jakarta.servlet") ||
                typeName.startsWith("org.springframework.web.context") ||
                typeName.startsWith("org.springframework.ui");
    }

    /**
     * 生成API描述
     */
    private String generateDescription(Class<?> controllerClass, Method method) {
        return controllerClass.getSimpleName() + "." + method.getName();
    }

    /**
     * 根据Controller类名筛选API
     */
    public List<ApiInfo> scanApisByController(String controllerName) {
        return scanAllApis().stream()
                .filter(api -> api.getControllerClass().equals(controllerName))
                .collect(Collectors.toList());
    }

    /**
     * 根据HTTP方法筛选API
     */
    public List<ApiInfo> scanApisByHttpMethod(String httpMethod) {
        return scanAllApis().stream()
                .filter(api -> api.getHttpMethod().equalsIgnoreCase(httpMethod))
                .collect(Collectors.toList());
    }
}

package com.inspur.dsp.direct.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.domain.Resp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 统一返回类的处理
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class aClass) {
        return this.filter(methodParameter);
    }

    /**
     * 根据返回类型来判断是否需要进行后续操作
     *
     * @param methodParameter
     * @return true执行beforeBodyWrite()  false不执行
     */
    private boolean filter(MethodParameter methodParameter) {
        final Class<?> declaringClass = methodParameter.getDeclaringClass();
        final RespAdvice ignoreRespAdviceAnnotationClass = declaringClass.getAnnotation(RespAdvice.class);
        return !ObjectUtils.isEmpty(ignoreRespAdviceAnnotationClass)
                || !ObjectUtils.isEmpty(methodParameter.getAnnotatedElement().getAnnotation(RespAdvice.class));
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object returnValue, MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        // 获取返回值类型
        String returnClassType = returnType.getParameterType().getSimpleName();

        // 判断返回值类型
        switch (returnClassType) {
            case "void":
                return Resp.success();
            case "Resp":
                return returnValue;
            case "String":
                response.getHeaders().setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
                return Resp.success(returnValue);
            case "Boolean":
                Boolean boo1 = (Boolean) returnValue;
                if (boo1) {
                    return Resp.success();
                } else {
                    return Resp.fail();
                }
            case "boolean":
                Boolean boo2 = (Boolean) returnValue;
                if (boo2) {
                    return Resp.success();
                } else {
                    return Resp.fail();
                }
            default:
                if (Objects.isNull(returnValue)) {
                    return Resp.success();
                } else {
                    return Resp.success(returnValue);
                }
        }
    }
}

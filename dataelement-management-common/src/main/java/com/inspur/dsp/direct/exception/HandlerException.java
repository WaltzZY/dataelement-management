package com.inspur.dsp.direct.exception;

import com.inspur.dsp.direct.domain.Resp;
import com.inspur.dsp.direct.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能描述:统一异常拦截类
 * 备注:handle 兜底异常方法放最后
 *
 * @author admin
 * @date 2024年06月21日23:34:23
 */
@Slf4j
@RestControllerAdvice
public class HandlerException {

    /**
     * validator异常拦截
     **/
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Resp constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return Resp.fail(message);
    }

    /**
     * 方法参数校验
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Resp handleBindException(BindException e) {
        if (log.isErrorEnabled()) {
            log.error(DateUtils.getTime() + " - 参数错误 - " + e.toString(), e.getMessage());
        }
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .filter(Objects::nonNull)
                .map(fieldError -> "【" + fieldError.getDefaultMessage() + "】")
                .collect(Collectors.joining(","));
        return Resp.fail(message);
    }

    /**
     * 自定义异常拦截
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomException.class)
    public Resp handleCustomException(CustomException e) {
        if (log.isErrorEnabled()) {
            log.error("自定义异常:{}", e.getMessage());
        }
        return Resp.fail(e.getMessage());
    }

    /**
     * RuntimeException的兜底方法
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Resp handle(RuntimeException e) {
        if (log.isErrorEnabled()) {
            log.error(DateUtils.getTime() + " - 未知错误 - " + e.toString(), e);
        }
        return Resp.fail(e.getMessage());
    }

    /**
     * 报错的兜底方法
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Throwable.class)
    public Resp handle(Throwable e) {
        if (log.isErrorEnabled()) {
            log.error(DateUtils.getTime() + " - 未知错误 - " + e.toString(), e);
        }
        return Resp.fail(e.getMessage());
    }
}

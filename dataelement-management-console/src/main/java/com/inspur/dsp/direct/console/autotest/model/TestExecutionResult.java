package com.inspur.dsp.direct.console.autotest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 测试执行结果模型
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestExecutionResult {

    /**
     * 结果ID
     */
    private String id;

    /**
     * 测试用例ID
     */
    private String testCaseId;

    /**
     * API路径
     */
    private String apiPath;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 请求参数
     */
    private Map<String, Object> requestParams;

    /**
     * HTTP响应状态码
     */
    private Integer responseStatus;

    /**
     * 响应体
     */
    private Object responseBody;

    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;

    /**
     * 是否成功（HTTP状态码200-299为成功）
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 异常堆栈
     */
    private String stackTrace;
}

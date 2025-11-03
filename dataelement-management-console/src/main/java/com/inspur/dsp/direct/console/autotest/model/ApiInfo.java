package com.inspur.dsp.direct.console.autotest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * API接口信息模型
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

    /**
     * API路径，如：/business/allDataElement/getAllDataElementPage
     */
    private String apiPath;

    /**
     * HTTP方法，如：GET, POST, PUT, DELETE
     */
    private String httpMethod;

    /**
     * Controller类名
     */
    private String controllerClass;

    /**
     * Controller方法名
     */
    private String methodName;

    /**
     * 请求参数类型（完整类名）
     */
    private String requestParamType;

    /**
     * 返回值类型（完整类名）
     */
    private String returnType;

    /**
     * 方法参数信息
     */
    private List<ParameterInfo> parameters;

    /**
     * API描述
     */
    private String description;

    /**
     * 是否已生成测试用例
     */
    private Boolean hasTestCases;

    /**
     * 测试用例数量
     */
    private Integer testCaseCount;

    /**
     * 参数信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParameterInfo {
        /**
         * 参数名
         */
        private String name;

        /**
         * 参数类型
         */
        private String type;

        /**
         * 是否必填
         */
        private Boolean required;

        /**
         * 参数注解（如@RequestBody, @RequestParam等）
         */
        private String annotation;
    }
}

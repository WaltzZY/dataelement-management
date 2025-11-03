package com.inspur.dsp.direct.console.autotest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 测试用例集合（用于JSON文件存储）
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseCollection {

    /**
     * API路径
     */
    private String apiPath;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * Controller类名
     */
    private String controllerClass;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 测试用例列表
     */
    private List<TestCase> testCases;

    /**
     * 总用例数
     */
    private Integer totalCount;
}

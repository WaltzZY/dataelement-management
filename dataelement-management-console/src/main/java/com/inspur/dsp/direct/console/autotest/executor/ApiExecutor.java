package com.inspur.dsp.direct.console.autotest.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.dsp.direct.console.autotest.model.TestCase;
import com.inspur.dsp.direct.console.autotest.model.TestExecutionResult;
import com.inspur.dsp.direct.console.autotest.storage.JsonStorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * API接口执行器
 * 执行测试用例并记录结果
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Slf4j
@Component
public class ApiExecutor {

    @Autowired
    private JsonStorageManager storageManager;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    // 本地服务器地址（从配置文件读取）
    private static final String BASE_URL = "http://localhost:10216/baseDataelementManage";

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(BASE_URL)
                .build();
    }

    /**
     * 执行单个测试用例
     *
     * @param testCase 测试用例
     * @return 执行结果
     */
    public TestExecutionResult executeTestCase(TestCase testCase) {
        log.info("开始执行测试用例: {} - {}", testCase.getId(), testCase.getDescription());

        long startTime = System.currentTimeMillis();
        TestExecutionResult result = TestExecutionResult.builder()
                .id(UUID.randomUUID().toString())
                .testCaseId(testCase.getId())
                .apiPath(testCase.getApiPath())
                .httpMethod(testCase.getHttpMethod())
                .executeTime(LocalDateTime.now())
                .requestParams(testCase.getRequestParams())
                .build();

        try {
            // 构建请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity;
            ResponseEntity<Object> response;

            // 根据HTTP方法执行请求
            switch (testCase.getHttpMethod().toUpperCase()) {
                case "GET":
                    response = restTemplate.exchange(
                            testCase.getApiPath(),
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            Object.class
                    );
                    break;

                case "POST":
                    requestEntity = new HttpEntity<>(testCase.getRequestParams(), headers);
                    response = restTemplate.exchange(
                            testCase.getApiPath(),
                            HttpMethod.POST,
                            requestEntity,
                            Object.class
                    );
                    break;

                case "PUT":
                    requestEntity = new HttpEntity<>(testCase.getRequestParams(), headers);
                    response = restTemplate.exchange(
                            testCase.getApiPath(),
                            HttpMethod.PUT,
                            requestEntity,
                            Object.class
                    );
                    break;

                case "DELETE":
                    response = restTemplate.exchange(
                            testCase.getApiPath(),
                            HttpMethod.DELETE,
                            new HttpEntity<>(headers),
                            Object.class
                    );
                    break;

                default:
                    throw new IllegalArgumentException("不支持的HTTP方法: " + testCase.getHttpMethod());
            }

            // 计算响应时间
            long responseTime = System.currentTimeMillis() - startTime;

            // 记录响应
            result.setResponseStatus(response.getStatusCodeValue());
            result.setResponseBody(response.getBody());
            result.setResponseTime(responseTime);
            result.setSuccess(response.getStatusCode().is2xxSuccessful());

            log.info("测试用例执行成功: {} - 响应时间: {}ms, 状态码: {}",
                    testCase.getId(), responseTime, response.getStatusCodeValue());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // HTTP错误
            long responseTime = System.currentTimeMillis() - startTime;

            result.setResponseStatus(e.getRawStatusCode());
            result.setResponseBody(e.getResponseBodyAsString());
            result.setResponseTime(responseTime);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStackTrace(getStackTraceAsString(e));

            log.error("测试用例执行失败(HTTP错误): {} - 状态码: {}, 错误: {}",
                    testCase.getId(), e.getRawStatusCode(), e.getMessage());

        } catch (Exception e) {
            // 其他异常
            long responseTime = System.currentTimeMillis() - startTime;

            result.setResponseStatus(0);
            result.setResponseTime(responseTime);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStackTrace(getStackTraceAsString(e));

            log.error("测试用例执行失败(异常): {} - 错误: {}", testCase.getId(), e.getMessage(), e);
        }

        // 保存执行结果
        storageManager.saveTestExecutionResult(result);

        return result;
    }

    /**
     * 批量执行测试用例
     *
     * @param testCases 测试用例列表
     * @return 执行结果列表
     */
    public List<TestExecutionResult> batchExecuteTestCases(List<TestCase> testCases) {
        log.info("开始批量执行测试用例，共 {} 个", testCases.size());

        List<TestExecutionResult> results = new ArrayList<>();

        for (TestCase testCase : testCases) {
            if (testCase.getEnabled() != null && !testCase.getEnabled()) {
                log.info("跳过已禁用的测试用例: {}", testCase.getId());
                continue;
            }

            try {
                TestExecutionResult result = executeTestCase(testCase);
                results.add(result);

                // 避免请求过快，适当延迟
                Thread.sleep(100);

            } catch (Exception e) {
                log.error("执行测试用例时发生异常: {}", testCase.getId(), e);
            }
        }

        log.info("批量执行测试用例完成，成功执行 {} 个", results.size());

        return results;
    }

    /**
     * 执行指定API的所有测试用例
     *
     * @param apiPath API路径
     * @return 执行结果列表
     */
    public List<TestExecutionResult> executeTestCasesByApi(String apiPath) {
        log.info("执行API的所有测试用例: {}", apiPath);

        // 加载测试用例
        var collection = storageManager.loadTestCaseCollection(apiPath);
        if (collection == null || collection.getTestCases() == null || collection.getTestCases().isEmpty()) {
            log.warn("API没有测试用例: {}", apiPath);
            return new ArrayList<>();
        }

        // 批量执行
        return batchExecuteTestCases(collection.getTestCases());
    }

    /**
     * 执行所有测试用例
     *
     * @return 执行结果列表
     */
    public List<TestExecutionResult> executeAllTestCases() {
        log.info("开始执行所有测试用例");

        List<TestExecutionResult> allResults = new ArrayList<>();

        // 加载所有测试用例集合
        var collections = storageManager.loadAllTestCaseCollections();

        for (var collection : collections) {
            if (collection.getTestCases() != null && !collection.getTestCases().isEmpty()) {
                List<TestExecutionResult> results = batchExecuteTestCases(collection.getTestCases());
                allResults.addAll(results);
            }
        }

        log.info("所有测试用例执行完成，共执行 {} 个", allResults.size());

        return allResults;
    }

    /**
     * 获取异常堆栈信息
     */
    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");

        StackTraceElement[] elements = e.getStackTrace();
        int maxLines = Math.min(10, elements.length); // 只保留前10行

        for (int i = 0; i < maxLines; i++) {
            sb.append("\tat ").append(elements[i].toString()).append("\n");
        }

        if (elements.length > maxLines) {
            sb.append("\t... ").append(elements.length - maxLines).append(" more");
        }

        return sb.toString();
    }

    /**
     * 设置基础URL（用于测试不同环境）
     */
    public void setBaseUrl(String baseUrl) {
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .build();
        log.info("API执行器基础URL已更新为: {}", baseUrl);
    }
}

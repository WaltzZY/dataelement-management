package com.inspur.dsp.direct.console.autotest.controller;

import com.inspur.dsp.direct.console.autotest.executor.ApiExecutor;
import com.inspur.dsp.direct.console.autotest.generator.TestDataGenerator;
import com.inspur.dsp.direct.console.autotest.model.ApiInfo;
import com.inspur.dsp.direct.console.autotest.model.TestCaseCollection;
import com.inspur.dsp.direct.console.autotest.model.TestExecutionResult;
import com.inspur.dsp.direct.console.autotest.scanner.ApiScanner;
import com.inspur.dsp.direct.console.autotest.storage.JsonStorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动化测试平台REST API控制器
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Slf4j
@RestController
@RequestMapping("/autotest")
@CrossOrigin(origins = "*") // 允许跨域，方便前端开发
public class AutoTestController {

    @Autowired
    private ApiScanner apiScanner;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private ApiExecutor apiExecutor;

    @Autowired
    private JsonStorageManager storageManager;

    /**
     * 扫描所有API接口
     * GET /autotest/apis
     */
    @GetMapping("/apis")
    public Map<String, Object> scanApis() {
        log.info("接收到API扫描请求");

        List<ApiInfo> apis = apiScanner.scanAllApis();

        // 检查每个API是否已有测试用例
        for (ApiInfo api : apis) {
            TestCaseCollection collection = storageManager.loadTestCaseCollection(api.getApiPath());
            if (collection != null) {
                api.setHasTestCases(true);
                api.setTestCaseCount(collection.getTotalCount());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "API扫描完成");
        response.put("data", apis);
        response.put("total", apis.size());

        return response;
    }

    /**
     * 为指定API生成测试用例
     * POST /autotest/testcases/generate
     */
    @PostMapping("/testcases/generate")
    public Map<String, Object> generateTestCases(@RequestBody ApiInfo apiInfo) {
        log.info("接收到测试用例生成请求: {} {}", apiInfo.getHttpMethod(), apiInfo.getApiPath());

        try {
            TestCaseCollection collection = testDataGenerator.generateTestCases(apiInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "测试用例生成成功");
            response.put("data", collection);

            return response;

        } catch (Exception e) {
            log.error("生成测试用例失败", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "生成测试用例失败: " + e.getMessage());

            return response;
        }
    }

    /**
     * 批量生成所有API的测试用例
     * POST /autotest/testcases/generate-all
     */
    @PostMapping("/testcases/generate-all")
    public Map<String, Object> generateAllTestCases() {
        log.info("接收到批量生成测试用例请求");

        try {
            List<ApiInfo> apis = apiScanner.scanAllApis();
            List<TestCaseCollection> collections = testDataGenerator.batchGenerateTestCases(apis);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量生成测试用例成功");
            response.put("data", collections);
            response.put("total", collections.size());

            return response;

        } catch (Exception e) {
            log.error("批量生成测试用例失败", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量生成测试用例失败: " + e.getMessage());

            return response;
        }
    }

    /**
     * 获取指定API的测试用例
     * GET /autotest/testcases/{apiPath}
     */
    @GetMapping("/testcases")
    public Map<String, Object> getTestCases(@RequestParam String apiPath) {
        log.info("接收到获取测试用例请求: {}", apiPath);

        TestCaseCollection collection = storageManager.loadTestCaseCollection(apiPath);

        Map<String, Object> response = new HashMap<>();
        if (collection != null) {
            response.put("success", true);
            response.put("message", "获取测试用例成功");
            response.put("data", collection);
        } else {
            response.put("success", false);
            response.put("message", "该API暂无测试用例");
            response.put("data", null);
        }

        return response;
    }

    /**
     * 获取所有测试用例
     * GET /autotest/testcases/all
     */
    @GetMapping("/testcases/all")
    public Map<String, Object> getAllTestCases() {
        log.info("接收到获取所有测试用例请求");

        List<TestCaseCollection> collections = storageManager.loadAllTestCaseCollections();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "获取所有测试用例成功");
        response.put("data", collections);
        response.put("total", collections.size());

        return response;
    }

    /**
     * 执行指定API的测试用例
     * POST /autotest/execute
     */
    @PostMapping("/execute")
    public Map<String, Object> executeTestCases(@RequestParam String apiPath) {
        log.info("接收到执行测试用例请求: {}", apiPath);

        try {
            List<TestExecutionResult> results = apiExecutor.executeTestCasesByApi(apiPath);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "测试用例执行完成");
            response.put("data", results);
            response.put("total", results.size());

            return response;

        } catch (Exception e) {
            log.error("执行测试用例失败", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "执行测试用例失败: " + e.getMessage());

            return response;
        }
    }

    /**
     * 执行所有测试用例
     * POST /autotest/execute-all
     */
    @PostMapping("/execute-all")
    public Map<String, Object> executeAllTestCases() {
        log.info("接收到执行所有测试用例请求");

        try {
            List<TestExecutionResult> results = apiExecutor.executeAllTestCases();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "所有测试用例执行完成");
            response.put("data", results);
            response.put("total", results.size());

            return response;

        } catch (Exception e) {
            log.error("执行所有测试用例失败", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "执行所有测试用例失败: " + e.getMessage());

            return response;
        }
    }

    /**
     * 获取测试执行结果
     * GET /autotest/results
     */
    @GetMapping("/results")
    public Map<String, Object> getTestResults(@RequestParam(required = false) String date) {
        log.info("接收到获取测试结果请求, 日期: {}", date);

        List<TestExecutionResult> results;

        if (date != null && !date.isEmpty()) {
            LocalDate localDate = LocalDate.parse(date);
            results = storageManager.loadTestExecutionResults(localDate);
        } else {
            results = storageManager.loadAllTestExecutionResults();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "获取测试结果成功");
        response.put("data", results);
        response.put("total", results.size());

        return response;
    }

    /**
     * 获取统计信息
     * GET /autotest/statistics
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        log.info("接收到获取统计信息请求");

        Map<String, Object> testCaseStats = storageManager.getTestCaseStatistics();
        Map<String, Object> executionStats = storageManager.getExecutionStatistics();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "获取统计信息成功");
        response.put("testCaseStatistics", testCaseStats);
        response.put("executionStatistics", executionStats);

        return response;
    }

    /**
     * 删除指定API的测试用例
     * DELETE /autotest/testcases
     */
    @DeleteMapping("/testcases")
    public Map<String, Object> deleteTestCases(@RequestParam String apiPath) {
        log.info("接收到删除测试用例请求: {}", apiPath);

        boolean deleted = storageManager.deleteTestCaseCollection(apiPath);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("message", deleted ? "删除成功" : "删除失败或测试用例不存在");

        return response;
    }

    /**
     * 健康检查
     * GET /autotest/health
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "自动化测试平台运行正常");
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }
}

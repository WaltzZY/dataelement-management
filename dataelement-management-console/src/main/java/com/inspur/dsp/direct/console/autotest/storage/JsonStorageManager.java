package com.inspur.dsp.direct.console.autotest.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inspur.dsp.direct.console.autotest.model.TestCase;
import com.inspur.dsp.direct.console.autotest.model.TestCaseCollection;
import com.inspur.dsp.direct.console.autotest.model.TestExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON文件存储管理器
 * 负责测试用例和执行结果的存储与读取
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Slf4j
@Component
public class JsonStorageManager {

    private static final String BASE_DIR = "autotest-data";
    private static final String TESTCASES_DIR = BASE_DIR + "/testcases";
    private static final String RESULTS_DIR = BASE_DIR + "/results";

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        // 初始化ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 确保目录存在
        createDirectoriesIfNotExists();
    }

    /**
     * 创建必要的目录
     */
    private void createDirectoriesIfNotExists() {
        try {
            Files.createDirectories(Paths.get(TESTCASES_DIR));
            Files.createDirectories(Paths.get(RESULTS_DIR));
            log.info("存储目录初始化完成: {}", BASE_DIR);
        } catch (IOException e) {
            log.error("创建存储目录失败", e);
        }
    }

    /**
     * 保存测试用例集合
     *
     * @param collection 测试用例集合
     */
    public void saveTestCaseCollection(TestCaseCollection collection) {
        String fileName = generateTestCaseFileName(collection.getApiPath());
        File file = new File(TESTCASES_DIR, fileName);

        try {
            objectMapper.writeValue(file, collection);
            log.info("测试用例保存成功: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("保存测试用例失败: {}", fileName, e);
            throw new RuntimeException("保存测试用例失败", e);
        }
    }

    /**
     * 读取测试用例集合
     *
     * @param apiPath API路径
     * @return 测试用例集合，不存在则返回null
     */
    public TestCaseCollection loadTestCaseCollection(String apiPath) {
        String fileName = generateTestCaseFileName(apiPath);
        File file = new File(TESTCASES_DIR, fileName);

        if (!file.exists()) {
            return null;
        }

        try {
            return objectMapper.readValue(file, TestCaseCollection.class);
        } catch (IOException e) {
            log.error("读取测试用例失败: {}", fileName, e);
            return null;
        }
    }

    /**
     * 读取所有测试用例集合
     *
     * @return 所有测试用例集合
     */
    public List<TestCaseCollection> loadAllTestCaseCollections() {
        List<TestCaseCollection> collections = new ArrayList<>();
        File dir = new File(TESTCASES_DIR);

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    TestCaseCollection collection = objectMapper.readValue(file, TestCaseCollection.class);
                    collections.add(collection);
                } catch (IOException e) {
                    log.error("读取测试用例文件失败: {}", file.getName(), e);
                }
            }
        }

        return collections;
    }

    /**
     * 保存测试执行结果
     *
     * @param result 执行结果
     */
    public void saveTestExecutionResult(TestExecutionResult result) {
        String fileName = generateResultFileName(result.getExecuteTime().toLocalDate());
        File file = new File(RESULTS_DIR, fileName);

        List<TestExecutionResult> results = new ArrayList<>();

        // 如果文件已存在，先读取现有数据
        if (file.exists()) {
            try {
                TestExecutionResult[] existingResults = objectMapper.readValue(file, TestExecutionResult[].class);
                results.addAll(Arrays.asList(existingResults));
            } catch (IOException e) {
                log.error("读取现有执行结果失败: {}", fileName, e);
            }
        }

        // 添加新结果
        results.add(result);

        // 保存
        try {
            objectMapper.writeValue(file, results);
            log.info("执行结果保存成功: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("保存执行结果失败: {}", fileName, e);
            throw new RuntimeException("保存执行结果失败", e);
        }
    }

    /**
     * 批量保存测试执行结果
     *
     * @param results 执行结果列表
     */
    public void saveTestExecutionResults(List<TestExecutionResult> results) {
        if (results == null || results.isEmpty()) {
            return;
        }

        // 按日期分组
        Map<LocalDate, List<TestExecutionResult>> resultsByDate = results.stream()
                .collect(Collectors.groupingBy(r -> r.getExecuteTime().toLocalDate()));

        // 分别保存每天的结果
        for (Map.Entry<LocalDate, List<TestExecutionResult>> entry : resultsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<TestExecutionResult> dayResults = entry.getValue();

            String fileName = generateResultFileName(date);
            File file = new File(RESULTS_DIR, fileName);

            List<TestExecutionResult> allResults = new ArrayList<>();

            // 读取现有数据
            if (file.exists()) {
                try {
                    TestExecutionResult[] existingResults = objectMapper.readValue(file, TestExecutionResult[].class);
                    allResults.addAll(Arrays.asList(existingResults));
                } catch (IOException e) {
                    log.error("读取现有执行结果失败: {}", fileName, e);
                }
            }

            // 添加新结果
            allResults.addAll(dayResults);

            // 保存
            try {
                objectMapper.writeValue(file, allResults);
                log.info("执行结果批量保存成功: {} ({}条记录)", file.getAbsolutePath(), dayResults.size());
            } catch (IOException e) {
                log.error("批量保存执行结果失败: {}", fileName, e);
            }
        }
    }

    /**
     * 读取指定日期的测试执行结果
     *
     * @param date 日期
     * @return 执行结果列表
     */
    public List<TestExecutionResult> loadTestExecutionResults(LocalDate date) {
        String fileName = generateResultFileName(date);
        File file = new File(RESULTS_DIR, fileName);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            TestExecutionResult[] results = objectMapper.readValue(file, TestExecutionResult[].class);
            return Arrays.asList(results);
        } catch (IOException e) {
            log.error("读取执行结果失败: {}", fileName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 读取所有测试执行结果
     *
     * @return 所有执行结果
     */
    public List<TestExecutionResult> loadAllTestExecutionResults() {
        List<TestExecutionResult> allResults = new ArrayList<>();
        File dir = new File(RESULTS_DIR);

        File[] files = dir.listFiles((d, name) -> name.startsWith("results_") && name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    TestExecutionResult[] results = objectMapper.readValue(file, TestExecutionResult[].class);
                    allResults.addAll(Arrays.asList(results));
                } catch (IOException e) {
                    log.error("读取执行结果文件失败: {}", file.getName(), e);
                }
            }
        }

        return allResults;
    }

    /**
     * 删除测试用例集合
     *
     * @param apiPath API路径
     * @return 是否删除成功
     */
    public boolean deleteTestCaseCollection(String apiPath) {
        String fileName = generateTestCaseFileName(apiPath);
        File file = new File(TESTCASES_DIR, fileName);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                log.info("测试用例删除成功: {}", fileName);
            }
            return deleted;
        }

        return false;
    }

    /**
     * 生成测试用例文件名
     * 格式: testcase_{md5(apiPath)}.json
     */
    private String generateTestCaseFileName(String apiPath) {
        String sanitized = apiPath.replaceAll("[^a-zA-Z0-9]", "_");
        return "testcase_" + sanitized + ".json";
    }

    /**
     * 生成执行结果文件名
     * 格式: results_yyyyMMdd.json
     */
    private String generateResultFileName(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return "results_" + date.format(formatter) + ".json";
    }

    /**
     * 获取测试用例统计信息
     *
     * @return 统计信息Map
     */
    public Map<String, Object> getTestCaseStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<TestCaseCollection> collections = loadAllTestCaseCollections();

        int totalApis = collections.size();
        int totalTestCases = collections.stream()
                .mapToInt(c -> c.getTestCases() != null ? c.getTestCases().size() : 0)
                .sum();

        stats.put("totalApis", totalApis);
        stats.put("totalTestCases", totalTestCases);
        stats.put("avgTestCasesPerApi", totalApis > 0 ? (double) totalTestCases / totalApis : 0);

        return stats;
    }

    /**
     * 获取执行结果统计信息
     *
     * @return 统计信息Map
     */
    public Map<String, Object> getExecutionStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<TestExecutionResult> results = loadAllTestExecutionResults();

        int totalExecutions = results.size();
        long successCount = results.stream().filter(TestExecutionResult::getSuccess).count();
        long failureCount = totalExecutions - successCount;

        double avgResponseTime = results.stream()
                .mapToLong(TestExecutionResult::getResponseTime)
                .average()
                .orElse(0);

        stats.put("totalExecutions", totalExecutions);
        stats.put("successCount", successCount);
        stats.put("failureCount", failureCount);
        stats.put("successRate", totalExecutions > 0 ? (double) successCount / totalExecutions * 100 : 0);
        stats.put("avgResponseTime", avgResponseTime);

        return stats;
    }
}

package com.inspur.dsp.direct.console.autotest.generator;

import com.inspur.dsp.direct.console.autotest.model.ApiInfo;
import com.inspur.dsp.direct.console.autotest.model.TestCase;
import com.inspur.dsp.direct.console.autotest.model.TestCaseCollection;
import com.inspur.dsp.direct.console.autotest.storage.JsonStorageManager;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 测试数据生成器
 * 基于数据库现有数据生成测试用例
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Slf4j
@Component
public class TestDataGenerator {

    @Autowired
    private JsonStorageManager storageManager;

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    /**
     * 为指定API生成测试用例
     *
     * @param apiInfo API信息
     * @return 测试用例集合
     */
    public TestCaseCollection generateTestCases(ApiInfo apiInfo) {
        log.info("开始为API生成测试用例: {} {}", apiInfo.getHttpMethod(), apiInfo.getApiPath());

        List<TestCase> testCases = new ArrayList<>();

        // 根据不同的参数类型生成测试用例
        String paramType = apiInfo.getRequestParamType();

        if (paramType == null || paramType.isEmpty()) {
            // 无参数的API，生成一个空参数的测试用例
            testCases.add(createEmptyTestCase(apiInfo));
        } else if (paramType.contains("DataElementPageQueryDto")) {
            // 数据元分页查询相关的测试用例
            testCases.addAll(generateDataElementPageQueryTestCases(apiInfo));
        } else if (paramType.contains("ManualConfirmUnitDto")) {
            // 手动定源测试用例
            testCases.addAll(generateManualConfirmTestCases(apiInfo));
        } else {
            // 其他类型，生成通用测试用例
            testCases.addAll(generateGenericTestCases(apiInfo));
        }

        // 构建测试用例集合
        TestCaseCollection collection = TestCaseCollection.builder()
                .apiPath(apiInfo.getApiPath())
                .httpMethod(apiInfo.getHttpMethod())
                .controllerClass(apiInfo.getControllerClass())
                .methodName(apiInfo.getMethodName())
                .testCases(testCases)
                .totalCount(testCases.size())
                .build();

        // 保存到JSON文件
        storageManager.saveTestCaseCollection(collection);

        log.info("测试用例生成完成: {} 个用例", testCases.size());
        return collection;
    }

    /**
     * 生成空参数测试用例
     */
    private TestCase createEmptyTestCase(ApiInfo apiInfo) {
        return TestCase.builder()
                .id(UUID.randomUUID().toString())
                .apiPath(apiInfo.getApiPath())
                .httpMethod(apiInfo.getHttpMethod())
                .controllerClass(apiInfo.getControllerClass())
                .methodName(apiInfo.getMethodName())
                .description("无参数请求")
                .requestParams(new HashMap<>())
                .dataSource("自动生成")
                .createTime(LocalDateTime.now())
                .enabled(true)
                .build();
    }

    /**
     * 生成DataElementPageQueryDto相关的测试用例
     */
    private List<TestCase> generateDataElementPageQueryTestCases(ApiInfo apiInfo) {
        List<TestCase> testCases = new ArrayList<>();

        // 从数据库中采样真实数据
        List<BaseDataElement> sampleData = baseDataElementMapper.selectList(null);
        if (sampleData.isEmpty()) {
            log.warn("数据库中没有数据元数据，无法生成测试用例");
            return testCases;
        }

        // 用例1: 基础分页查询
        testCases.add(createTestCase(apiInfo, "基础分页查询", createMap(
                "pageNum", 1L,
                "pageSize", 10L
        ), "自动生成-基础用例"));

        // 用例2: 大页码查询
        testCases.add(createTestCase(apiInfo, "大页码查询", createMap(
                "pageNum", 100L,
                "pageSize", 10L
        ), "自动生成-边界值测试"));

        // 用例3: 大分页大小
        testCases.add(createTestCase(apiInfo, "大分页大小查询", createMap(
                "pageNum", 1L,
                "pageSize", 100L
        ), "自动生成-边界值测试"));

        // 用例4: 按关键词搜索（从数据库中随机选择）
        if (!sampleData.isEmpty()) {
            BaseDataElement sample = sampleData.get(new Random().nextInt(sampleData.size()));
            if (sample.getName() != null && sample.getName().length() > 2) {
                String keyword = sample.getName().substring(0, Math.min(2, sample.getName().length()));
                testCases.add(createTestCase(apiInfo, "关键词搜索: " + keyword, createMap(
                        "pageNum", 1L,
                        "pageSize", 10L,
                        "keyword", keyword
                ), "数据库采样-关键词: " + keyword));
            }
        }

        // 用例5: 按状态筛选
        Set<String> statuses = new HashSet<>();
        sampleData.forEach(data -> {
            if (data.getStatus() != null) {
                statuses.add(data.getStatus());
            }
        });

        if (!statuses.isEmpty()) {
            List<String> statusList = new ArrayList<>(statuses);
            testCases.add(createTestCase(apiInfo, "按状态筛选: " + statusList.get(0), createMap(
                    "pageNum", 1L,
                    "pageSize", 10L,
                    "statusList", Collections.singletonList(statusList.get(0))
            ), "数据库采样-状态: " + statusList.get(0)));
        }

        // 用例6: 按数源单位筛选
        Set<String> unitCodes = new HashSet<>();
        sampleData.forEach(data -> {
            if (data.getSourceUnitCode() != null) {
                unitCodes.add(data.getSourceUnitCode());
            }
        });

        if (!unitCodes.isEmpty()) {
            List<String> unitCodeList = new ArrayList<>(unitCodes);
            String unitCode = unitCodeList.get(0);
            testCases.add(createTestCase(apiInfo, "按数源单位筛选", createMap(
                    "pageNum", 1L,
                    "pageSize", 10L,
                    "collectUnitCodeList", Collections.singletonList(unitCode)
            ), "数据库采样-单位代码: " + unitCode));
        }

        // 用例7: 组合条件查询
        if (!statuses.isEmpty() && !sampleData.isEmpty()) {
            BaseDataElement sample = sampleData.get(0);
            testCases.add(createTestCase(apiInfo, "组合条件查询", createMap(
                    "pageNum", 1L,
                    "pageSize", 10L,
                    "keyword", sample.getName() != null && sample.getName().length() > 0 ?
                        sample.getName().substring(0, 1) : "",
                    "statusList", Collections.singletonList(new ArrayList<>(statuses).get(0))
            ), "数据库采样-组合条件"));
        }

        // 用例8: 排序测试 - 按采集单位数量升序
        testCases.add(createTestCase(apiInfo, "排序:采集单位数量升序", createMap(
                "pageNum", 1L,
                "pageSize", 10L,
                "sortField", "collectunitqty",
                "sortOrder", "asc"
        ), "自动生成-排序测试"));

        // 用例9: 排序测试 - 按采集单位数量降序
        testCases.add(createTestCase(apiInfo, "排序:采集单位数量降序", createMap(
                "pageNum", 1L,
                "pageSize", 10L,
                "sortField", "collectunitqty",
                "sortOrder", "desc"
        ), "自动生成-排序测试"));

        // 用例10: 排序测试 - 按状态升序
        testCases.add(createTestCase(apiInfo, "排序:状态升序", createMap(
                "pageNum", 1L,
                "pageSize", 10L,
                "sortField", "status",
                "sortOrder", "asc"
        ), "自动生成-排序测试"));

        return testCases;
    }

    /**
     * 生成手动定源测试用例
     */
    private List<TestCase> generateManualConfirmTestCases(ApiInfo apiInfo) {
        List<TestCase> testCases = new ArrayList<>();

        // 从数据库中采样数据
        List<BaseDataElement> sampleData = baseDataElementMapper.selectList(null);
        if (sampleData.isEmpty()) {
            log.warn("数据库中没有数据元数据，无法生成手动定源测试用例");
            return testCases;
        }

        // 随机选择一条数据
        BaseDataElement sample = sampleData.get(new Random().nextInt(Math.min(5, sampleData.size())));

        // 用例1: 正常手动定源
        if (sample.getDataid() != null && sample.getSourceUnitCode() != null) {
            testCases.add(createTestCase(apiInfo, "正常手动定源", createMap(
                    "dataid", sample.getDataid(),
                    "sourceUnitId", sample.getSourceUnitCode()
            ), "数据库采样-dataid: " + sample.getDataid()));
        }

        // 用例2: 不存在的数据元ID
        testCases.add(createTestCase(apiInfo, "不存在的数据元ID", createMap(
                "dataid", "non_existing_id_" + UUID.randomUUID().toString().substring(0, 8),
                "sourceUnitId", sample.getSourceUnitCode() != null ? sample.getSourceUnitCode() : "test_unit"
        ), "自动生成-异常测试"));

        return testCases;
    }

    /**
     * 生成通用测试用例
     */
    private List<TestCase> generateGenericTestCases(ApiInfo apiInfo) {
        List<TestCase> testCases = new ArrayList<>();

        // 通用用例：空参数
        testCases.add(createTestCase(apiInfo, "空参数请求", new HashMap<>(), "自动生成-通用用例"));

        // 通用用例：基础参数
        testCases.add(createTestCase(apiInfo, "基础参数请求", createMap(
                "id", 1,
                "name", "test"
        ), "自动生成-通用用例"));

        return testCases;
    }

    /**
     * 创建测试用例
     */
    private TestCase createTestCase(ApiInfo apiInfo, String description, Map<String, Object> params, String dataSource) {
        return TestCase.builder()
                .id(UUID.randomUUID().toString())
                .apiPath(apiInfo.getApiPath())
                .httpMethod(apiInfo.getHttpMethod())
                .controllerClass(apiInfo.getControllerClass())
                .methodName(apiInfo.getMethodName())
                .description(description)
                .requestParams(params)
                .dataSource(dataSource)
                .createTime(LocalDateTime.now())
                .enabled(true)
                .build();
    }

    /**
     * 便捷方法：创建Map
     */
    private Map<String, Object> createMap(Object... keyValues) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put((String) keyValues[i], keyValues[i + 1]);
        }
        return map;
    }

    /**
     * 批量生成测试用例
     *
     * @param apiInfoList API信息列表
     * @return 生成的测试用例集合列表
     */
    public List<TestCaseCollection> batchGenerateTestCases(List<ApiInfo> apiInfoList) {
        log.info("开始批量生成测试用例，共 {} 个API", apiInfoList.size());

        List<TestCaseCollection> collections = new ArrayList<>();

        for (ApiInfo apiInfo : apiInfoList) {
            try {
                TestCaseCollection collection = generateTestCases(apiInfo);
                collections.add(collection);
            } catch (Exception e) {
                log.error("生成测试用例失败: {} {}", apiInfo.getHttpMethod(), apiInfo.getApiPath(), e);
            }
        }

        log.info("批量生成测试用例完成，成功生成 {} 个API的测试用例", collections.size());
        return collections;
    }
}

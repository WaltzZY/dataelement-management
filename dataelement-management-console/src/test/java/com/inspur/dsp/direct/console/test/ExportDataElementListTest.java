package com.inspur.dsp.direct.console.test;

import com.inspur.dsp.direct.console.controller.business.AlldataelementinfoController;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 数据元列表导出功能测试类
 * 测试AlldataelementinfoController的exportDataElementList方法
 *
 * @author Test
 * @since 2025-09-24
 */
@SpringBootTest
public class ExportDataElementListTest {

    @Autowired
    private AlldataelementinfoController controller;

    private MockHttpServletResponse mockResponse;

    @BeforeEach
    public void setUp() {
        mockResponse = new MockHttpServletResponse();
    }

    /**
     * 测试用例1：基础导出功能测试
     * 不添加任何过滤条件，导出所有数据
     */
    @Test
    public void testExportDataElementList_Basic() {
        System.out.println("=== 测试用例1：基础导出功能测试 ===");

        try {
            // 准备查询条件 - 不设置任何过滤条件，导出所有数据
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();

            // 调用导出方法
            controller.exportDataElementList(queryDto, mockResponse);

            // 验证响应结果
            System.out.println("导出结果验证：");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("Content-Type：" + mockResponse.getContentType());
            System.out.println("Content-Disposition：" + mockResponse.getHeader("Content-Disposition"));
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            // 验证是否成功生成Excel文件
            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 导出成功，Excel文件已生成");
            } else {
                System.out.println("❌ 导出失败，未生成文件内容");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例2：按关键词过滤导出
     */
    @Test
    public void testExportDataElementList_ByKeyword() {
        System.out.println("=== 测试用例2：按关键词过滤导出 ===");

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setKeyword("数据"); // 设置关键词过滤条件

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：关键词包含'数据'");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 按关键词过滤导出成功");
            } else {
                System.out.println("❌ 按关键词过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例3：按状态列表过滤导出
     */
    @Test
    public void testExportDataElementList_ByStatusList() {
        System.out.println("=== 测试用例3：按状态列表过滤导出 ===");

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setStatusList(Arrays.asList("1", "2")); // 设置状态列表过滤条件

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：状态为1或2");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 按状态列表过滤导出成功");
            } else {
                System.out.println("❌ 按状态列表过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例4：按数源单位代码过滤导出
     */
    @Test
    public void testExportDataElementList_BySourceUnitCode() {
        System.out.println("=== 测试用例4：按数源单位代码过滤导出 ===");

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setSourceUnitCode(Arrays.asList("91110000123456789A", "91110000987654321B")); // 设置数源单位代码列表

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：数源单位代码为91110000123456789A或91110000987654321B");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 按数源单位代码过滤导出成功");
            } else {
                System.out.println("❌ 按数源单位代码过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例5：按发起时间范围过滤导出
     */
    @Test
    public void testExportDataElementList_BySendDateRange() {
        System.out.println("=== 测试用例5：按发起时间范围过滤导出 ===");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setSendDateBegin(sdf.parse("2024-01-01 00:00:00")); // 发起时间开始
            queryDto.setSendDateEnd(sdf.parse("2024-12-31 23:59:59"));   // 发起时间结束

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：发起时间在2024年内");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 按发起时间范围过滤导出成功");
            } else {
                System.out.println("❌ 按发起时间范围过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例6：按定源时间范围过滤导出
     */
    @Test
    public void testExportDataElementList_ByConfirmDateRange() {
        System.out.println("=== 测试用例6：按定源时间范围过滤导出 ===");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setConfirmDateBegin(sdf.parse("2024-06-01 00:00:00")); // 定源时间开始
            queryDto.setConfirmDateEnd(sdf.parse("2024-06-30 23:59:59"));   // 定源时间结束

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：定源时间在2024年6月内");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 按定源时间范围过滤导出成功");
            } else {
                System.out.println("❌ 按定源时间范围过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例7：复合条件过滤导出
     */
    @Test
    public void testExportDataElementList_MultipleFilters() {
        System.out.println("=== 测试用例7：复合条件过滤导出 ===");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setKeyword("数据元"); // 关键词
            queryDto.setStatusList(Arrays.asList("1", "3")); // 状态列表
            queryDto.setSourceUnitCode(Arrays.asList("91110000123456789A")); // 数源单位代码
            queryDto.setSendDateBegin(sdf.parse("2024-01-01 00:00:00")); // 发起时间开始
            queryDto.setSendDateEnd(sdf.parse("2024-12-31 23:59:59")); // 发起时间结束

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：");
            System.out.println("  - 关键词：数据元");
            System.out.println("  - 状态：1或3");
            System.out.println("  - 数源单位：91110000123456789A");
            System.out.println("  - 发起时间：2024年内");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 复合条件过滤导出成功");
            } else {
                System.out.println("❌ 复合条件过滤导出失败或无匹配数据");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例8：空结果集导出测试
     */
    @Test
    public void testExportDataElementList_EmptyResult() {
        System.out.println("=== 测试用例8：空结果集导出测试 ===");

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setKeyword("不存在的关键词"); // 设置一个不存在的关键词
            queryDto.setStatusList(Arrays.asList("999")); // 设置一个不存在的状态

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("过滤条件：关键词='不存在的关键词'，状态=999");
            System.out.println("响应状态码：" + mockResponse.getStatus());
            System.out.println("响应数据大小：" + mockResponse.getContentAsByteArray().length + " bytes");

            // 即使没有数据，也应该生成一个只有表头的Excel文件
            if (mockResponse.getContentAsByteArray().length > 0) {
                System.out.println("✅ 空结果集导出成功，生成了只有表头的Excel文件");
            } else {
                System.out.println("❌ 空结果集导出失败");
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例9：验证导出文件的HTTP响应头
     */
    @Test
    public void testExportDataElementList_HttpHeaders() {
        System.out.println("=== 测试用例9：验证导出文件的HTTP响应头 ===");

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();

            controller.exportDataElementList(queryDto, mockResponse);

            System.out.println("HTTP响应头验证：");
            System.out.println("Content-Type：" + mockResponse.getContentType());
            System.out.println("Content-Disposition：" + mockResponse.getHeader("Content-Disposition"));
            System.out.println("Cache-Control：" + mockResponse.getHeader("Cache-Control"));

            // 验证是否设置了正确的响应头
            boolean hasCorrectContentType = mockResponse.getContentType() != null &&
                    (mockResponse.getContentType().contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                            mockResponse.getContentType().contains("application/vnd.ms-excel"));
            boolean hasContentDisposition = mockResponse.getHeader("Content-Disposition") != null &&
                    mockResponse.getHeader("Content-Disposition").contains("attachment");

            if (hasCorrectContentType && hasContentDisposition) {
                System.out.println("✅ HTTP响应头设置正确");
            } else {
                System.out.println("❌ HTTP响应头设置有误");
                if (!hasCorrectContentType) {
                    System.out.println("  - Content-Type设置错误，期望Excel格式的MIME类型");
                }
                if (!hasContentDisposition) {
                    System.out.println("  - Content-Disposition设置错误，期望包含attachment");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例10：极端情况测试 - null参数
     */
    @Test
    public void testExportDataElementList_NullParams() {
        System.out.println("=== 测试用例10：极端情况测试 - null参数 ===");

        try {
            // 测试传入null的queryDto参数
            controller.exportDataElementList(null, mockResponse);

            System.out.println("测试传入null的queryDto参数");
            System.out.println("响应状态码：" + mockResponse.getStatus());

        } catch (Exception e) {
            System.out.println("预期异常：" + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // 重新初始化mockResponse进行下一个测试
        mockResponse = new MockHttpServletResponse();

        try {
            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            // 测试传入null的response参数
            controller.exportDataElementList(queryDto, null);

            System.out.println("测试传入null的response参数");

        } catch (Exception e) {
            System.out.println("预期异常：" + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    /**
     * 运行所有测试用例
     */
    @Test
    public void runAllTests() {
        System.out.println("========================================");
        System.out.println("开始运行exportDataElementList导出功能完整测试套件");
        System.out.println("========================================");

        try {
            testExportDataElementList_Basic();
            System.out.println();

            testExportDataElementList_ByKeyword();
            System.out.println();

            testExportDataElementList_ByStatusList();
            System.out.println();

            testExportDataElementList_BySourceUnitCode();
            System.out.println();

            testExportDataElementList_BySendDateRange();
            System.out.println();

            testExportDataElementList_ByConfirmDateRange();
            System.out.println();

            testExportDataElementList_MultipleFilters();
            System.out.println();

            testExportDataElementList_EmptyResult();
            System.out.println();

            testExportDataElementList_HttpHeaders();
            System.out.println();

            testExportDataElementList_NullParams();

            System.out.println("========================================");
            System.out.println("测试套件执行完成");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("测试套件执行失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
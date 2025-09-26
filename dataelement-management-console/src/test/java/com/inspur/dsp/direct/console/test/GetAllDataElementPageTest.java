package com.inspur.dsp.direct.console.test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.dsp.direct.console.controller.business.AlldataelementinfoController;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * 数据元信息控制器测试类
 */
@SpringBootTest
public class GetAllDataElementPageTest {

    @Autowired
    private AlldataelementinfoService alldataelementinfoService;

    @Autowired
    private AlldataelementinfoController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 测试用例1：基础分页查询
     */
    @Test
    public void testGetAllDataElementPage_Basic() {
        System.out.println("=== 测试用例1：基础分页查询 ===");

        DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
        queryDto.setPageNum(1L);
        queryDto.setPageSize(10L);

        try {
            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("查询结果：");
            System.out.println("总记录数：" + result.getTotal());
            System.out.println("当前页：" + result.getCurrent());
            System.out.println("每页大小：" + result.getSize());
            System.out.println("总页数：" + result.getPages());

            System.out.println("\n数据列表：");
            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 状态: " + vo.getStatus() +
                        ", 状态描述: " + vo.getStatusDesc() +
                        ", 数源单位: " + vo.getSourceUnitName());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例2：按状态筛选查询
     */
    @Test
    public void testGetAllDataElementPage_ByStatus() {
        System.out.println("\n=== 测试用例2：按状态筛选查询 ===");

        DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
        queryDto.setPageNum(1L);
        queryDto.setPageSize(10L);
        queryDto.setStatusList(Arrays.asList("DESIGNATED_SOURCE"));

        try {
            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("筛选状态：DESIGNATED_SOURCE");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 状态: " + vo.getStatus() +
                        ", 数源单位: " + vo.getSourceUnitName());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例3：关键字搜索
     */
    @Test
    public void testGetAllDataElementPage_ByKeyword() {
        System.out.println("\n=== 测试用例3：关键字搜索 ===");

        DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
        queryDto.setPageNum(1L);
        queryDto.setPageSize(10L);
        queryDto.setKeyword("企业");

        try {
            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("搜索关键字：企业");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 定义: " + vo.getDefinition());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例4：按数源单位筛选
     */
    @Test
    public void testGetAllDataElementPage_BySourceUnit() {
        System.out.println("\n=== 测试用例4：按数源单位筛选 ===");

        DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
        queryDto.setPageNum(1L);
        queryDto.setPageSize(10L);
        queryDto.setSourceUnitCode(Arrays.asList("91110000123456789A"));

        try {
            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("筛选数源单位：91110000123456789A");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 数源单位代码: " + vo.getSourceUnitCode() +
                        ", 数源单位名称: " + vo.getSourceUnitName());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例5：时间范围查询
     */
    @Test
    public void testGetAllDataElementPage_ByDateRange() {
        System.out.println("\n=== 测试用例5：时间范围查询 ===");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setPageNum(1L);
            queryDto.setPageSize(10L);
            queryDto.setSendDateBegin(sdf.parse("2025-09-26 00:00:00"));
            queryDto.setSendDateEnd(sdf.parse("2025-09-26 23:59:59"));

            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("时间范围：2025-09-26 到 2025-09-26");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 发起时间: " + sdf.format(vo.getSendDate()));
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例6：组合条件查询
     */
    @Test
    public void testGetAllDataElementPage_Combined() {
        System.out.println("\n=== 测试用例6：组合条件查询 ===");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
            queryDto.setPageNum(1L);
            queryDto.setPageSize(10L);
            queryDto.setStatusList(Arrays.asList("DESIGNATED_SOURCE", "PENDING_SOURCE"));
            queryDto.setKeyword("企业");
            queryDto.setSendDateBegin(sdf.parse("2024-01-15 00:00:00"));
            queryDto.setSendDateEnd(sdf.parse("2024-01-17 23:59:59"));

            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("组合条件：状态(已定源,待定源) + 关键字(企业) + 时间范围");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("ID: " + vo.getDataid() +
                        ", 名称: " + vo.getName() +
                        ", 状态: " + vo.getStatus() +
                        ", 发起时间: " + sdf.format(vo.getSendDate()));
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 测试用例7：关键字带空格的搜索测试
     */
    @Test
    public void testGetAllDataElementPage_KeywordWithSpaces() {
        System.out.println("\n=== 测试用例7：关键字带空格的搜索测试 ===");

        // 测试前后有空格的关键字
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(10L);
        queryDto1.setKeyword("  现金流  "); // 前后有空格

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("测试1 - 关键字带前后空格：'  现金流  '");
            System.out.println("查询结果数量：" + result1.getRecords().size());

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                System.out.println("匹配到: " + vo.getName() + " - " + vo.getDefinition());
            }

        } catch (Exception e) {
            System.err.println("测试1失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试中间有空格的关键字
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(10L);
        queryDto2.setKeyword(" 负债 合计 "); // 中间和前后都有空格

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n测试2 - 关键字多处空格：' 负债 合计 '");
            System.out.println("查询结果数量：" + result2.getRecords().size());

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                System.out.println("匹配到: " + vo.getName() + " - " + vo.getDefinition());
            }

        } catch (Exception e) {
            System.err.println("测试2失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试只包含空格的关键字
        DataElementPageQueryDto queryDto3 = new DataElementPageQueryDto();
        queryDto3.setPageNum(1L);
        queryDto3.setPageSize(5L);
        queryDto3.setKeyword("   "); // 只有空格

        try {
            Page<DataElementPageInfoVo> result3 = controller.getAllDataElementPage(queryDto3);
            System.out.println("\n测试3 - 只有空格的关键字：'   '");
            System.out.println("查询结果数量：" + result3.getRecords().size());
            System.out.println("应该返回所有数据（空格被去除后为空字符串）");

        } catch (Exception e) {
            System.err.println("测试3失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例8：采集单位数量排序测试（数字排序）
     */
    @Test
    public void testGetAllDataElementPage_SortByCollectUnitQty() {
        System.out.println("\n=== 测试用例8：采集单位数量排序测试 ===");

        // 测试升序
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(10L);
        queryDto1.setSortField("collectunitqty");
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("采集单位数量升序排序：");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                System.out.println("数据元: " + vo.getName() +
                        " | 采集单位数量: " + vo.getCollectunitqty() +
                        " | 状态: " + vo.getStatus());
            }

        } catch (Exception e) {
            System.err.println("升序测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试降序
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(10L);
        queryDto2.setSortField("collectunitqty");
        queryDto2.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n采集单位数量降序排序：");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                System.out.println("数据元: " + vo.getName() +
                        " | 采集单位数量: " + vo.getCollectunitqty() +
                        " | 状态: " + vo.getStatus());
            }

        } catch (Exception e) {
            System.err.println("降序测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例9：状态排序测试（字母排序）
     */
    @Test
    public void testGetAllDataElementPage_SortByStatus() {
        System.out.println("\n=== 测试用例9：状态排序测试 ===");

        // 测试状态升序
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(15L);
        queryDto1.setSortField("status");
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("状态升序排序：");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                System.out.println("状态: " + vo.getStatus() +
                        " (" + vo.getStatusDesc() + ") | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("状态升序测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试状态降序
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(15L);
        queryDto2.setSortField("status");
        queryDto2.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n状态降序排序：");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                System.out.println("状态: " + vo.getStatus() +
                        " (" + vo.getStatusDesc() + ") | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("状态降序测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例10：数源单位排序测试（字母排序，null值在后）
     */
    @Test
    public void testGetAllDataElementPage_SortBySourceUnit() {
        System.out.println("\n=== 测试用例10：数源单位排序测试 ===");

        // 测试数源单位升序
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(20L);
        queryDto1.setSortField("sourceUnitName");
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("数源单位升序排序（null值应该在后面）：");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                String unitName = vo.getSourceUnitName() == null ? "【NULL】" : vo.getSourceUnitName();
                System.out.println("数源单位: " + unitName + " | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("数源单位升序测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试数源单位降序
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(20L);
        queryDto2.setSortField("sourceUnitName");
        queryDto2.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n数源单位降序排序（null值应该在后面）：");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                String unitName = vo.getSourceUnitName() == null ? "【NULL】" : vo.getSourceUnitName();
                System.out.println("数源单位: " + unitName + " | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("数源单位降序测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例11：发起时间排序测试
     */
    @Test
    public void testGetAllDataElementPage_SortBySendDate() {
        System.out.println("\n=== 测试用例11：发起时间排序测试 ===");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 测试发起时间升序
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(10L);
        queryDto1.setSortField("sendDate");
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("发起时间升序排序：");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                String sendDate = vo.getSendDate() != null ? sdf.format(vo.getSendDate()) : "NULL";
                System.out.println("发起时间: " + sendDate + " | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("发起时间升序测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试发起时间降序
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(10L);
        queryDto2.setSortField("sendDate");
        queryDto2.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n发起时间降序排序：");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                String sendDate = vo.getSendDate() != null ? sdf.format(vo.getSendDate()) : "NULL";
                System.out.println("发起时间: " + sendDate + " | 数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("发起时间降序测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例12：定源时间排序测试
     */
    @Test
    public void testGetAllDataElementPage_SortByConfirmDate() {
        System.out.println("\n=== 测试用例12：定源时间排序测试 ===");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 测试定源时间升序
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(15L);
        queryDto1.setSortField("confirmDate");
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("定源时间升序排序：");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                String confirmDate = vo.getConfirmDate() != null ? sdf.format(vo.getConfirmDate()) : "NULL";
                System.out.println("定源时间: " + confirmDate + " | 数据元: " + vo.getName() + " | 状态: " + vo.getStatus());
            }

        } catch (Exception e) {
            System.err.println("定源时间升序测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试定源时间降序
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(15L);
        queryDto2.setSortField("confirmDate");
        queryDto2.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n定源时间降序排序：");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                String confirmDate = vo.getConfirmDate() != null ? sdf.format(vo.getConfirmDate()) : "NULL";
                System.out.println("定源时间: " + confirmDate + " | 数据元: " + vo.getName() + " | 状态: " + vo.getStatus());
            }

        } catch (Exception e) {
            System.err.println("定源时间降序测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例13：无效排序参数测试
     */
    @Test
    public void testGetAllDataElementPage_InvalidSortParams() {
        System.out.println("\n=== 测试用例13：无效排序参数测试 ===");

        // 测试无效排序字段
        DataElementPageQueryDto queryDto1 = new DataElementPageQueryDto();
        queryDto1.setPageNum(1L);
        queryDto1.setPageSize(5L);
        queryDto1.setSortField("invalidField"); // 无效字段
        queryDto1.setSortOrder("asc");

        try {
            Page<DataElementPageInfoVo> result1 = controller.getAllDataElementPage(queryDto1);
            System.out.println("测试无效排序字段 'invalidField'：");
            System.out.println("查询结果数量：" + result1.getRecords().size());
            System.out.println("应该回退到默认排序（按发起时间降序）");

            for (DataElementPageInfoVo vo : result1.getRecords()) {
                System.out.println("数据元: " + vo.getName());
            }

        } catch (Exception e) {
            System.err.println("无效排序字段测试失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 测试无效排序方向
        DataElementPageQueryDto queryDto2 = new DataElementPageQueryDto();
        queryDto2.setPageNum(1L);
        queryDto2.setPageSize(5L);
        queryDto2.setSortField("status");
        queryDto2.setSortOrder("invalid"); // 无效排序方向

        try {
            Page<DataElementPageInfoVo> result2 = controller.getAllDataElementPage(queryDto2);
            System.out.println("\n测试无效排序方向 'invalid'：");
            System.out.println("查询结果数量：" + result2.getRecords().size());
            System.out.println("应该回退到默认排序（按发起时间降序）");

            for (DataElementPageInfoVo vo : result2.getRecords()) {
                System.out.println("数据元: " + vo.getName() + " | 状态: " + vo.getStatus());
            }

        } catch (Exception e) {
            System.err.println("无效排序方向测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例14：组合条件与排序测试
     */
    @Test
    public void testGetAllDataElementPage_CombinedWithSort() {
        System.out.println("\n=== 测试用例14：组合条件与排序测试 ===");

        DataElementPageQueryDto queryDto = new DataElementPageQueryDto();
        queryDto.setPageNum(1L);
        queryDto.setPageSize(10L);
        queryDto.setKeyword("  现金  "); // 带空格的关键字
        queryDto.setStatusList(Arrays.asList("pending_source"));
        queryDto.setSortField("collectunitqty");
        queryDto.setSortOrder("desc");

        try {
            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);
            System.out.println("组合测试：关键字搜索(含空格) + 状态筛选 + 采集单位数量降序：");
            System.out.println("查询结果数量：" + result.getRecords().size());

            for (DataElementPageInfoVo vo : result.getRecords()) {
                System.out.println("数据元: " + vo.getName() +
                        " | 采集单位数量: " + vo.getCollectunitqty() +
                        " | 状态: " + vo.getStatus() +
                        " | 数源单位: " + (vo.getSourceUnitName() != null ? vo.getSourceUnitName() : "NULL"));
            }

        } catch (Exception e) {
            System.err.println("组合测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 运行所有新增的测试用例
     */
    @Test
    public void runAllNewTests() {
        System.out.println("开始执行所有新增测试用例（关键字去空格 + 动态排序）...\n");

        testGetAllDataElementPage_ByDateRange();

        System.out.println("\n所有新增测试用例执行完成！");
    }


}
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
public class AlldataelementinfoControllerTest {

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
            queryDto.setSendDateBegin(sdf.parse("2024-01-16 00:00:00"));
            queryDto.setSendDateEnd(sdf.parse("2024-01-18 23:59:59"));

            Page<DataElementPageInfoVo> result = controller.getAllDataElementPage(queryDto);

            System.out.println("时间范围：2024-01-16 到 2024-01-18");
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
     * 运行所有测试用例
     */
    @Test
    public void runAllTests() {
        System.out.println("开始执行所有测试用例...\n");

        testGetAllDataElementPage_Basic();
        testGetAllDataElementPage_ByStatus();
        testGetAllDataElementPage_ByKeyword();
        testGetAllDataElementPage_BySourceUnit();
        testGetAllDataElementPage_ByDateRange();
        testGetAllDataElementPage_Combined();

        System.out.println("\n所有测试用例执行完成！");
    }
}
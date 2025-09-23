package com.inspur.dsp.direct.console.test;


import com.inspur.dsp.direct.console.controller.business.AlldataelementinfoController;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import com.inspur.dsp.direct.entity.vo.FailureDetailVo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 上传文件测试类
 */
@SpringBootTest
public class UploadFileTest {

    @Autowired
    private AlldataelementinfoController controller;

    /**
     * 测试用例1：正常Excel文件上传
     */
    @Test
    public void testUploadConfirmUnitFile_Success() {
        System.out.println("=== 测试用例1：正常Excel文件上传 ===");

        try {
            // 创建测试Excel文件
            MultipartFile testFile = createTestExcelFile();

            // 调用上传接口
            UploadConfirmResultVo result = controller.uploadconfirmunitfile(testFile);

            // 打印结果
            System.out.println("上传结果：");
            System.out.println("总记录数：" + result.getTotalCount());
            System.out.println("成功记录数：" + result.getSuccessCount());
            System.out.println("失败记录数：" + result.getFailureCount());

            if (result.getFailureCount() > 0) {
                System.out.println("\n失败记录详情：");
                for (FailureDetailVo failure : result.getFailureDetails()) {
                    System.out.println("----------------------------------------");
                    System.out.println("序号：" + failure.getSerialNumber());
                    System.out.println("数据元名称：" + failure.getElementName());
                    System.out.println("数源单位代码：" + failure.getUnitCode());
                    System.out.println("失败原因：" + failure.getFailureReason());
                }
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例2：空文件上传
     */
    @Test
    public void testUploadConfirmUnitFile_EmptyFile() {
        System.out.println("\n=== 测试用例2：空文件上传 ===");

        try {
            // 创建空文件
            MultipartFile emptyFile = new MockMultipartFile(
                    "file",
                    "empty.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    new byte[0]
            );

            // 调用上传接口
            UploadConfirmResultVo result = controller.uploadconfirmunitfile(emptyFile);
            System.out.println("空文件测试不应该成功");

        } catch (Exception e) {
            System.out.println("空文件测试正确抛出异常：" + e.getMessage());
        }
    }

    /**
     * 测试用例3：错误格式文件上传
     */
    @Test
    public void testUploadConfirmUnitFile_WrongFormat() {
        System.out.println("\n=== 测试用例3：错误格式文件上传 ===");

        try {
            // 创建txt文件
            MultipartFile txtFile = new MockMultipartFile(
                    "file",
                    "test.txt",
                    "text/plain",
                    "这是一个文本文件".getBytes()
            );

            // 调用上传接口
            UploadConfirmResultVo result = controller.uploadconfirmunitfile(txtFile);
            System.out.println("错误格式文件测试不应该成功");

        } catch (Exception e) {
            System.out.println("错误格式文件测试正确抛出异常：" + e.getMessage());
        }
    }

    /**
     * 测试用例4：包含错误数据的Excel文件
     */
    @Test
    public void testUploadConfirmUnitFile_WithErrors() {
        System.out.println("\n=== 测试用例4：包含错误数据的Excel文件 ===");

        try {
            // 创建包含错误数据的Excel文件
            MultipartFile testFile = createErrorExcelFile();

            // 调用上传接口
            UploadConfirmResultVo result = controller.uploadconfirmunitfile(testFile);

            // 打印结果
            System.out.println("上传结果：");
            System.out.println("总记录数：" + result.getTotalCount());
            System.out.println("成功记录数：" + result.getSuccessCount());
            System.out.println("失败记录数：" + result.getFailureCount());

            System.out.println("\n失败记录详情：");
            for (FailureDetailVo failure : result.getFailureDetails()) {
                System.out.println("----------------------------------------");
                System.out.println("序号：" + failure.getSerialNumber());
                System.out.println("数据元名称：" + failure.getElementName());
                System.out.println("数源单位代码：" + failure.getUnitCode());
                System.out.println("失败原因：" + failure.getFailureReason());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建正常测试Excel文件
     */
    private MultipartFile createTestExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("测试数据");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("序号");
        headerRow.createCell(1).setCellValue("数据元名称");
        headerRow.createCell(2).setCellValue("数源单位统一社会信用代码");

        // 创建测试数据行
        String[][] testData = {
                {"1", "企业名称", "91110000123456789A"},
                {"2", "注册资本", "91110000987654321B"},
                {"3", "法定代表人", "91110000123456789A"},
                {"4", "组织机构代码", "91110000987654321B"}
        };

        for (int i = 0; i < testData.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < testData[i].length; j++) {
                row.createCell(j).setCellValue(testData[i][j]);
            }
        }

        // 转换为MultipartFile
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new MockMultipartFile(
                "file",
                "D:\\IDEAProjects\\dataelement-management_clean\\dataelement-management-console\\src\\test\\test_upload.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                outputStream.toByteArray()
        );
    }

    /**
     * 创建包含错误数据的Excel文件
     */
    private MultipartFile createErrorExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("错误测试数据");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("序号");
        headerRow.createCell(1).setCellValue("数据元名称");
        headerRow.createCell(2).setCellValue("数源单位统一社会信用代码");

        // 创建包含错误的测试数据
        String[][] errorData = {
                {"1", "", "91110000123456789A"},  // 空数据元名称
                {"2", "不存在的数据元", "91110000123456789A"},  // 不存在的数据元
                {"3", "企业名称", ""},  // 空数源单位代码
                {"4", "注册资本", "不存在的单位代码"},  // 不存在的数源单位
                {"5", "法定代表人", "91110000123456789A"}  // 正常数据
        };

        for (int i = 0; i < errorData.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < errorData[i].length; j++) {
                row.createCell(j).setCellValue(errorData[i][j]);
            }
        }

        // 转换为MultipartFile
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new MockMultipartFile(
                "file",
                "error_test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                outputStream.toByteArray()
        );
    }

    /**
     * 运行所有测试用例
     */
    @Test
    public void runAllUploadTests() {
        System.out.println("开始执行所有上传文件测试用例...\n");

        testUploadConfirmUnitFile_Success();
        testUploadConfirmUnitFile_EmptyFile();
        testUploadConfirmUnitFile_WrongFormat();
        testUploadConfirmUnitFile_WithErrors();

        System.out.println("\n所有上传文件测试用例执行完成！");
    }
}

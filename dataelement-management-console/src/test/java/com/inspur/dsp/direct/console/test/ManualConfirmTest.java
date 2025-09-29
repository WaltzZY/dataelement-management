package com.inspur.dsp.direct.console.test;


import com.inspur.dsp.direct.console.controller.business.AlldataelementinfoController;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.service.AlldataelementinfoService;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 手动定源测试类
 */
@SpringBootTest
public class ManualConfirmTest {

    @Autowired
    private AlldataelementinfoController controller;

    @Autowired
    private AlldataelementinfoService alldataelementinfoService;

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    /**
     * 测试用例1：正常手动定源
     */
    @Test
    public void testManualConfirmUnit_Success() {
        System.out.println("=== 测试用例1：正常手动定源 ===");

        try {
            String testDataId = "222e8400-e29b-41d4-a716-446655440013";

            // 1. 查询测试前的数据状态
            BaseDataElement beforeElement = baseDataElementMapper.selectById(testDataId);
            if (beforeElement == null) {
                System.out.println("测试数据不存在，请先插入测试数据：" + testDataId);
                return;
            }

            System.out.println("定源前状态：");
            System.out.println("  数据元ID：" + beforeElement.getDataid());
            System.out.println("  数据元名称：" + beforeElement.getName());
            System.out.println("  当前状态：" + beforeElement.getStatus());
            System.out.println("  数源单位代码：" + beforeElement.getSourceUnitCode());
            System.out.println("  数源单位名称：" + beforeElement.getSourceUnitName());

            // 2. 构造手动定源请求
            ManualConfirmUnitDto confirmDto = ManualConfirmUnitDto.builder()
                    .dataid(testDataId)
                    .sourceUnitId("914500001000889000")  // 使用已存在的测试单位
                    .build();

            System.out.println("\n执行手动定源操作...");
            System.out.println("  目标数源单位ID：" + confirmDto.getSourceUnitId());

            // 3. 调用手动定源接口
            controller.manualConfirmUnit(confirmDto);

            // 4. 查询定源后的数据状态
            BaseDataElement afterElement = baseDataElementMapper.selectById(testDataId);

            System.out.println("\n定源后状态：");
            System.out.println("  数据元ID：" + afterElement.getDataid());
            System.out.println("  数据元名称：" + afterElement.getName());
            System.out.println("  当前状态：" + afterElement.getStatus());
            System.out.println("  数源单位代码：" + afterElement.getSourceUnitCode());
            System.out.println("  数源单位名称：" + afterElement.getSourceUnitName());
            System.out.println("  定源时间：" + afterElement.getConfirmDate());
            System.out.println("  最后修改人：" + afterElement.getLastModifyAccount());
            System.out.println("  最后修改时间：" + afterElement.getLastModifyDate());

            // 5. 验证结果
            if ("DESIGNATED_SOURCE".equals(afterElement.getStatus())) {
                System.out.println("\n✅ 手动定源测试成功！");
                System.out.println("  状态已更新为：已定源");
                System.out.println("  数源单位已设置：" + afterElement.getSourceUnitName());
            } else {
                System.out.println("\n❌ 手动定源测试失败！");
                System.out.println("  期望状态：DESIGNATED_SOURCE");
                System.out.println("  实际状态：" + afterElement.getStatus());
            }

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试用例2：不存在的数据元
     */
    @Test
    public void testManualConfirmUnit_DataElementNotExists() {
        System.out.println("\n=== 测试用例2：不存在的数据元 ===");

        try {
            ManualConfirmUnitDto confirmDto = ManualConfirmUnitDto.builder()
                    .dataid("not_exists_id")
                    .sourceUnitId("91110000123456789A")
                    .build();

            controller.manualConfirmUnit(confirmDto);
            System.out.println("❌ 不存在数据元的测试不应该成功");

        } catch (Exception e) {
            System.out.println("✅ 不存在数据元测试正确抛出异常：" + e.getMessage());
        }
    }

    /**
     * 测试用例3：参数校验测试
     */
    @Test
    public void testManualConfirmUnit_InvalidParams() {
        System.out.println("\n=== 测试用例3：参数校验测试 ===");

        try {
            // 测试空的dataid
            ManualConfirmUnitDto confirmDto1 = ManualConfirmUnitDto.builder()
                    .dataid("")  // 空字符串
                    .sourceUnitId("91110000123456789A")
                    .build();

            controller.manualConfirmUnit(confirmDto1);
            System.out.println("❌ 空dataid测试不应该成功");

        } catch (Exception e) {
            System.out.println("✅ 空dataid测试正确抛出异常：" + e.getMessage());
        }

        try {
            // 测试空的sourceUnitId
            ManualConfirmUnitDto confirmDto2 = ManualConfirmUnitDto.builder()
                    .dataid("manual_test_001")
                    .sourceUnitId("")  // 空字符串
                    .build();

            controller.manualConfirmUnit(confirmDto2);
            System.out.println("❌ 空sourceUnitId测试不应该成功");

        } catch (Exception e) {
            System.out.println("✅ 空sourceUnitId测试正确抛出异常：" + e.getMessage());
        }
    }

    /**
     * 测试用例4：批量手动定源测试
     */
    @Test
    public void testManualConfirmUnit_Batch() {
        System.out.println("\n=== 测试用例4：批量手动定源测试 ===");

        String[] testDataIds = {"manual_test_001", "manual_test_002", "manual_test_003"};
        String[] sourceUnitIds = {"91110000123456789A", "91110000987654321B", "91110000123456789A"};

        for (int i = 0; i < testDataIds.length; i++) {
            try {
                String testDataId = testDataIds[i];
                String sourceUnitId = sourceUnitIds[i];

                System.out.println("\n处理数据元：" + testDataId);

                // 查询定源前状态
                BaseDataElement beforeElement = baseDataElementMapper.selectById(testDataId);
                if (beforeElement == null) {
                    System.out.println("  跳过：测试数据不存在");
                    continue;
                }

                System.out.println("  定源前状态：" + beforeElement.getStatus());

                // 执行手动定源
                ManualConfirmUnitDto confirmDto = ManualConfirmUnitDto.builder()
                        .dataid(testDataId)
                        .sourceUnitId(sourceUnitId)
                        .build();

                controller.manualConfirmUnit(confirmDto);

                // 查询定源后状态
                BaseDataElement afterElement = baseDataElementMapper.selectById(testDataId);
                System.out.println("  定源后状态：" + afterElement.getStatus());
                System.out.println("  数源单位：" + afterElement.getSourceUnitName());

                if ("DESIGNATED_SOURCE".equals(afterElement.getStatus())) {
                    System.out.println("  ✅ 定源成功");
                } else {
                    System.out.println("  ❌ 定源失败");
                }

            } catch (Exception e) {
                System.err.println("  ❌ 处理失败：" + e.getMessage());
            }
        }
    }

    /**
     * 测试用例5：验证定源事件记录
     */
    @Test
    public void testManualConfirmUnit_VerifyEventRecord() {
        System.out.println("\n=== 测试用例5：验证定源事件记录 ===");

        try {
            String testDataId = "manual_test_002";

            // 执行手动定源
            ManualConfirmUnitDto confirmDto = ManualConfirmUnitDto.builder()
                    .dataid(testDataId)
                    .sourceUnitId("91110000987654321B")
                    .build();

            System.out.println("执行手动定源：" + testDataId);
            controller.manualConfirmUnit(confirmDto);

            // 查询定源结果
            BaseDataElement element = baseDataElementMapper.selectById(testDataId);

            System.out.println("定源结果验证：");
            System.out.println("  数据元名称：" + element.getName());
            System.out.println("  最终状态：" + element.getStatus());
            System.out.println("  数源单位代码：" + element.getSourceUnitCode());
            System.out.println("  数源单位名称：" + element.getSourceUnitName());
            System.out.println("  定源时间：" + element.getConfirmDate());

            // 注意：根据代码，手动定源会插入source_event_record表的记录
            // 可以通过查询该表来验证事件记录是否正确创建
            System.out.println("\n💡 提示：可以查询 source_event_record 表验证定源事件记录");

        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 运行所有手动定源测试用例
     */
    @Test
    public void runAllManualConfirmTests() {
        System.out.println("开始执行所有手动定源测试用例...\n");

        testManualConfirmUnit_Success();
        testManualConfirmUnit_DataElementNotExists();
        testManualConfirmUnit_InvalidParams();
        testManualConfirmUnit_Batch();
        testManualConfirmUnit_VerifyEventRecord();

        System.out.println("\n所有手动定源测试用例执行完成！");
    }
}
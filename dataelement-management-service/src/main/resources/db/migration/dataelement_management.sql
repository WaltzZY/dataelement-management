/*
 Navicat Premium Dump SQL

 Source Server         : dataelement_management
 Source Server Type    : MySQL
 Source Server Version : 50725 (5.7.25-log)
 Source Host           : 10.110.26.121:3326
 Source Schema         : dataelement_management

 Target Server Type    : MySQL
 Target Server Version : 50725 (5.7.25-log)
 File Encoding         : 65001

 Date: 03/11/2025 10:29:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_data_element
-- ----------------------------
DROP TABLE IF EXISTS `base_data_element`;
CREATE TABLE `base_data_element`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据行唯一标识',
  `data_element_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元编码',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元状态:定标阶段(待定标: TodoDetermined 待审核: PendingReview 征求意见: SolicitingOpinions 待修订: TodoRevised 待复审: PendingReExamination 待发布: Todoreleased 已发布: Published )',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元名称',
  `definition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据元定义描述',
  `datatype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `data_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据格式',
  `value_domain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '值域',
  `source_unit_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数源单位统一社会信用代码',
  `source_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数源单位名称',
  `publish_date` datetime NULL DEFAULT NULL COMMENT '数据元发布日期(即定标时间)',
  `send_date` datetime NULL DEFAULT NULL COMMENT '发起定源时间',
  `confirm_date` datetime NULL DEFAULT NULL COMMENT '确定数源单位时间',
  `collectunitqty` int(11) NULL DEFAULT NULL COMMENT '采集单位数量',
  `generatedatetime` datetime NULL DEFAULT NULL COMMENT '定数时间',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人账号',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基准数据元表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_data_element
-- ----------------------------
INSERT INTO `base_data_element` VALUES ('00a1b2c3-d4e5-f678-9012-34567890abcd', 'DE0001', 'negotiating', '持有待售资产期末余额', '持有待售资产期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:47', NULL, 3, '2025-10-28 10:45:37', '持有待售资产期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:07:26', 'user1_002');
INSERT INTO `base_data_element` VALUES ('00abcdef-1234-5678-90ab-cdef01234567', 'DE0017', 'negotiating', '存货金额', '存货金额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:49', NULL, 3, '2025-10-28 10:45:37', '存货金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('01234567-89ab-cdef-0123-456789abcdef', 'DE00115', 'confirming', '定期存款期末余额', '定期存款期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:42:51', NULL, 1, '2025-10-28 10:45:37', '定期存款期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:42:51', 'user1_002');
INSERT INTO `base_data_element` VALUES ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'DE00124', 'pending_negotiation', '对外担保标识ID', '对外担保标识ID', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:15:13', NULL, 1, '2025-10-28 10:45:37', '对外担保标识ID', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:17:20', 'user1_003');
INSERT INTO `base_data_element` VALUES ('0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', 'DE00142', 'negotiating', '负债和净资产总计期末余额', '负债期末余额合计+净资产合计期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:50', NULL, 3, '2025-10-28 10:45:37', '负债期末余额合计+净资产合计期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', 'DE00133', 'negotiating', '非流动资产期末余额', '非流动资产期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:52', NULL, 3, '2025-10-28 10:45:37', '非流动资产期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('11b2c3d4-e5f6-7890-1234-567890abcdef', 'DE0002', 'negotiating', '持有至到期投资期末余额', '持有至到期投资期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:54', NULL, 3, '2025-10-28 10:45:37', '持有至到期投资期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('11bcdef0-1234-5678-90ab-cdef01234567', 'DE0018', 'negotiating', '大学及以上学历（人）', '企业的大学及以上学历人数。', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:55', NULL, 3, '2025-10-28 10:45:37', '企业的大学及以上学历人数。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d6', 'DE00117', 'pending_approval', '独立账户资产期末余额', '独立账户资产期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:28:03', NULL, 1, '2025-10-28 10:45:37', '独立账户资产期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:04:01', 'user1_003');
INSERT INTO `base_data_element` VALUES ('1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', 'DE00135', 'negotiating', '分保费收入本期金额', '分保费收入本期金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:57', NULL, 3, '2025-10-28 10:45:37', '分保费收入本期金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6a', 'DE00126', 'confirming', '对外投资所支付的现金金额', '对外投资所支付的现金金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:42:51', NULL, 1, '2025-10-28 10:45:37', '对外投资所支付的现金金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:42:51', 'user1_002');
INSERT INTO `base_data_element` VALUES ('1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', 'DE00144', 'negotiating', '负债及业主权益总计金额', '本项=负债合计金额+业主权益合计金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:34:59', NULL, 3, '2025-10-28 10:45:37', '本项=负债合计金额+业主权益合计金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('222e8400-e29b-41d4-a716-446655440013', 'DE0007', 'designated_source', '保户质押贷款期末余额', '保户质押贷款期末余额', '数值型', 'n..20', '1-1000', '914500001000889000', '经济和信息化局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:25:56', 1, '2025-10-28 10:45:37', '保户质押贷款期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:25:56', 'user1_002');
INSERT INTO `base_data_element` VALUES ('22c3d4e5-f678-9012-3456-7890abcdef01', 'DE0003', 'negotiating', '筹资活动产生的现金流量净额', '本项=本年筹资活动产生的现金流入小计-', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 14:36:16', NULL, 3, '2025-10-28 10:45:37', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f', 'DE00119', 'designated_source', '短期借款期末数', '短期借款期末数', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 15:15:13', '2025-10-31 15:18:30', 1, '2025-10-28 10:45:37', '短期借款期末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:18:30', 'user1_002');
INSERT INTO `base_data_element` VALUES ('2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', 'DE00137', 'negotiating', '分出保费本期金额', '分出保费本期金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:36:16', NULL, 3, '2025-10-28 10:45:37', '分出保费本期金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'DE00128', 'designated_source', '发放贷款和垫款期末余额', '发放贷款和垫款期末余额', '数值型', 'n..13', '1-1000', '914500001000889000', '经济和信息化局', NULL, '2025-10-31 14:16:47', '2025-10-31 14:26:01', 1, '2025-10-28 10:45:37', '发放贷款和垫款期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:26:01', 'user1_002');
INSERT INTO `base_data_element` VALUES ('2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', 'DE00146', 'negotiating', '负债与净资产总计期末数', '负债与净资产总计期末数=负债合计期末数+', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 14:36:17', NULL, 3, '2025-10-28 10:45:37', '负债与净资产总计期末数=负债合计期末数+净资产合计期末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:49', 'user1_002');
INSERT INTO `base_data_element` VALUES ('33d4e5f6-7890-1234-5678-90abcdef0123', 'DE0004', 'negotiating', '筹资活动产生的现金流量净额本年金额', '本项=筹资活动的现金流入小计本年金额-', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:14', NULL, 3, '2025-10-28 10:45:37', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'DE00130', 'negotiating', '反向投资股权比例', '企业拥有的境外投资者的股权投资额在境外投', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:14', NULL, 3, '2025-10-28 10:45:37', '企业拥有的境外投资者的股权投资额在境外投资者所有股权中所占的比例。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', 'DE00148', 'negotiating', '个人所得税', '报告年度企业（机构）实际缴纳的个人所得税', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-10-28 10:45:37', '报告年度企业（机构）实际缴纳的个人所得税。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', 'DE00121', 'designated_source', '短期投资年末数', '短期投资年末数', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:16:43', '2025-10-31 14:27:11', 1, '2025-10-28 10:45:37', '短期投资年末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:27:11', 'user1_002');
INSERT INTO `base_data_element` VALUES ('3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', 'DE00139', 'negotiating', '分配外方股东的利润', '报告年度实际分配外方股东的利润。', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-10-28 10:45:37', '报告年度实际分配外方股东的利润。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('444e8400-e29b-41d4-a716-446655440015', 'DE0008', 'pending_source', '保险合同准备金期末余额', '保险合同准备金期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, NULL, NULL, 1, '2025-10-28 10:45:37', '保险合同准备金期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('44e5f678-9012-3456-7890-abcdef012345', 'DE0005', 'negotiating', '筹资活动的现金流出小计本年金额', '本项=偿还借款支付的现金本年金额+偿还', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-10-28 10:45:37', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', 'DE00123', 'designated_source', '短期投资期末余额', '短期投资期末余额', '数值型', 'n..13', '1-1000', '913900001000334000', '国家数据共享主管部门', NULL, '2025-10-31 14:17:12', '2025-10-31 14:26:53', 1, '2025-10-28 10:45:37', '短期投资期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:26:53', 'user1_002');
INSERT INTO `base_data_element` VALUES ('4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', 'DE00141', 'negotiating', '负债和净资产总计期末数', '负债和净资产期末数总计=负债期末数合计+', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-10-28 10:45:37', '负债和净资产期末数总计=负债期末数合计+净资产期末数合计', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'DE00132', 'negotiating', '非流动负债合计期末余额', '长期借款期末余额+长期应付款期末余额+预', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-10-28 10:45:37', '长期借款期末余额+长期应付款期末余额+预计负债期末余额+其他非流动负债期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('55f67890-1234-5678-90ab-cdef01234567', 'DE0006', 'negotiating', '筹资活动现金金额流出小计', '本项=偿还借款所支付的现金金额+偿付利', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-09-28 10:45:44', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e0f', 'DE00125', 'pending_negotiation', '对外投资年末数', '对外投资年末数', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:42:51', NULL, 1, '2025-09-28 10:45:44', '对外投资年末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 16:28:55', 'user1_003');
INSERT INTO `base_data_element` VALUES ('5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', 'DE00143', 'negotiating', '负债和所有者权益总计年末数', '负债合计年末数+所有者权益合计年末数', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-09-28 10:45:44', '负债合计年末数+所有者权益合计年末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', 'DE00134', 'negotiating', '非限定性净资产期末数', '非限定性净资产期末数', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:10:15', NULL, 3, '2025-09-28 10:45:44', '非限定性净资产期末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:59', 'user1_002');
INSERT INTO `base_data_element` VALUES ('660ab1c2-def3-4567-890a-bcdef1234567', 'DE0007', 'designated_source', '筹资活动现金金额流量净额', '本项=筹资活动现金金额流入小计-筹资活', '数值型', 'n..20', '1-1000', '911800001000556000', '统计局', NULL, '2025-10-31 15:16:20', '2025-10-31 15:18:22', 3, '2025-09-28 10:45:44', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:18:22', 'user1_002');
INSERT INTO `base_data_element` VALUES ('666e8400-e29b-41d4-a716-446655440017', 'DE0009', 'pending_source', '保险业务收入本期金额', '保险业务收入本期金额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, NULL, NULL, 1, '2025-09-28 10:45:44', '保险业务收入本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', 'DE00127', 'designated_source', '对外投资支付的现金本年金额', '对外投资支付的现金本年金额', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:23:31', 1, '2025-09-28 10:45:44', '对外投资支付的现金本年金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:31', 'user1_002');
INSERT INTO `base_data_element` VALUES ('6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', 'DE00145', 'designated_source', '负债期末数合计', '负债期末数合计=流动负债期末数+长期负债', '数值型', 'n..13', '1-1000', '911800001000556000', '统计局', NULL, '2025-10-31 15:16:20', '2025-10-31 15:18:19', 3, '2025-09-28 10:45:44', '负债期末数合计=流动负债期末数+长期负债合计期末数+受托代理负债期末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:18:19', 'user1_002');
INSERT INTO `base_data_element` VALUES ('770e8400-e29b-41d4-a716-446655440003', 'DE0002', 'pending_approval', '按所有权归属分类本期金额', '按所有权归属分类本期金额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 16:28:24', NULL, 1, '2025-09-28 10:45:44', '按所有权归属分类本期金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 16:28:47', 'user1_003');
INSERT INTO `base_data_element` VALUES ('771bc2de-f345-6789-0abc-def123456789', 'DE0008', 'pending_approval', '筹资活动现金金额流入小计', '本项=借款所收到的现金金额+收到的其他', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:16:20', NULL, 3, '2025-09-28 10:45:44', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', 'DE00129', 'pending_approval', '发行债券收到的现金本期金额', '发行债券收到的现金本期金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:16:20', NULL, 3, '2025-09-28 10:45:44', '发行债券收到的现金本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', 'DE00147', 'designated_source', '负债总额是否公示', '企业选择是否公示负债总额。', '数值型', 'n..13', '1-1000', '911800001000556000', '统计局', NULL, '2025-10-31 15:16:20', '2025-10-31 15:18:30', 3, '2025-09-28 10:45:44', '企业选择是否公示负债总额。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:18:30', 'user1_002');
INSERT INTO `base_data_element` VALUES ('7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d2', 'DE00118', 'pending_source', '短期借款年末数', '短期借款年末数', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, NULL, NULL, 1, '2025-09-28 10:45:44', '短期借款年末数', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', 'DE00136', 'pending_negotiation', '分保费用本期金额', '分保费用本期金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:23:09', NULL, 3, '2025-09-28 10:45:44', '分保费用本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('882def34-5678-90ab-cdef-1234567890ab', 'DE0009', 'pending_negotiation', '筹资活动现金流出小计本期金额', '偿还债务支付的现金本期金额+分配股利、', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:23:09', NULL, 3, '2025-09-28 10:45:44', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('888e8400-e29b-41d4-a716-446655440019', 'DE0010', 'designated_source', '保障性住房净值期末余额', '保障性住房期末余额-保障性住房累计折旧', '数值型', 'n..20', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:23:41', 1, '2025-09-28 10:45:44', '保障性住房期末余额-保障性住房累计折旧期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:41', 'user1_002');
INSERT INTO `base_data_element` VALUES ('8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', 'DE00131', 'pending_negotiation', '返售业务资金净增加额本期金额', '返售业务资金净增加额本期金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:23:09', NULL, 3, '2025-09-28 10:45:44', '返售业务资金净增加额本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', 'DE00149', 'pending_negotiation', '个人所得税金额', '个人所得税金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:23:09', NULL, 3, '2025-09-28 10:45:44', '个人所得税金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3a', 'DE00120', 'pending_source', '短期借款期末余额', '短期借款期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, NULL, NULL, 1, '2025-09-28 10:45:44', '短期借款期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', 'DE00138', 'pending_negotiation', '分配利润支付的现金本年累计金额', '分配利润支付的现金本年累计金额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:23:09', NULL, 3, '2025-09-28 10:45:44', '分配利润支付的现金本年累计金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('98765432-1abc-def0-9876-543210abcdef', 'DE00113', 'confirming', '电子邮件', '经营主体或自然人的电子邮箱。', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:15:13', NULL, 1, '2025-09-28 10:45:44', '经营主体或自然人的电子邮箱。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:15:13', 'user1_002');
INSERT INTO `base_data_element` VALUES ('990e8400-e29b-41d4-a716-446655440005', 'DE0003', 'designated_source', '办理使用登记特种设备总台（套）数', '特种设备企业办理使用登记特种设备总台（', '数值型', 'n..12', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:23:29', 1, '2025-09-28 10:45:44', '特种设备企业办理使用登记特种设备总台（套）数 。', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:29', 'user1_002');
INSERT INTO `base_data_element` VALUES ('99345678-90ab-cdef-1234-567890abcdef', 'DE0010', 'claimed_ing', '筹资活动现金流入小计本期金额', '吸收投资收到的现金本期金额+取得借款收', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:35:21', NULL, 3, '2025-09-28 10:45:44', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3c4', 'DE00122', 'designated_source', '短期投资期末数', '短期投资期末数', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:23:28', 1, '2025-09-28 10:45:44', '短期投资期末数', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:28', 'user1_002');
INSERT INTO `base_data_element` VALUES ('9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', 'DE00140', 'claimed_ing', '负债合计金额', '本项=借入款项金额+应付款项金额+应付工', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:35:21', NULL, 3, '2025-09-28 10:45:44', '本项=借入款项金额+应付款项金额+应付工资金额+未交税金金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'DE00111', 'designated_source', '递延所得税负债期末余额', '递延所得税负债期末余额', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:16:45', '2025-10-31 14:23:41', 1, '2025-09-28 10:45:44', '递延所得税负债期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:41', 'user1_002');
INSERT INTO `base_data_element` VALUES ('aa456789-0abc-def1-2345-67890abcdef0', 'DE0011', 'claimed_ing', '初始建设投资金额', '是指该项目首次建成通车时的原始投资。不', '数值型', 'n', '1-1000', NULL, NULL, NULL, '2025-10-31 15:35:22', NULL, 3, '2025-09-28 10:45:44', '', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('abcdef98-7654-3210-fedc-ba9876543210', 'DE00114', 'designated_source', '电子邮箱', '经营主体或自然人的电子邮箱。', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 15:03:32', '2025-10-31 15:06:26', 1, '2025-09-28 10:45:44', '经营主体或自然人的电子邮箱。', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:06:26', 'user1_002');
INSERT INTO `base_data_element` VALUES ('b5c6d7e8-f9a0-1234-5678-9abcdef01234', 'DE00151', 'claimed_ing', '工程物资期末余额', '工程物资期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:35:22', NULL, 3, '2025-09-28 10:45:44', '工程物资期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('bb0e8400-e29b-41d4-a716-446655440007', 'DE0004', 'designated_source', '保单红利支出本期金额', '保单红利支出本期金额', '数值型', 'n..20', '1-1000', '111000000000132664', '中华人民共和国农业农村部', NULL, '2025-10-31 14:17:12', '2025-10-31 14:27:17', 1, '2025-09-28 10:45:44', '保单红利支出本期金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:27:17', 'user1_002');
INSERT INTO `base_data_element` VALUES ('bb567890-abcd-ef12-3456-7890abcdef01', 'DE0012', 'claimed_ing', '从业人数', '指从事某项具体工作的人员数量。', '数值型', 'n..12', '1-1000', NULL, NULL, NULL, '2025-10-31 15:35:22', NULL, 3, '2025-09-28 10:45:44', '指从事某项具体工作的人员数量。', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('cc67890a-bcde-f123-4567-890abcdef012', 'DE0013', 'claimed_ing', '存出保证金期末余额', '存出保证金期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 16:28:29', NULL, 3, '2025-09-28 10:45:44', '存出保证金期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('dd0e8400-e29b-41d4-a716-446655440009', 'DE0005', 'designated_source', '保户储金及投资款净增加额本期金额', '保户储金及投资款净增加额本期金额', '数值型', 'n..20', '1-1000', '111000000000132664', '中华人民共和国农业农村部', NULL, '2025-10-31 14:17:12', '2025-10-31 14:27:14', 1, '2025-09-28 10:45:44', '保户储金及投资款净增加额本期金额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:27:14', 'user1_002');
INSERT INTO `base_data_element` VALUES ('dd7890ab-cdef-1234-5678-90abcdef0123', 'DE0014', 'claimed_ing', '存出资本保证金期末余额', '存出资本保证金期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 16:29:24', NULL, 3, '2025-09-28 10:45:44', '存出资本保证金期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('deadbeef-cafe-babe-fade-deafcafebabe', 'DE00116', 'pending_approval', '独立账户负债期末余额', '独立账户负债期末余额', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, '2025-10-31 15:42:51', NULL, 1, '2025-09-28 10:45:44', '独立账户负债期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:53:01', 'user1_003');
INSERT INTO `base_data_element` VALUES ('ee890abc-def1-2345-6789-0abcdef01234', 'DE0015', 'pending_source', '存放同业款项期末余额', '存放同业款项期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, NULL, NULL, 3, '2025-09-28 10:45:44', '存放同业款项期末余额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('f4e3d2c1-b0a9-8765-4321-0fedcba98765', 'DE00150', 'pending_source', '工程物资期末数', '工程物资期末数', '数值型', 'n..13', '1-1000', NULL, NULL, NULL, NULL, NULL, 3, '2025-09-28 10:45:44', '工程物资期末数', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');
INSERT INTO `base_data_element` VALUES ('fedcba98-7654-3210-fedc-ba9876543210', 'DE00112', 'designated_source', '递延所得税资产期末余额', '递延所得税资产期末余额', '数值型', 'n..13', '1-1000', '11100000000014445H', '国家税务总局', NULL, '2025-10-31 14:17:12', '2025-10-31 14:23:41', 1, '2025-09-28 10:45:44', '递延所得税资产期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 14:23:41', 'user1_002');
INSERT INTO `base_data_element` VALUES ('ff0e8400-e29b-41d4-a716-446655440011', 'DE0006', 'pending_negotiation', '保户储金及投资款期末余额', '保户储金及投资款期末余额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, '2025-10-31 15:15:07', NULL, 1, '2025-09-28 10:45:44', '保户储金及投资款期末余额', '2024-10-23 16:56:20', 'admin', '2025-10-31 15:16:57', 'user1_003');
INSERT INTO `base_data_element` VALUES ('ff90abcd-ef12-3456-7890-abcdef012345', 'DE0016', 'pending_source', '存放中央银行和同业款项净增加额本期金额', '存放中央银行和同业款项净增加额本期金额', '数值型', 'n..20', '1-1000', NULL, NULL, NULL, NULL, NULL, 3, '2025-09-28 10:45:44', '存放中央银行和同业款项净增加额本期金额', '2024-10-23 16:56:20', 'admin', '2024-10-23 16:56:20', 'admin');

-- ----------------------------

-- ----------------------------
-- Table structure for confirmation_task
-- ----------------------------
DROP TABLE IF EXISTS `confirmation_task`;
CREATE TABLE `confirmation_task`  (
  `task_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元唯一标识',
  `tasktype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类型(确认任务/认领任务)',
  `send_unit_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出单位统一社会信用代码',
  `send_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出单位名称',
  `send_date` datetime NULL DEFAULT NULL COMMENT '发出日期',
  `sender_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出人用户账号',
  `sender_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出人姓名',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认任务状态(待确认、已确认、已拒绝);认领任务状态(待认领,已认领,不认领)',
  `processing_unit_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务处理单位统一社会信用代码',
  `processing_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务处理单位名称',
  `processing_date` datetime NULL DEFAULT NULL COMMENT '处理日期',
  `processing_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `processing_opinion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理意见',
  `processor_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人用户账号',
  `processor_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '确认任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of confirmation_task
-- ----------------------------
INSERT INTO `confirmation_task` VALUES ('005e832a-a0c6-4819-8680-585635a33014', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('02984ca0-aca4-494a-86d2-7047b32af9c2', 'bb567890-abcd-ef12-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 16:29:41', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('0301d956-139c-48a1-849b-d06235dd85b0', '99345678-90ab-cdef-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 16:29:48', '不同意认领', 'burenl ', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('03aa0cce-d71a-41f3-84e4-f6b04a6b95c4', '770e8400-e29b-41d4-a716-446655440003', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:28:24', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 16:28:47', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('06700f5b-4e94-4ccf-8600-d88586a7a891', 'deadbeef-cafe-babe-fade-deafcafebabe', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:42:51', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 15:53:01', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('07593b7d-22c9-44e7-b3ad-a6718f4612e6', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e0f', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:42:51', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 16:28:55', '绝', '绝', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('0791217c-a829-4d90-8400-e629fe7478bc', '11b2c3d4-e5f6-7890-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:54', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:47:50', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('09a1b9f1-4695-454f-ab8d-72b427a13d5d', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:00', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('0ac89d68-8d7b-42b5-9d72-826c76aff013', '55f67890-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('0b328d30-502d-49f6-8c2e-3faceb733384', '11bcdef0-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:55', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:08', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('0b4c0792-41d7-4024-9593-d2f97fe4cc5d', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:52', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:42', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('0b625d45-8287-4903-aea4-c11f0c7ba54e', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:43', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('0c940386-843e-4da9-a439-1ac459c53b68', '11bcdef0-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:55', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:43', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('0ce716bc-657d-4013-922b-03adc72c17b7', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:50', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:13', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('0d28ff4f-209e-4f67-9e7f-9069943a2251', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:57', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:47:51', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('1221dd54-9bea-4f4d-b20c-04d07f1f0788', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:47:52', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('12f6b6d8-a053-41bf-b579-01d2ed63f243', '660ab1c2-def3-4567-890a-bcdef1234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:17:39', '不同意认领', '不认领。2', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('16be2770-8e7b-419c-958a-b256757ccc3b', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:16:45', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('1a604086-e5a3-4dac-9ae9-0b1e2d98841d', '55f67890-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('1ccca94c-9221-4487-83eb-c36b0f4bcc7f', 'bb567890-abcd-ef12-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('1e5879fc-1b8d-4f24-9107-ea9d6a06838a', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('22e46661-91db-43ba-9646-7437b3b94e67', 'ff0e8400-e29b-41d4-a716-446655440011', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:15:07', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 15:16:57', '拒绝同意', '拒绝同意', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('230279a9-a4b7-4a26-9d9c-c18fa98709f2', '11bcdef0-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:55', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:47:52', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('2495b1c4-d0b8-4ca0-9f5f-097d009b7f3d', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('274e5504-52b0-4fb1-92a5-a50a464cebed', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:23:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('2d0829ac-c243-4d34-9fbc-94fa0341f71b', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('2df302fd-74be-4751-83a1-ac92f319576c', '22c3d4e5-f678-9012-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:47:53', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('2ed6bce5-b4d1-45e6-80aa-ccba1db65e2e', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('2facfad1-1ea1-410c-8fb9-9b6f0ce88bab', 'dd7890ab-cdef-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:29:24', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 16:29:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('3285bc81-1152-4eef-a233-2d24e76f7b7b', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:52', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:15', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('3714b7dc-f256-4319-80f6-c92c62d7fcee', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:17:39', '不同意认领', '不认领。2', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('37f3e1d3-71a2-4938-a46e-097bb8d51376', 'aa456789-0abc-def1-2345-67890abcdef0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('3972888c-48fa-4d0a-9fc2-cd11e6c8cc41', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('3a9b1de5-f8a3-4fde-9472-a45c5257bd65', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:57', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:44', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('3ad587df-4e12-41e3-81d7-1113b5c7d641', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'pending_claimed', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('3d8d5424-79f4-49f6-9008-b8c4be71ffc8', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:16:45', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:19:44', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('3fdcbbe4-610f-4a3d-88e4-1c77bc868244', '44e5f678-9012-3456-7890-abcdef012345', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('45119484-d500-4f75-8ccf-63fac9413360', '01234567-89ab-cdef-0123-456789abcdef', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:42:51', 'user1_002', '李四', 'pending', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('482cbcb5-869d-43a0-8205-c3d77a2031d7', '22c3d4e5-f678-9012-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:46', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('499a893b-e24e-4b9a-85ed-caf75f2ea737', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:23:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('4d341c3b-fc1e-4a7f-bb08-5f64d630185a', '99345678-90ab-cdef-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('4dca4178-2288-4a9c-a449-07d0911d9bc7', '00abcdef-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:49', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:17', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('4e6ff462-2dac-4e52-8e00-7ee3d5465e99', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:17', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:46', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('572b18e6-fd25-4082-b81c-5a463cd9a5e3', 'cc67890a-bcde-f123-4567-890abcdef012', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:28:29', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('583a82cd-9a97-4a6d-b55e-b25bb6acbd58', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:24:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('586599a3-5b37-43f6-a952-ea2692ee2a53', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:23:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('58eada0c-e471-4809-828b-bf7c57579e38', 'bb567890-abcd-ef12-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('5acd45e3-36ac-4087-b685-e4666e4b138f', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:59', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:38:20', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('5f0f5c5a-1916-43f1-93d5-7e70a328f313', '888e8400-e29b-41d4-a716-446655440019', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:19:46', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('60ab45a7-0eaf-4797-8b91-3d2c72aaef42', '55f67890-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('60dded1c-a3dd-468c-b62d-e9108294e4d6', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('60e37914-2228-4c7c-ab30-d2e717f2937a', '660ab1c2-def3-4567-890a-bcdef1234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:16:45', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('64a83522-be50-4279-9420-7a78281712bb', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('67eb1ff1-644e-40a1-b056-32d855ab195f', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:24:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('6d3df7ed-3374-4b1a-bb64-0ee1364f0080', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:16:45', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('6e435557-e2fc-4b9c-968d-7ac7c36c837e', 'fedcba98-7654-3210-fedc-ba9876543210', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:19:46', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('6e7b405a-4dae-418f-b436-a12fe25baa50', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:17:39', '不同意认领', '不认领。2', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('6e877a44-888d-4113-a0a9-8e6b5968de51', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:50', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('6f35951c-b4d5-4943-8c06-6291ede1fab7', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:15:13', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 15:17:10', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('6f65ba96-69b3-4561-9ce5-c32ae1a66b7d', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:17:39', '不同意认领', '不认领。2', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('71273bd4-4376-40ea-a5c2-e22a1b1d6f57', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('7255bb33-f151-4eff-9fa6-661925035b8b', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('756897ef-332c-4775-9a38-53030e13982a', '44e5f678-9012-3456-7890-abcdef012345', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('78415fd9-7226-470e-8464-6737fecc62ee', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:24:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('7a2790bb-601d-4499-941f-0e31be608bcf', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('7a6714cd-db35-4ca8-b0ec-c5ed4979598a', 'bb0e8400-e29b-41d4-a716-446655440007', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:20:52', '我不是，我不是。', '我不是，我不是。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('7c0fea49-52e2-4424-8c72-511894e6299b', '33d4e5f6-7890-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('87231e84-325a-44fb-b1e9-7b2451417f29', '11b2c3d4-e5f6-7890-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:54', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:55', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('88822966-ed9d-4a94-bdc8-6618a6e297ea', '00a1b2c3-d4e5-f678-9012-34567890abcd', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:47', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('8c3fe395-9a6c-4436-8544-6e32ed827b8d', '00abcdef-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:49', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:55', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('9045ebd9-1d8a-4c96-a9f3-fe6fcd1726b1', '99345678-90ab-cdef-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('90eda671-df6f-4a58-ad40-4b576ad0a0cf', '771bc2de-f345-6789-0abc-def123456789', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:17:39', '不同意认领', '不认领。2', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('937a8366-6acc-4c8c-9acc-f5cdf48b50f5', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:25:26', '不同意认领', '不认领。', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('9430c1e5-5514-443b-8b2b-205c95a40cf1', '33d4e5f6-7890-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('962c0f84-3e72-40d4-845b-0810b59acd5a', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:23:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('968c1b06-1bc9-4551-9e70-0c4d9f153f36', '771bc2de-f345-6789-0abc-def123456789', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:16:45', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('9cbb59eb-a061-4c6a-a710-3bb63997fc6d', '222e8400-e29b-41d4-a716-446655440013', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:20:57', '我也不是。', '我也不是。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('9da8c04f-35a3-4258-95c1-b9e4ef1b7be5', 'cc67890a-bcde-f123-4567-890abcdef012', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:28:29', 'user1_002', '李四', 'pending_claimed', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('9db028e0-4f45-4a38-bebd-8686465d08b8', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6a', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:42:51', 'user1_002', '李四', 'pending', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('a13bdddd-1e42-4b10-803f-b28f09c392d1', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:25:26', '不同意认领', '不认领。', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('a3707061-376d-4836-8603-f3213418a649', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:59', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('a72d11e8-39cc-46f3-b0f8-adbe45182e61', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:15:13', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 15:17:20', '拒绝同意2', '拒绝同意2', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('a8c68c0f-8d25-4411-b91f-ebf0977ebdc7', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('a96e4b38-0b10-456c-8d6c-a1ac7180d809', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:16:43', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:02', '我更不是。', '我更不是。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('aa498a58-805d-4c2a-9548-3f99bd870625', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('aa4b6c70-7372-437f-8250-06683638f9b1', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3c4', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:25', '都是我的。', '都是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('ac64e55a-0a43-4ed2-ad4b-28cf2055fc58', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:17:12', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('aee58c3a-0e6e-4031-aa73-2635d7760ab1', '882def34-5678-90ab-cdef-1234567890ab', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:25:26', '不同意认领', '不认领。', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('b07d3eb4-78ff-4430-8d6f-d5fd82947d38', '44e5f678-9012-3456-7890-abcdef012345', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('b2277d90-9685-4648-80ab-dc8394dad028', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('b40420a0-34fb-4cbe-81d5-a62638cd1cf9', 'dd7890ab-cdef-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:29:24', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('b4bcdd8e-ed98-4eb1-9a9c-2c11bb1b6183', 'aa456789-0abc-def1-2345-67890abcdef0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('b9d13264-e001-4721-905f-fc25f92bfb76', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:57', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:42:28', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('bd556317-824f-49b1-87f4-dac985e6246a', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('c0f6ff01-ee74-4302-aa44-50ae1a7f2e88', '660ab1c2-def3-4567-890a-bcdef1234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:17:12', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('c4033ecb-988a-4ab7-9229-acbcbf8c6248', '990e8400-e29b-41d4-a716-446655440005', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:25', '都是我的。', '都是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('c5273a32-e9fa-42ba-96c4-e2e80df4269d', '00abcdef-1234-5678-90ab-cdef01234567', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:49', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('c62e3d1f-1446-4853-82c9-cc190edf67f8', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d6', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:28:03', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 15:04:01', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('c63c11b7-2769-4fc9-a516-503faf6cdbb2', '00a1b2c3-d4e5-f678-9012-34567890abcd', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:47', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:55', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('ca48c296-90df-4a3a-9b92-33fa8764f7f7', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('cad651b4-4484-4391-a0b6-89482f93b6e8', '882def34-5678-90ab-cdef-1234567890ab', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:23:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('cd865cd6-62c7-453e-9dbc-6c1a055b0970', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:17:12', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('ce4c1cda-c6cf-4b52-b1a8-52df24fc7af4', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:17', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('d03bf5ec-e5b7-4dd6-9e77-50c6d9a6e61f', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:50', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:55', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('d072f6af-fd64-4c55-b211-402479cf511e', '22c3d4e5-f678-9012-3456-7890abcdef01', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:16', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:48:37', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('d080b268-bddc-4f8a-9b5c-8e6faa376c76', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('d0edf3c9-d8dd-43ec-bc3a-75ed8720a45f', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('d1f36538-8da0-4bbd-8f12-b89230e5f8fc', '33d4e5f6-7890-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('d30afe1f-6356-4b53-bc01-42dd6c7b5244', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:36:17', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:55:22', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('d33a10f6-5a5e-4280-bcef-522850cc514f', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:24:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('d43161a2-da29-4ddc-a1f0-3c49e5fddf07', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:59', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 14:43:55', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('d4624bac-5f21-4ba2-a02b-bd3b65490d32', 'cc67890a-bcde-f123-4567-890abcdef012', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:28:29', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('deac8b98-2225-4909-b6fe-c2af94f8ec4c', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('df639bc5-ad3a-4339-9514-9c79ffbf2bde', 'abcdef98-7654-3210-fedc-ba9876543210', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:03:32', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 15:04:02', NULL, NULL, 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('e11c364d-ce6c-44ee-9d4b-0b28e52c09c7', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('e2762f42-aaed-45c5-8931-2066b5b63bf4', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('e3648da3-e177-4d47-8bf7-0dc70f143b63', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:25:26', '不同意认领', '不认领。', 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('e4d2c8b2-7c4a-48ed-82e4-b4f8bd031220', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:10:53', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('e59438f9-5334-476c-8c8b-8b2ff906e86d', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'confirmed', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:25', '都是我的。', '都是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('e7ca4534-2f36-43d8-8118-620d913f2222', '771bc2de-f345-6789-0abc-def123456789', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:17:12', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('e96c5111-f560-4886-bcda-69eb1605f97b', 'dd0e8400-e29b-41d4-a716-446655440009', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:34', '更不是我的。', '更不是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('e98f5151-0729-4f28-addb-a326513650bf', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('ebdd20ee-a720-4d28-a7ce-9072fd5649d1', 'aa456789-0abc-def1-2345-67890abcdef0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:22', 'user1_002', '李四', 'pending_claimed', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('ec247ef4-21ef-4f53-a1d6-779dfd9e34b9', 'dd7890ab-cdef-1234-5678-90abcdef0123', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 16:29:24', 'user1_002', '李四', 'pending_claimed', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('eca5589e-e96e-4206-8c3a-31ec44727189', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:17:12', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('ed2e248c-03f4-4650-a697-bf492b79ab70', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:16:47', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:34', '更不是我的。', '更不是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('ee0ca825-9041-4dd7-9bb1-eee25477896f', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:16:20', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 15:16:45', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('efb4787a-ec79-4c49-a05e-a13380ed4fde', '00a1b2c3-d4e5-f678-9012-34567890abcd', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:47', 'user1_002', '李四', 'not_claimed', '911800001000556000', '统计局', '2025-10-31 14:55:36', '不同意认领', '测试不认领', 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('f17e76d0-c198-4452-a157-262e348b919c', '11b2c3d4-e5f6-7890-1234-567890abcdef', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:54', 'user1_002', '李四', 'claimed', '911800001000556000', '统计局', '2025-10-31 14:42:54', '同意认领', NULL, 'user2_002', '钱七');
INSERT INTO `confirmation_task` VALUES ('f3436e35-49a1-4355-8218-707d8d970531', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:34:52', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 14:48:00', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('f396f56a-c3fb-4834-b1a7-1e5dd88f5d65', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 14:17:12', 'user1_002', '李四', 'rejected', '11100000000014445H', '国家税务总局', '2025-10-31 14:21:34', '更不是我的。', '更不是我的。', 'user1_003', '王五');
INSERT INTO `confirmation_task` VALUES ('f479400a-3506-4d78-9109-e904141a37ae', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:14', 'user1_002', '李四', 'not_claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:11:59', '不同意认领', '不认领。', 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('f690d8d1-1a68-47d9-89e9-14f8b97d8fa3', '98765432-1abc-def0-9876-543210abcdef', 'confirmation_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:15:13', 'user1_002', '李四', 'pending', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('f6ab3973-4fb4-447d-a498-d165897ad78f', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:10:15', 'user1_002', '李四', 'claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:13:05', '同意认领', NULL, 'user3_003', '郑十一');
INSERT INTO `confirmation_task` VALUES ('f7ca5b0e-f609-4f6a-95c6-3e2459865d04', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:35:21', 'user1_002', '李四', 'pending_claimed', '911800001000556000', '统计局', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `confirmation_task` VALUES ('f88b699f-7f90-49c5-848a-7cd137f96fef', '882def34-5678-90ab-cdef-1234567890ab', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'claimed', '914500001000889000', '经济和信息化局', '2025-10-31 15:24:51', '同意认领', NULL, 'user3_001', '周九');
INSERT INTO `confirmation_task` VALUES ('fda6c9ef-dc9d-4fe9-831c-4035292c838f', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', 'claim_task', '913900001000334000', '国家数据共享主管部门', '2025-10-31 15:23:09', 'user1_002', '李四', 'not_claimed', '111000000000132664', '中华人民共和国农业农村部', '2025-10-31 15:25:26', '不同意认领', '不认领。', 'user3_003', '郑十一');

-- ----------------------------
-- Table structure for data_element_attachment
-- ----------------------------
DROP TABLE IF EXISTS `data_element_attachment`;
CREATE TABLE `data_element_attachment`  (
  `attachfileid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元ID',
  `attachfiletype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件类型: standardfile(标准文件), examplefile(样例文件)',
  `attachfilename` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件文件名称',
  `attachfileurl` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件文件地址',
  `attachfilelocation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件文件存储位置',
  PRIMARY KEY (`attachfileid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元附件类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_element_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for data_element_attribute
-- ----------------------------
DROP TABLE IF EXISTS `data_element_attribute`;
CREATE TABLE `data_element_attribute`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元ID',
  `attributename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名称',
  `attributevalue` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_element_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for data_element_catalog_relation
-- ----------------------------
DROP TABLE IF EXISTS `data_element_catalog_relation`;
CREATE TABLE `data_element_catalog_relation`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联关系数据行ID',
  `data_element_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元ID',
  `catalog_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录的唯一标识',
  `catalog_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录的名称',
  `catalog_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录描述（摘要）',
  `info_item_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信息项ID',
  `info_item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信息项名称',
  `Info_item_datatype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信息项类型',
  `catalog_unit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录单位统一社会信用代码',
  `catalog_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录单位名称',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人账号',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元-目录关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_element_catalog_relation
-- ----------------------------
INSERT INTO `data_element_catalog_relation` VALUES ('11fbabc5-d70b-42e2-9543-b575aaa70703', '222e8400-e29b-41d4-a716-446655440013', 'd5f6549e-9b36-42ae-993e-3b18cc968636', '2025年10月29日测试目录-经济', '用于测试一数一源', '570e268e-95f6-4c46-a3ac-9d71ecb8e83a', '存出资本保证金期末余额', '逻辑型L', '914500001000889000', '经济和信息化局', '2025-10-31 14:29:19', 'user1_002', NULL, NULL);
INSERT INTO `data_element_catalog_relation` VALUES ('19de82e8-247d-45ac-81df-523aefbb550b', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', '2adbbb65ea754e53899d2c985e24be30', '测试1106', '从网上', '4eb7bcef801a46818946d530aa78e575', '年龄', '数值型N', '11100000000019692W', '工业和信息化部', '2025-10-31 14:38:17', 'user1_002', NULL, NULL);
INSERT INTO `data_element_catalog_relation` VALUES ('23d4b0d0-8a38-4a0f-beff-b1fee21a53bf', '222e8400-e29b-41d4-a716-446655440013', '4f9d22d8-38a3-48a1-92d3-b1d184ad5909', '2025年10月29日测试目录-经济', '用于测试一数一源', '721f8fcd-9031-4b4d-87cc-6c270fd04b9b', '存出保证金期末余额', '逻辑型L', '914500001000889000', '经济和信息化局', '2025-10-31 14:31:26', 'user1_002', NULL, NULL);
INSERT INTO `data_element_catalog_relation` VALUES ('36fdbfa6-9275-42f4-a7b6-86d6aef76a0a', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '2adbbb65ea754e53899d2c985e24be30', '测试1106', '从网上', '4eb7bcef801a46818946d530aa78e575', '年龄', '数值型N', '11100000000019692W', '工业和信息化部', '2025-10-31 14:57:44', 'user1_002', NULL, NULL);
INSERT INTO `data_element_catalog_relation` VALUES ('bd86bb49-de2b-4706-a803-8576610f7d42', '222e8400-e29b-41d4-a716-446655440013', '4f9d22d8-38a3-48a1-92d3-b1d184ad5909', '2025年10月29日测试目录-经济', '用于测试一数一源', '2ab587ca-fa3c-4cc6-8246-db24992f1634', '存货金额', '逻辑型L', '914500001000889000', '经济和信息化局', '2025-10-31 14:32:24', 'user1_002', NULL, NULL);
INSERT INTO `data_element_catalog_relation` VALUES ('bf15763e-0aac-4da6-b830-11cb66c0c356', '222e8400-e29b-41d4-a716-446655440013', '2adbbb65ea754e53899d2c985e24be30', '测试1106', '从网上', '4eb7bcef801a46818946d530aa78e575', '年龄', '数值型N', '11100000000019692W', '工业和信息化部', '2025-10-31 16:28:01', 'user1_002', NULL, NULL);

-- ----------------------------
-- Table structure for data_element_category
-- ----------------------------
DROP TABLE IF EXISTS `data_element_category`;
CREATE TABLE `data_element_category`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元id',
  `labelvalue` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签值: 分类字符串',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_element_category
-- ----------------------------

-- ----------------------------
-- Table structure for data_element_contact
-- ----------------------------
DROP TABLE IF EXISTS `data_element_contact`;
CREATE TABLE `data_element_contact`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元ID',
  `contactname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contacttel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元标准联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_element_contact
-- ----------------------------
INSERT INTO `data_element_contact` VALUES ('5730d83a-5698-4eb8-bbdb-4c741ae04816', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', '王芳', '13800000000');
INSERT INTO `data_element_contact` VALUES ('68eeca94-6193-45d6-8da1-2dcbf3bfded7', 'dd0e8400-e29b-41d4-a716-446655440009', '陈刚', '13800000000');
INSERT INTO `data_element_contact` VALUES ('8e276fbf-0483-4011-a62c-e9cbbd4a670e', '222e8400-e29b-41d4-a716-446655440013', '王芳', '13800000000');
INSERT INTO `data_element_contact` VALUES ('d22e1336-36aa-461d-9dd2-49e2620420f7', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', '刘丽', '13800000000');
INSERT INTO `data_element_contact` VALUES ('dfa9e1ac-9473-4f4d-899d-16939609952e', 'bb0e8400-e29b-41d4-a716-446655440007', '陈刚', '13800000000');
INSERT INTO `data_element_contact` VALUES ('f86f2a16-c820-48ed-80df-4603566cb684', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', '李明', '13800000000');

-- ----------------------------
-- Table structure for domain_data_element
-- ----------------------------
DROP TABLE IF EXISTS `domain_data_element`;
CREATE TABLE `domain_data_element`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据行唯一标识',
  `data_element_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元名称',
  `definition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据元定义描述',
  `datatype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `data_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据格式',
  `value_domain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '值域',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `source_unit_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提供单位统一社会信用代码',
  `source_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提供单位名称',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元数据行标识',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人账号',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '领域数据元表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domain_data_element
-- ----------------------------
INSERT INTO `domain_data_element` VALUES ('002fe02c-29fa-4341-8f0b-dfe8a3b42557', 'DE00140', '负债合计金额', '本项=借入款项金额+应付款项金额+应付工资金额+未交税金金额', '数值型', 'n..20', '1-1000', '负债合计金额', '111000000000132664', '中华人民共和国农业农村部', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('008e00f5-536d-42db-abc6-1cbac78b3326', 'DE00144', '负债及业主权益总计金额', '本项=负债合计金额+业主权益合计金额', '数值型', 'n..20', '1-1000', '负债及业主权益总计金额', '914500001000889000', '经济和信息化局', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('023318dd-741a-4654-beb2-1ddbb939caba', 'DE00121', '短期投资年末数', '短期投资年末数', '数值型', 'n..20', '1-1000', '短期投资年末数', '11100000000014445H', '国家税务总局', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('057cc2a4-74f5-4332-a6bd-dd9689840ca5', 'DE0006', '筹资活动现金金额流出小计', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '914500001000889000', '经济和信息化局', '55f67890-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('08816f0c-2865-410f-b780-b714de18d055', 'DE00131', '返售业务资金净增加额本期金额', '返售业务资金净增加额本期金额', '数值型', 'n..20', '1-1000', '返售业务资金净增加额本期金额', '914500001000889000', '经济和信息化局', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('0fad8e8c-4200-42e8-a05f-841c2a084fb0', 'DE00145', '负债期末数合计', '负债期末数合计=流动负债期末数+长期负债合计期末数+受托代理负债期末数', '数值型', 'n..20', '1-1000', '负债期末数合计', '111000000000132664', '中华人民共和国农业农村部', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('119287eb-587a-49d7-bf77-ceb5c3b3670d', 'DE00147', '负债总额是否公示', '企业选择是否公示负债总额。', '数值型', 'n..20', '1-1000', '负债总额是否公示', '111000000000132664', '中华人民共和国农业农村部', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('1196d3e0-c0c9-487a-9b3e-7efaf32862f8', 'DE00111', '递延所得税负债期末余额', '递延所得税负债期末余额', '数值型', 'n..20', '1-1000', '递延所得税负债期末余额', '11100000000014445H', '国家税务总局', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('160191ee-fcab-4510-9415-85a9e4f2aa23', 'DE00128', '发放贷款和垫款期末余额', '发放贷款和垫款期末余额', '数值型', 'n..20', '1-1000', '发放贷款和垫款期末余额', '11100000000014445H', '国家税务总局', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('16df84af-9e0d-4bad-a295-f16fb61a7066', 'DE00131', '返售业务资金净增加额本期金额', '返售业务资金净增加额本期金额', '数值型', 'n..20', '1-1000', '返售业务资金净增加额本期金额', '111000000000132664', '中华人民共和国农业农村部', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('16e9f1e5-23b6-4a2c-a43b-1e1eb71f1105', 'DE0004', '筹资活动产生的现金流量净额本年金额', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '数值型', 'n..20', '1-1000', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '911800001000556000', '统计局', '33d4e5f6-7890-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('17a2ab7c-b65b-4af0-8c5d-280f33c9d652', 'DE0008', '筹资活动现金金额流入小计', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '914500001000889000', '经济和信息化局', '771bc2de-f345-6789-0abc-def123456789', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('1b631c54-f074-4fd1-972b-de7744edf706', 'DE0001', '持有待售资产期末余额', '持有待售资产期末余额', '数值型', 'n..20', '1-1000', '持有待售资产期末余额', '911800001000556000', '统计局', '00a1b2c3-d4e5-f678-9012-34567890abcd', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('1d107082-ad26-48fc-aeb0-1094e1a75620', 'DE00148', '个人所得税', '报告年度企业（机构）实际缴纳的个人所得税。', '数值型', 'n..20', '1-1000', '个人所得税', '911800001000556000', '统计局', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('1ef03838-d209-4f75-a721-bec65e4c32d9', 'DE00150', '工程物资期末数', '工程物资期末数', '数值型', 'n..20', '1-1000', '工程物资期末数', '911800001000556000', '统计局', 'f4e3d2c1-b0a9-8765-4321-0fedcba98765', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('1fef4a40-b0ba-4477-8e8c-461a77ab9b2f', 'DE00134', '非限定性净资产期末数', '非限定性净资产期末数', '数值型', 'n..20', '1-1000', '非限定性净资产期末数', '914500001000889000', '经济和信息化局', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('268e8ec4-697a-431c-beec-148349a7403e', 'DE00141', '负债和净资产总计期末数', '负债和净资产期末数总计=负债期末数合计+净资产期末数合计', '数值型', 'n..20', '1-1000', '负债和净资产总计期末数', '111000000000132664', '中华人民共和国农业农村部', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('270285a9-dc2d-4faa-ad50-64a4c1896c66', 'DE00147', '负债总额是否公示', '企业选择是否公示负债总额。', '数值型', 'n..20', '1-1000', '负债总额是否公示', '914500001000889000', '经济和信息化局', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('285c4c79-5b0c-4930-ac45-bfd69efab00c', 'DE0007', '保户质押贷款期末余额', '保户质押贷款期末余额', '数值型', 'n..20', '1-1000', '保户质押贷款期末余额', '11100000000014445H', '国家税务总局', '222e8400-e29b-41d4-a716-446655440013', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2a25ea31-4ef9-4794-956b-8dda3bb349f4', 'DE00151', '工程物资期末余额', '工程物资期末余额', '数值型', 'n..20', '1-1000', '工程物资期末余额', '911800001000556000', '统计局', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2a2d3c53-e07b-4a5c-8130-4eacc76628db', 'DE0010', '筹资活动现金流入小计本期金额', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '911800001000556000', '统计局', '99345678-90ab-cdef-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2d34fe17-628e-48e5-a6ce-40e5828a940d', 'DE0006', '筹资活动现金金额流出小计', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '111000000000132664', '中华人民共和国农业农村部', '55f67890-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2d48ef02-efc1-4c2e-bc78-10479328dca4', 'DE00137', '分出保费本期金额', '分出保费本期金额', '数值型', 'n..20', '1-1000', '分出保费本期金额', '911800001000556000', '统计局', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2f3c53d3-014a-4ef6-81cf-a358df68503d', 'DE0007', '筹资活动现金金额流量净额', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '数值型', 'n..20', '1-1000', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '914500001000889000', '经济和信息化局', '660ab1c2-def3-4567-890a-bcdef1234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('2ff1e9c2-6495-4e48-ac84-81dc3c334dab', 'DE00127', '对外投资支付的现金本年金额', '对外投资支付的现金本年金额', '数值型', 'n..20', '1-1000', '对外投资支付的现金本年金额', '11100000000014445H', '国家税务总局', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('327fc425-f0fd-4a2b-9884-ae00bd8b3b35', 'DE0008', '筹资活动现金金额流入小计', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '111000000000132664', '中华人民共和国农业农村部', '771bc2de-f345-6789-0abc-def123456789', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('32d7d149-c51a-4f19-b271-12ddabe9c616', 'DE0004', '保单红利支出本期金额', '保单红利支出本期金额', '数值型', 'n..20', '1-1000', '保单红利支出本期金额', '11100000000014445H', '国家税务总局', 'bb0e8400-e29b-41d4-a716-446655440007', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('34c383e2-7d6b-4bdc-adff-e110248b7a2c', 'DE0013', '存出保证金期末余额', '存出保证金期末余额', '数值型', 'n..20', '1-1000', '存出保证金期末余额', '911800001000556000', '统计局', 'cc67890a-bcde-f123-4567-890abcdef012', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('35740598-0e7f-47e5-a3e6-0e719e052f0f', 'DE00150', '工程物资期末数', '工程物资期末数', '数值型', 'n..20', '1-1000', '工程物资期末数', '914500001000889000', '经济和信息化局', 'f4e3d2c1-b0a9-8765-4321-0fedcba98765', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('35f40d51-1061-4f05-823b-c451083182f1', 'DE00146', '负债与净资产总计期末数', '负债与净资产总计期末数=负债合计期末数+净资产合计期末数', '数值型', 'n..20', '1-1000', '负债与净资产总计期末数', '911800001000556000', '统计局', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('377c9c83-f598-435a-987b-65372955944f', 'DE0005', '保户储金及投资款净增加额本期金额', '保户储金及投资款净增加额本期金额', '数值型', 'n..20', '1-1000', '保户储金及投资款净增加额本期金额', '11100000000014445H', '国家税务总局', 'dd0e8400-e29b-41d4-a716-446655440009', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('3b815f5d-d009-4f53-aad1-38dc3cd8e8b2', 'DE0003', '办理使用登记特种设备总台（套）数', '特种设备企业办理使用登记特种设备总台（套）数 。', '数值型', 'n..12', '1-1000', '特种设备企业办理使用登记特种设备总台（套）数 。', '11100000000014445H', '国家税务总局', '990e8400-e29b-41d4-a716-446655440005', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('3c694900-766f-4636-8f9d-59820d018070', 'DE0012', '从业人数', '指从事某项具体工作的人员数量。', '数值型', 'n..12', '1-1000', '指从事某项具体工作的人员数量。', '914500001000889000', '经济和信息化局', 'bb567890-abcd-ef12-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('3ce04892-a259-46ee-afd6-cc82079bc226', 'DE0017', '存货金额', '存货金额', '数值型', 'n..20', '1-1000', '存货金额', '914500001000889000', '经济和信息化局', '00abcdef-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('3db9cc9e-47dd-4f55-9047-44bb4587b21c', 'DE0003', '筹资活动产生的现金流量净额', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '数值型', 'n..20', '1-1000', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '111000000000132664', '中华人民共和国农业农村部', '22c3d4e5-f678-9012-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('41f4599a-e917-490c-800c-11b10da83104', 'DE0002', '持有至到期投资期末余额', '持有至到期投资期末余额', '数值型', 'n..20', '1-1000', '持有至到期投资期末余额', '914500001000889000', '经济和信息化局', '11b2c3d4-e5f6-7890-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('45111c0b-0c14-40fc-ac63-dffde8f1555a', 'DE0012', '从业人数', '指从事某项具体工作的人员数量。', '数值型', 'n..12', '1-1000', '指从事某项具体工作的人员数量。', '111000000000132664', '中华人民共和国农业农村部', 'bb567890-abcd-ef12-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('46b14bc8-88f4-4d58-8d69-617a84fdfa00', 'DE00142', '负债和净资产总计期末余额', '负债期末余额合计+净资产合计期末余额', '数值型', 'n..20', '1-1000', '负债和净资产总计期末余额', '911800001000556000', '统计局', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('48ca88b5-6dc3-46f0-b1fd-f3e63c312336', 'DE00129', '发行债券收到的现金本期金额', '发行债券收到的现金本期金额', '数值型', 'n..20', '1-1000', '发行债券收到的现金本期金额', '911800001000556000', '统计局', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('48e2c911-a106-46e0-beae-b0441eda9eec', 'DE0003', '筹资活动产生的现金流量净额', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '数值型', 'n..20', '1-1000', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '914500001000889000', '经济和信息化局', '22c3d4e5-f678-9012-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('4ac741ce-2624-4c28-96b9-2d948ec68d23', 'DE00141', '负债和净资产总计期末数', '负债和净资产期末数总计=负债期末数合计+净资产期末数合计', '数值型', 'n..20', '1-1000', '负债和净资产总计期末数', '914500001000889000', '经济和信息化局', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('4b431faa-a236-4765-a7c9-c1f8c330661f', 'DE0009', '筹资活动现金流出小计本期金额', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '111000000000132664', '中华人民共和国农业农村部', '882def34-5678-90ab-cdef-1234567890ab', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('4dc91690-058e-4aac-bfa6-00a1568eac24', 'DE0007', '筹资活动现金金额流量净额', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '数值型', 'n..20', '1-1000', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '111000000000132664', '中华人民共和国农业农村部', '660ab1c2-def3-4567-890a-bcdef1234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('50346ee0-3f51-4bba-a4cc-775faebb95e8', 'DE00149', '个人所得税金额', '个人所得税金额', '数值型', 'n..20', '1-1000', '个人所得税金额', '911800001000556000', '统计局', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('50b8f24d-7755-4321-97d6-d92e6f731586', 'DE00138', '分配利润支付的现金本年累计金额', '分配利润支付的现金本年累计金额', '数值型', 'n..20', '1-1000', '分配利润支付的现金本年累计金额', '911800001000556000', '统计局', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('533c6714-802f-4a03-8998-e56a79625a43', 'DE0005', '筹资活动的现金流出小计本年金额', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '数值型', 'n..20', '1-1000', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '914500001000889000', '经济和信息化局', '44e5f678-9012-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('570e4482-1ecd-4afb-a69c-23c822ae5f2e', 'DE0007', '筹资活动现金金额流量净额', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '数值型', 'n..20', '1-1000', '本项=筹资活动现金金额流入小计-筹资活动现金金额流出小计', '911800001000556000', '统计局', '660ab1c2-def3-4567-890a-bcdef1234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('59358890-92ab-49c4-b39e-719ba7d3d730', 'DE0008', '筹资活动现金金额流入小计', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=借款所收到的现金金额+收到的其他与筹资活动有关的现金金额', '911800001000556000', '统计局', '771bc2de-f345-6789-0abc-def123456789', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('59e10788-2570-468a-98c6-397d1870a92a', 'DE00149', '个人所得税金额', '个人所得税金额', '数值型', 'n..20', '1-1000', '个人所得税金额', '914500001000889000', '经济和信息化局', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('5b4a0098-d772-4042-a067-228cdcb8665f', 'DE00130', '反向投资股权比例', '企业拥有的境外投资者的股权投资额在境外投资者所有股权中所占的比例。', '数值型', 'n..20', '1-1000', '反向投资股权比例', '111000000000132664', '中华人民共和国农业农村部', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('5eda32e1-9c5c-4452-aea7-30325b7a121f', 'DE00133', '非流动资产期末余额', '非流动资产期末余额', '数值型', 'n..20', '1-1000', '非流动资产期末余额', '911800001000556000', '统计局', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('5f19f1e0-b8c1-42dc-8b0d-8829c727268f', 'DE00122', '短期投资期末数', '短期投资期末数', '数值型', 'n..20', '1-1000', '短期投资期末数', '11100000000014445H', '国家税务总局', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3c4', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('5f3e156e-b251-4609-b4c1-7a1f92102268', 'DE0010', '保障性住房净值期末余额', '保障性住房期末余额-保障性住房累计折旧期末余额', '数值型', 'n..20', '1-1000', '保障性住房期末余额-保障性住房累计折旧期末余额', '11100000000014445H', '国家税务总局', '888e8400-e29b-41d4-a716-446655440019', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('675f37f0-73e6-4817-8ee6-4183ac1dfca5', 'DE00134', '非限定性净资产期末数', '非限定性净资产期末数', '数值型', 'n..20', '1-1000', '非限定性净资产期末数', '911800001000556000', '统计局', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6983f1fc-9e8f-4671-9d0d-37da30892ce0', 'DE00132', '非流动负债合计期末余额', '长期借款期末余额+长期应付款期末余额+预计负债期末余额+其他非流动负债期末余额', '数值型', 'n..20', '1-1000', '非流动负债合计期末余额', '111000000000132664', '中华人民共和国农业农村部', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6af40b9d-18a8-4ff2-bc3d-e415f4096c37', 'DE0016', '存放中央银行和同业款项净增加额本期金额', '存放中央银行和同业款项净增加额本期金额', '数值型', 'n..20', '1-1000', '存放中央银行和同业款项净增加额本期金额', '911800001000556000', '统计局', 'ff90abcd-ef12-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6bd94f85-4d94-427a-9fe6-4da9b7be26a6', 'DE0018', '大学及以上学历（人）', '企业的大学及以上学历人数。', '数值型', 'n..13', '1-1000', '企业的大学及以上学历人数。', '914500001000889000', '经济和信息化局', '11bcdef0-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6d2b2d77-0195-4687-a8ec-122977fda0db', 'DE00146', '负债与净资产总计期末数', '负债与净资产总计期末数=负债合计期末数+净资产合计期末数', '数值型', 'n..20', '1-1000', '负债与净资产总计期末数', '111000000000132664', '中华人民共和国农业农村部', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6e1096c2-fe31-4c07-ae12-b767d1632194', 'DE00130', '反向投资股权比例', '企业拥有的境外投资者的股权投资额在境外投资者所有股权中所占的比例。', '数值型', 'n..20', '1-1000', '反向投资股权比例', '914500001000889000', '经济和信息化局', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('6e43e7a5-7db7-4f47-a00c-990ac53ef4f3', 'DE00123', '短期投资期末余额', '短期投资期末余额', '数值型', 'n..20', '1-1000', '短期投资期末余额', '11100000000014445H', '国家税务总局', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7086f0c6-b9a9-45ef-9e95-d1da4d58c00f', 'DE0013', '存出保证金期末余额', '存出保证金期末余额', '数值型', 'n..20', '1-1000', '存出保证金期末余额', '914500001000889000', '经济和信息化局', 'cc67890a-bcde-f123-4567-890abcdef012', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('740dd9fa-80d6-4740-a317-0f8205667a7f', 'DE0001', '持有待售资产期末余额', '持有待售资产期末余额', '数值型', 'n..20', '1-1000', '持有待售资产期末余额', '111000000000132664', '中华人民共和国农业农村部', '00a1b2c3-d4e5-f678-9012-34567890abcd', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('74e41d56-7563-45d6-a035-0047c10ad0cb', 'DE00144', '负债及业主权益总计金额', '本项=负债合计金额+业主权益合计金额', '数值型', 'n..20', '1-1000', '负债及业主权益总计金额', '111000000000132664', '中华人民共和国农业农村部', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('76597bb8-6a32-41d0-8c1f-9bdc14f0d3f6', 'DE0009', '筹资活动现金流出小计本期金额', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '911800001000556000', '统计局', '882def34-5678-90ab-cdef-1234567890ab', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('77579a15-3977-492e-acb5-6e6986ca234f', 'DE00112', '递延所得税资产期末余额', '递延所得税资产期末余额', '数值型', 'n..20', '1-1000', '递延所得税资产期末余额', '11100000000014445H', '国家税务总局', 'fedcba98-7654-3210-fedc-ba9876543210', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('777e160d-767f-4cb1-b124-40eed385963d', 'DE00145', '负债期末数合计', '负债期末数合计=流动负债期末数+长期负债合计期末数+受托代理负债期末数', '数值型', 'n..20', '1-1000', '负债期末数合计', '911800001000556000', '统计局', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('77f8cfd2-43fa-458e-844c-1f232bc09d41', 'DE00147', '负债总额是否公示', '企业选择是否公示负债总额。', '数值型', 'n..20', '1-1000', '负债总额是否公示', '911800001000556000', '统计局', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('796710f2-ff1d-4095-ad7d-477032f44d48', 'DE00135', '分保费收入本期金额', '分保费收入本期金额', '数值型', 'n..20', '1-1000', '分保费收入本期金额', '911800001000556000', '统计局', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7a9cc0a3-c9a1-4946-a649-ab0aa012f059', 'DE00129', '发行债券收到的现金本期金额', '发行债券收到的现金本期金额', '数值型', 'n..20', '1-1000', '发行债券收到的现金本期金额', '914500001000889000', '经济和信息化局', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7cf6201a-3f66-4cc4-b0ed-d3e03170205f', 'DE00140', '负债合计金额', '本项=借入款项金额+应付款项金额+应付工资金额+未交税金金额', '数值型', 'n..20', '1-1000', '负债合计金额', '914500001000889000', '经济和信息化局', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7d1692dd-90ed-4f8b-bccf-0866f08c4e02', 'DE00133', '非流动资产期末余额', '非流动资产期末余额', '数值型', 'n..20', '1-1000', '非流动资产期末余额', '111000000000132664', '中华人民共和国农业农村部', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7d7502dc-815d-4ee9-b569-7ae60aeaec6a', 'DE0001', '持有待售资产期末余额', '持有待售资产期末余额', '数值型', 'n..20', '1-1000', '持有待售资产期末余额', '914500001000889000', '经济和信息化局', '00a1b2c3-d4e5-f678-9012-34567890abcd', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('7f43222d-3641-43c7-9826-5496cdbdbb5e', 'DE00129', '发行债券收到的现金本期金额', '发行债券收到的现金本期金额', '数值型', 'n..20', '1-1000', '发行债券收到的现金本期金额', '111000000000132664', '中华人民共和国农业农村部', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d2', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8173929b-81f7-48bd-9966-f609feb569e3', 'DE00130', '反向投资股权比例', '企业拥有的境外投资者的股权投资额在境外投资者所有股权中所占的比例。', '数值型', 'n..20', '1-1000', '反向投资股权比例', '911800001000556000', '统计局', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('820c50a1-b9cb-4867-8804-a4e2c174d3a1', 'DE00135', '分保费收入本期金额', '分保费收入本期金额', '数值型', 'n..20', '1-1000', '分保费收入本期金额', '914500001000889000', '经济和信息化局', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('83672aba-518a-46c1-8a54-ec61b0c544ac', 'DE00146', '负债与净资产总计期末数', '负债与净资产总计期末数=负债合计期末数+净资产合计期末数', '数值型', 'n..20', '1-1000', '负债与净资产总计期末数', '914500001000889000', '经济和信息化局', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('84b33a71-7510-4c11-9e25-d77fcc6e6fb2', 'DE00117', '独立账户资产期末余额', '独立账户资产期末余额', '数值型', 'n..20', '1-1000', '独立账户资产期末余额', '11100000000014445H', '国家税务总局', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d6', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('87ef9a0a-4658-48a8-8827-92af23075b84', 'DE0006', '筹资活动现金金额流出小计', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '数值型', 'n..20', '1-1000', '本项=偿还借款所支付的现金金额+偿付利息所支付的现金金额+支付的其他与筹资活动有关的现金金额', '911800001000556000', '统计局', '55f67890-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('898b0134-37f5-465c-a8e3-d0b23f9fa460', 'DE0005', '筹资活动的现金流出小计本年金额', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '数值型', 'n..20', '1-1000', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '911800001000556000', '统计局', '44e5f678-9012-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8b1a21f5-82b5-4039-9f5f-cf961f617fec', 'DE0005', '筹资活动的现金流出小计本年金额', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '数值型', 'n..20', '1-1000', '本项=偿还借款支付的现金本年金额+偿还利息支付的现金本年金额+支付的其他与筹资活动有关的现金本年金额', '111000000000132664', '中华人民共和国农业农村部', '44e5f678-9012-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8c40914b-e94b-4523-8d57-2f72e9efba9d', 'DE0016', '存放中央银行和同业款项净增加额本期金额', '存放中央银行和同业款项净增加额本期金额', '数值型', 'n..20', '1-1000', '存放中央银行和同业款项净增加额本期金额', '914500001000889000', '经济和信息化局', 'ff90abcd-ef12-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8c75ce86-d8cb-4ed5-a4c5-26e46122f57b', 'DE0011', '初始建设投资金额', '是指该项目首次建成通车时的原始投资。不含通车之后，新发生的改扩建或其他工程投资，主要用于识别该项目是否发生过改扩建。', '数值型', 'n', '1-1000', '', '914500001000889000', '经济和信息化局', 'aa456789-0abc-def1-2345-67890abcdef0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8edd960e-6d99-40b4-86aa-81fc6d92a212', 'DE00131', '返售业务资金净增加额本期金额', '返售业务资金净增加额本期金额', '数值型', 'n..20', '1-1000', '返售业务资金净增加额本期金额', '911800001000556000', '统计局', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('8fba3c51-1d53-4cb9-bc17-b633964ae935', 'DE0004', '筹资活动产生的现金流量净额本年金额', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '数值型', 'n..20', '1-1000', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '111000000000132664', '中华人民共和国农业农村部', '33d4e5f6-7890-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('916c536c-093e-4b8d-bb5a-fd427bdee40a', 'DE0014', '存出资本保证金期末余额', '存出资本保证金期末余额', '数值型', 'n..20', '1-1000', '存出资本保证金期末余额', '914500001000889000', '经济和信息化局', 'dd7890ab-cdef-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9287385c-822f-4162-b91f-ebeeec69125e', 'DE00114', '电子邮箱', '经营主体或自然人的电子邮箱。', '数值型', 'n..20', '1-1000', '电子邮箱', '11100000000014445H', '国家税务总局', 'abcdef98-7654-3210-fedc-ba9876543210', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('93e282de-c0a7-41d2-b92c-e5a765a9fd9d', 'DE0006', '保户储金及投资款期末余额', '保户储金及投资款期末余额', '数值型', 'n..20', '1-1000', '保户储金及投资款期末余额', '11100000000014445H', '国家税务总局', 'ff0e8400-e29b-41d4-a716-446655440011', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9468bd29-4416-4d2e-acd6-fd5f3db211ff', 'DE00113', '电子邮件', '经营主体或自然人的电子邮箱。', '数值型', 'n..20', '1-1000', '电子邮件', '11100000000014445H', '国家税务总局', '98765432-1abc-def0-9876-543210abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('948954f2-a2f9-40f7-8061-407867028fa2', 'DE00119', '短期借款期末数', '短期借款期末数', '数值型', 'n..20', '1-1000', '短期借款期末数', '11100000000014445H', '国家税务总局', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('97604572-f323-4dba-865f-08a383dd710a', 'DE00143', '负债和所有者权益总计年末数', '负债合计年末数+所有者权益合计年末数', '数值型', 'n..20', '1-1000', '负债和所有者权益总计年末数', '914500001000889000', '经济和信息化局', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('97f78284-92e6-4d77-a8e4-23b10b4ff553', 'DE0015', '存放同业款项期末余额', '存放同业款项期末余额', '数值型', 'n..20', '1-1000', '存放同业款项期末余额', '914500001000889000', '经济和信息化局', 'ee890abc-def1-2345-6789-0abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('988dd0e0-1ae6-476b-8ca1-787d1766c89a', 'DE00124', '对外担保标识ID', '对外担保标识ID', '数值型', 'n..20', '1-1000', '对外担保标识ID', '11100000000014445H', '国家税务总局', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('98f72341-04be-4a32-bf49-af97b9a85290', 'DE00145', '负债期末数合计', '负债期末数合计=流动负债期末数+长期负债合计期末数+受托代理负债期末数', '数值型', 'n..20', '1-1000', '负债期末数合计', '914500001000889000', '经济和信息化局', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('996b1c1e-05e0-4201-8f9b-4d49af000771', 'DE00135', '分保费收入本期金额', '分保费收入本期金额', '数值型', 'n..20', '1-1000', '分保费收入本期金额', '111000000000132664', '中华人民共和国农业农村部', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9a53d11e-32ba-4f1f-b421-bf13cad1050b', 'DE00142', '负债和净资产总计期末余额', '负债期末余额合计+净资产合计期末余额', '数值型', 'n..20', '1-1000', '负债和净资产总计期末余额', '111000000000132664', '中华人民共和国农业农村部', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9d44f76f-d5e7-456c-86e0-04794154f491', 'DE0010', '筹资活动现金流入小计本期金额', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '111000000000132664', '中华人民共和国农业农村部', '99345678-90ab-cdef-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9e59b6e6-0ec2-42f3-9f22-b99aa56d3829', 'DE00143', '负债和所有者权益总计年末数', '负债合计年末数+所有者权益合计年末数', '数值型', 'n..20', '1-1000', '负债和所有者权益总计年末数', '911800001000556000', '统计局', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('9ff85dd3-85ea-4427-902a-6e876b66324d', 'DE00149', '个人所得税金额', '个人所得税金额', '数值型', 'n..20', '1-1000', '个人所得税金额', '111000000000132664', '中华人民共和国农业农村部', '8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e40', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('a2b594a7-fbee-4e07-a31b-7dcffaa0b69c', 'DE00136', '分保费用本期金额', '分保费用本期金额', '数值型', 'n..20', '1-1000', '分保费用本期金额', '111000000000132664', '中华人民共和国农业农村部', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('a411a41f-9557-483d-ab01-8fa14605ebf9', 'DE00115', '定期存款期末余额', '定期存款期末余额', '数值型', 'n..20', '1-1000', '定期存款期末余额', '11100000000014445H', '国家税务总局', '01234567-89ab-cdef-0123-456789abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('a4518ff4-9e88-4708-bce7-66c401d63b22', 'DE00137', '分出保费本期金额', '分出保费本期金额', '数值型', 'n..20', '1-1000', '分出保费本期金额', '914500001000889000', '经济和信息化局', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('a7103a34-e1ef-4517-913f-442b4daf79a8', 'DE0012', '从业人数', '指从事某项具体工作的人员数量。', '数值型', 'n..12', '1-1000', '指从事某项具体工作的人员数量。', '911800001000556000', '统计局', 'bb567890-abcd-ef12-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('a970aa59-4892-498b-b270-344f48976f38', 'DE00136', '分保费用本期金额', '分保费用本期金额', '数值型', 'n..20', '1-1000', '分保费用本期金额', '914500001000889000', '经济和信息化局', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('aa4dcbca-c30a-41c3-a0f2-8c0a24580850', 'DE00134', '非限定性净资产期末数', '非限定性净资产期末数', '数值型', 'n..20', '1-1000', '非限定性净资产期末数', '111000000000132664', '中华人民共和国农业农村部', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ad42da8b-03c0-4c81-8465-7b318368305c', 'DE0002', '持有至到期投资期末余额', '持有至到期投资期末余额', '数值型', 'n..20', '1-1000', '持有至到期投资期末余额', '111000000000132664', '中华人民共和国农业农村部', '11b2c3d4-e5f6-7890-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ade4da6f-0eb1-40cc-8b2a-e538364530a7', 'DE0017', '存货金额', '存货金额', '数值型', 'n..20', '1-1000', '存货金额', '111000000000132664', '中华人民共和国农业农村部', '00abcdef-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('b37860c5-8f6b-47f4-9839-eec804a20b5c', 'DE00138', '分配利润支付的现金本年累计金额', '分配利润支付的现金本年累计金额', '数值型', 'n..20', '1-1000', '分配利润支付的现金本年累计金额', '111000000000132664', '中华人民共和国农业农村部', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('b586e028-dfda-4199-bfcd-d18d07ff5d76', 'DE0016', '存放中央银行和同业款项净增加额本期金额', '存放中央银行和同业款项净增加额本期金额', '数值型', 'n..20', '1-1000', '存放中央银行和同业款项净增加额本期金额', '111000000000132664', '中华人民共和国农业农村部', 'ff90abcd-ef12-3456-7890-abcdef012345', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('b6d7618e-45fa-4317-8600-fd2742b00341', 'DE00136', '分保费用本期金额', '分保费用本期金额', '数值型', 'n..20', '1-1000', '分保费用本期金额', '911800001000556000', '统计局', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d3', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('b8da0f53-dbe0-4108-bc2b-570cfb8585a6', 'DE00137', '分出保费本期金额', '分出保费本期金额', '数值型', 'n..20', '1-1000', '分出保费本期金额', '111000000000132664', '中华人民共和国农业农村部', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('bb59af4e-1fe0-4cdc-b60f-ba152467f65b', 'DE00125', '对外投资年末数', '对外投资年末数', '数值型', 'n..20', '1-1000', '对外投资年末数', '11100000000014445H', '国家税务总局', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e0f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('bcc0ef82-609c-493c-b103-32e717cdec25', 'DE00151', '工程物资期末余额', '工程物资期末余额', '数值型', 'n..20', '1-1000', '工程物资期末余额', '111000000000132664', '中华人民共和国农业农村部', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('bda5226f-cd46-4e55-9a55-0461bf04702d', 'DE00142', '负债和净资产总计期末余额', '负债期末余额合计+净资产合计期末余额', '数值型', 'n..20', '1-1000', '负债和净资产总计期末余额', '914500001000889000', '经济和信息化局', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('c06e0150-fdc7-4363-bb44-a0b252ff3601', 'DE0011', '初始建设投资金额', '是指该项目首次建成通车时的原始投资。不含通车之后，新发生的改扩建或其他工程投资，主要用于识别该项目是否发生过改扩建。', '数值型', 'n', '1-1000', '', '111000000000132664', '中华人民共和国农业农村部', 'aa456789-0abc-def1-2345-67890abcdef0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('c51345a9-91fd-4871-8ead-c05e6fc0b254', 'DE0009', '筹资活动现金流出小计本期金额', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '偿还债务支付的现金本期金额+分配股利、利润或偿付利息支付的现金本期金额+支付其他与筹资活动有关的现金本期金额', '914500001000889000', '经济和信息化局', '882def34-5678-90ab-cdef-1234567890ab', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('c8950d75-96c3-4ec1-8feb-6d1a3d7f2f0c', 'DE0014', '存出资本保证金期末余额', '存出资本保证金期末余额', '数值型', 'n..20', '1-1000', '存出资本保证金期末余额', '911800001000556000', '统计局', 'dd7890ab-cdef-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('c9ce6905-8457-4996-b056-3e0dd4929e09', 'DE00143', '负债和所有者权益总计年末数', '负债合计年末数+所有者权益合计年末数', '数值型', 'n..20', '1-1000', '负债和所有者权益总计年末数', '111000000000132664', '中华人民共和国农业农村部', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('cd5eaa97-6846-4678-8943-388ff09c33da', 'DE00140', '负债合计金额', '本项=借入款项金额+应付款项金额+应付工资金额+未交税金金额', '数值型', 'n..20', '1-1000', '负债合计金额', '911800001000556000', '统计局', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3d0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('cedff2b2-7de6-4036-8518-5f55439b6424', 'DE00141', '负债和净资产总计期末数', '负债和净资产期末数总计=负债期末数合计+净资产期末数合计', '数值型', 'n..20', '1-1000', '负债和净资产总计期末数', '911800001000556000', '统计局', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d2483fba-80f5-475a-bf0a-e1e0a96426a4', 'DE0018', '大学及以上学历（人）', '企业的大学及以上学历人数。', '数值型', 'n..13', '1-1000', '企业的大学及以上学历人数。', '111000000000132664', '中华人民共和国农业农村部', '11bcdef0-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d373c6a4-8a4b-4a2c-a88e-daf9c3f8ccad', 'DE00116', '独立账户负债期末余额', '独立账户负债期末余额', '数值型', 'n..20', '1-1000', '独立账户负债期末余额', '11100000000014445H', '国家税务总局', 'deadbeef-cafe-babe-fade-deafcafebabe', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d4bb072b-a594-4a43-b73b-af7554964ffa', 'DE00126', '对外投资所支付的现金金额', '对外投资所支付的现金金额', '数值型', 'n..20', '1-1000', '对外投资所支付的现金金额', '11100000000014445H', '国家税务总局', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6a', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d7889ae8-2994-4aca-ba43-ae26db1fcede', 'DE0002', '按所有权归属分类本期金额', '按所有权归属分类本期金额', '数值型', 'n..20', '1-1000', '按所有权归属分类本期金额', '11100000000014445H', '国家税务总局', '770e8400-e29b-41d4-a716-446655440003', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d84fb3c8-a511-43de-b485-293dfc91c844', 'DE0015', '存放同业款项期末余额', '存放同业款项期末余额', '数值型', 'n..20', '1-1000', '存放同业款项期末余额', '911800001000556000', '统计局', 'ee890abc-def1-2345-6789-0abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d8864ac0-4bc8-4a2e-a61d-347d10788b12', 'DE00133', '非流动资产期末余额', '非流动资产期末余额', '数值型', 'n..20', '1-1000', '非流动资产期末余额', '914500001000889000', '经济和信息化局', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d930dce2-0061-453f-a18f-c54209ac726c', 'DE0008', '保险合同准备金期末余额', '保险合同准备金期末余额', '数值型', 'n..20', '1-1000', '保险合同准备金期末余额', '11100000000014445H', '国家税务总局', '444e8400-e29b-41d4-a716-446655440015', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('d9cf7fd9-2e2b-47cd-8790-52f05abf39f9', 'DE00139', '分配外方股东的利润', '报告年度实际分配外方股东的利润。', '数值型', 'n..20', '1-1000', '分配外方股东的利润', '111000000000132664', '中华人民共和国农业农村部', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('daa47507-8081-452c-ac6a-6f313f3ca1c6', 'DE0018', '大学及以上学历（人）', '企业的大学及以上学历人数。', '数值型', 'n..13', '1-1000', '企业的大学及以上学历人数。', '911800001000556000', '统计局', '11bcdef0-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('db052344-a9ad-48c2-b457-86d06428b07e', 'DE00132', '非流动负债合计期末余额', '长期借款期末余额+长期应付款期末余额+预计负债期末余额+其他非流动负债期末余额', '数值型', 'n..20', '1-1000', '非流动负债合计期末余额', '911800001000556000', '统计局', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('dee0fea0-0b54-443d-9b47-309c9c57728e', 'DE0009', '保险业务收入本期金额', '保险业务收入本期金额', '数值型', 'n..20', '1-1000', '保险业务收入本期金额', '11100000000014445H', '国家税务总局', '666e8400-e29b-41d4-a716-446655440017', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('df4b369e-dd10-4e33-ada0-d3cbe3c76289', 'DE0004', '筹资活动产生的现金流量净额本年金额', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '数值型', 'n..20', '1-1000', '本项=筹资活动的现金流入小计本年金额-筹资活动的现金流出小计本年金额', '914500001000889000', '经济和信息化局', '33d4e5f6-7890-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('e0839005-726c-4051-8373-2e6cd1e2119e', 'DE00151', '工程物资期末余额', '工程物资期末余额', '数值型', 'n..20', '1-1000', '工程物资期末余额', '914500001000889000', '经济和信息化局', 'b5c6d7e8-f9a0-1234-5678-9abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('e1126fc2-f127-44af-a7f8-ba10731a11a2', 'DE00120', '短期借款期末余额', '短期借款期末余额', '数值型', 'n..20', '1-1000', '短期借款期末余额', '11100000000014445H', '国家税务总局', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3a', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('e341ecf2-4ba8-455d-ad48-3b55e39cb9ee', 'DE0017', '存货金额', '存货金额', '数值型', 'n..20', '1-1000', '存货金额', '911800001000556000', '统计局', '00abcdef-1234-5678-90ab-cdef01234567', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('e487e87c-e18c-47fd-af26-5ee6beb0edef', 'DE00118', '短期借款年末数', '短期借款年末数', '数值型', 'n..20', '1-1000', '短期借款年末数', '11100000000014445H', '国家税务总局', '7b8c9d0e-f1a2-b3c4-d5e6-e7f8a9b0c1d2', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('e7dc1a2f-e115-49f4-a93d-5f6f080d9524', 'DE00150', '工程物资期末数', '工程物资期末数', '数值型', 'n..20', '1-1000', '工程物资期末数', '111000000000132664', '中华人民共和国农业农村部', 'f4e3d2c1-b0a9-8765-4321-0fedcba98765', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ea39cc72-d136-4fd4-a087-b8b9f0388a75', 'DE00148', '个人所得税', '报告年度企业（机构）实际缴纳的个人所得税。', '数值型', 'n..20', '1-1000', '个人所得税', '111000000000132664', '中华人民共和国农业农村部', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('eb29eb48-b391-4ed3-a1a2-76933cf95c0f', 'DE0010', '筹资活动现金流入小计本期金额', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '数值型', 'n..20', '1-1000', '吸收投资收到的现金本期金额+取得借款收到的现金本期金额+发行债券收到的现金本期金额+收到其他与筹资活动有关的现金本期金额', '914500001000889000', '经济和信息化局', '99345678-90ab-cdef-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ebabd69c-3b04-45d2-8f0d-e9d82b9d766a', 'DE0013', '存出保证金期末余额', '存出保证金期末余额', '数值型', 'n..20', '1-1000', '存出保证金期末余额', '111000000000132664', '中华人民共和国农业农村部', 'cc67890a-bcde-f123-4567-890abcdef012', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ed18a39c-6564-4535-a1a6-dc6c16edc728', 'DE0015', '存放同业款项期末余额', '存放同业款项期末余额', '数值型', 'n..20', '1-1000', '存放同业款项期末余额', '111000000000132664', '中华人民共和国农业农村部', 'ee890abc-def1-2345-6789-0abcdef01234', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ee52129f-ff1c-47e5-b3c8-ab34e55a128c', 'DE00139', '分配外方股东的利润', '报告年度实际分配外方股东的利润。', '数值型', 'n..20', '1-1000', '分配外方股东的利润', '911800001000556000', '统计局', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ef209988-c1f4-44e6-b3d4-85fa2e773988', 'DE0014', '存出资本保证金期末余额', '存出资本保证金期末余额', '数值型', 'n..20', '1-1000', '存出资本保证金期末余额', '111000000000132664', '中华人民共和国农业农村部', 'dd7890ab-cdef-1234-5678-90abcdef0123', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('ef9f12a6-5a33-4c20-997c-38b0557ac8bf', 'DE00132', '非流动负债合计期末余额', '长期借款期末余额+长期应付款期末余额+预计负债期末余额+其他非流动负债期末余额', '数值型', 'n..20', '1-1000', '非流动负债合计期末余额', '914500001000889000', '经济和信息化局', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f05e44ab-5ce7-4a19-aac8-4900499515b1', 'DE0003', '筹资活动产生的现金流量净额', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '数值型', 'n..20', '1-1000', '本项=本年筹资活动产生的现金流入小计-本年筹资活动产生的现金流出小计', '911800001000556000', '统计局', '22c3d4e5-f678-9012-3456-7890abcdef01', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f3d146d4-10f2-483c-a089-60800e79387b', 'DE00148', '个人所得税', '报告年度企业（机构）实际缴纳的个人所得税。', '数值型', 'n..20', '1-1000', '个人所得税', '914500001000889000', '经济和信息化局', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f56f4bff-9ec3-4810-a160-3425edf28501', 'DE00139', '分配外方股东的利润', '报告年度实际分配外方股东的利润。', '数值型', 'n..20', '1-1000', '分配外方股东的利润', '914500001000889000', '经济和信息化局', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f5e1b245-5e24-4b07-a3f0-0f2251f56b48', 'DE00138', '分配利润支付的现金本年累计金额', '分配利润支付的现金本年累计金额', '数值型', 'n..20', '1-1000', '分配利润支付的现金本年累计金额', '914500001000889000', '经济和信息化局', '8d9e0f1a-2b3c-4d5e-6f7a-8b9c0d1e2f3b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f77517f2-566a-487c-bb4f-0d897e65a490', 'DE00144', '负债及业主权益总计金额', '本项=负债合计金额+业主权益合计金额', '数值型', 'n..20', '1-1000', '负债及业主权益总计金额', '911800001000556000', '统计局', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f84b914b-b685-488b-a880-4740d6346d3f', 'DE0002', '持有至到期投资期末余额', '持有至到期投资期末余额', '数值型', 'n..20', '1-1000', '持有至到期投资期末余额', '911800001000556000', '统计局', '11b2c3d4-e5f6-7890-1234-567890abcdef', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');
INSERT INTO `domain_data_element` VALUES ('f91eb87f-d90c-4e84-8dd5-da1300053955', 'DE0011', '初始建设投资金额', '是指该项目首次建成通车时的原始投资。不含通车之后，新发生的改扩建或其他工程投资，主要用于识别该项目是否发生过改扩建。', '数值型', 'n', '1-1000', '', '911800001000556000', '统计局', 'aa456789-0abc-def1-2345-67890abcdef0', '2024-10-27 16:13:01', 'test_account', '2024-10-27 16:13:01', 'test_account');

-- ----------------------------

-- ----------------------------
-- Table structure for flow_definition
-- ----------------------------
DROP TABLE IF EXISTS `flow_definition`;
CREATE TABLE `flow_definition`  (
  `flowid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `flowname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程名称',
  `flowdescription` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程描述',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态: 启用(Enable), 禁用(Disable)',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  PRIMARY KEY (`flowid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程配置主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_definition
-- ----------------------------
INSERT INTO `flow_definition` VALUES ('1', '定标阶段流程', '数据元定标流程', 'Enable', '2025-10-24 10:14:34', 'system', '2025-10-24 10:14:42', 'system');

-- ----------------------------
-- Table structure for flow_transfer_definition
-- ----------------------------
DROP TABLE IF EXISTS `flow_transfer_definition`;
CREATE TABLE `flow_transfer_definition`  (
  `transferid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `sourceactivityname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迁移源环节名称',
  `transfercondition` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迁移条件',
  `destactivityname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迁移目标环节名称',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  PRIMARY KEY (`transferid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程迁移配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_transfer_definition
-- ----------------------------
INSERT INTO `flow_transfer_definition` VALUES ('1', 'TodoDetermined', '提交审核', 'PendingReview', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('2', 'PendingReview', '驳回', 'TodoDetermined', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('3', 'PendingReview', '审核通过', 'SolicitingOpinions', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('4', 'SolicitingOpinions', '按意见完善', 'TodoRevised', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('5', 'TodoRevised', '提交复审', 'PendingReExamination', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('6', 'SolicitingOpinions', '报送领导审阅', 'Todoreleased', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('7', 'PendingReExamination', '报送领导审阅', 'Todoreleased', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');
INSERT INTO `flow_transfer_definition` VALUES ('8', 'Todoreleased', '发布', 'Published', '2025-10-24 10:17:15', 'system', '2025-10-24 10:17:21', 'system');

-- ----------------------------
-- Table structure for negotiation_record
-- ----------------------------
DROP TABLE IF EXISTS `negotiation_record`;
CREATE TABLE `negotiation_record`  (
  `record_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协商记录数据ID',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元唯一标识',
  `send_date` datetime NULL DEFAULT NULL COMMENT '发出日期',
  `sender_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出人用户账号',
  `sender_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发出人姓名',
  `negotiation_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '协商事宜说明',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '协商记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of negotiation_record
-- ----------------------------
INSERT INTO `negotiation_record` VALUES ('0bbd973b-b819-40af-bfd6-f7a45a906579', '00a1b2c3-d4e5-f678-9012-34567890abcd', '2025-10-31 15:07:26', 'user1_002', '李四', '协商。');
INSERT INTO `negotiation_record` VALUES ('1fce3eb0-a079-4693-b096-be4d37533c61', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7d', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('2b053c92-b7f5-40d8-ad20-9f29f96fd8ca', '11bcdef0-1234-5678-90ab-cdef01234567', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('312df114-d1e0-4222-af10-4557814006af', '1d2e3f4a-5b6c-7d8e-9f0a-1b2c3d4e5f6b', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('4810648a-f5c1-47e5-bd87-b1944e05c7ad', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e80', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('5aec608e-e9c4-482e-94d1-11021e42f47a', '55f67890-1234-5678-90ab-cdef01234567', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('63ac5dab-616d-45d7-8f38-30dd749df226', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', '2025-10-31 14:24:47', 'user1_002', '李四', '协商谁是数源单位。33');
INSERT INTO `negotiation_record` VALUES ('6707bd58-2186-4b0b-a55d-a2f00dee7e19', 'bb0e8400-e29b-41d4-a716-446655440007', '2025-10-31 14:24:47', 'user1_002', '李四', '协商谁是数源单位。33');
INSERT INTO `negotiation_record` VALUES ('6e72abd8-3753-4c14-a0b3-d1957eb052a0', 'dd0e8400-e29b-41d4-a716-446655440009', '2025-10-31 14:24:47', 'user1_002', '李四', '协商谁是数源单位。33');
INSERT INTO `negotiation_record` VALUES ('6e8c3e7f-6720-4351-b65b-b4884cb1a919', '22c3d4e5-f678-9012-3456-7890abcdef01', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('9499dd36-95e6-4fde-a1a7-acd1afe7dbb7', '222e8400-e29b-41d4-a716-446655440013', '2025-10-31 14:24:33', 'user1_002', '李四', '协商谁是数源单位。');
INSERT INTO `negotiation_record` VALUES ('9d10febb-42fc-4f88-a408-d0532b0ec2e9', '33d4e5f6-7890-1234-5678-90abcdef0123', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('a41e5ba3-38fc-4195-8798-bfee071a6874', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9e', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('a808196f-88e7-44bf-81e1-676adc7a8c6f', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8c', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('ab3ce5fb-2fe0-4593-ad6f-423277dee17b', '0e1f2a3b-4c5d-6e7f-8a9b-0c1d2e3f4a5b', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('afa8247b-b544-44f4-91a4-5401bcdf9749', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', '2025-10-31 14:24:47', 'user1_002', '李四', '协商谁是数源单位。33');
INSERT INTO `negotiation_record` VALUES ('b505b19f-03c7-4fd3-8e39-3cd8a1e61425', '00abcdef-1234-5678-90ab-cdef01234567', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('b91f9423-dbe0-410e-8f3d-826dcd9da369', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('c83d54c4-ea61-4eaa-ae2b-4d6947b15a62', '4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('cb43d15d-c343-45bd-8564-bb3e1ad96fa6', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', '2025-10-31 14:24:41', 'user1_002', '李四', '协商谁是数源单位。');
INSERT INTO `negotiation_record` VALUES ('cbc5c44f-b1be-479f-8102-ff91588ed734', '5c6d7e8f-9a0b-1c2d-3e4f-5a6b7c8d9e10', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('d923867b-4c42-47ec-920d-c620553e3d8c', '44e5f678-9012-3456-7890-abcdef012345', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('dbf8f946-8f2d-4ec8-a7bd-e5f63f9a24f2', '11b2c3d4-e5f6-7890-1234-567890abcdef', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('de8937a7-8694-4d2d-8d4b-2f7b8e95738d', '5f6a7b8c-9d0e-1f2a-3b4c-5d6e7f8a9b0c', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('e1a6db2b-2a01-49a4-870f-1fec8ebdddf1', '1a2b3c4d-5e6f-7a8b-9c0d-e1f2a3b4c5d7', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('e88fb99f-34d8-44ec-89f1-6c3131ff4c6e', '0b1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5f', '2025-10-31 15:15:49', 'user1_002', '李四', '协商数源单位。');
INSERT INTO `negotiation_record` VALUES ('f56e0256-5414-4505-92c7-24a251c94d28', '3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8f', '2025-10-31 15:15:59', 'user1_002', '李四', '协商数源单位。');

-- ----------------------------
-- Table structure for negotiation_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `negotiation_record_detail`;
CREATE TABLE `negotiation_record_detail`  (
  `record_detail_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协商参与者记录数据ID',
  `record_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协商记录数据ID',
  `negotiation_unit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协商单位统一社会信用代码',
  `negotiation_unit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协商单位名称',
  `negotiation_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '协商结果',
  `recorder_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录协商结果用户账号',
  `recorder_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录协商结果用户姓名',
  `result_date` datetime NULL DEFAULT NULL COMMENT '协商结果记录日期',
  PRIMARY KEY (`record_detail_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '协商记录明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of negotiation_record_detail
-- ----------------------------
INSERT INTO `negotiation_record_detail` VALUES ('03d6367b-28ab-4577-9256-c1ea7b20d19d', '4810648a-f5c1-47e5-bd87-b1944e05c7ad', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('04ceaabb-c7c7-46e6-bb13-39182c51a6f4', '9d10febb-42fc-4f88-a408-d0532b0ec2e9', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('0515d046-450d-427e-bdfb-0ae4665233ba', 'b91f9423-dbe0-410e-8f3d-826dcd9da369', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('095094ad-e799-4a0e-beb2-c9d4a2678604', '9499dd36-95e6-4fde-a1a7-acd1afe7dbb7', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('0af4e7ec-2d29-401f-9701-6ae49b2f6d57', 'a808196f-88e7-44bf-81e1-676adc7a8c6f', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('12a53df8-d937-46bf-99fa-72ece821bc0d', '9499dd36-95e6-4fde-a1a7-acd1afe7dbb7', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('14f87d9e-6b59-4993-95f6-5bc5aaccdded', 'c83d54c4-ea61-4eaa-ae2b-4d6947b15a62', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('164e2b27-dbba-423c-9080-da5291deb595', 'a41e5ba3-38fc-4195-8798-bfee071a6874', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1662d816-fa5b-4222-93b2-e198773e7a77', '5aec608e-e9c4-482e-94d1-11021e42f47a', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1b20641a-f545-4a0f-8ff5-b4aed5ef239b', 'a41e5ba3-38fc-4195-8798-bfee071a6874', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1b8c09d5-e2c5-4c7a-aa77-a87dc0978c92', '0bbd973b-b819-40af-bfd6-f7a45a906579', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1ba1026f-75f8-4bde-8028-d06259f7d2d4', 'ab3ce5fb-2fe0-4593-ad6f-423277dee17b', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1bf37492-7698-4ff7-bbec-e91f62d5bf97', 'e88fb99f-34d8-44ec-89f1-6c3131ff4c6e', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1db58126-be1d-4400-9e2c-628a401ec10a', 'f56e0256-5414-4505-92c7-24a251c94d28', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('1f8defa2-3359-4702-962e-9ae932929104', '9499dd36-95e6-4fde-a1a7-acd1afe7dbb7', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('2413db4f-5d40-4292-937a-cd5d0a364661', 'e1a6db2b-2a01-49a4-870f-1fec8ebdddf1', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('261fa65b-eed7-48af-b676-e47d9c02d5e9', 'e88fb99f-34d8-44ec-89f1-6c3131ff4c6e', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('2c98b23f-25a5-405b-b314-784e445b8c47', 'cb43d15d-c343-45bd-8564-bb3e1ad96fa6', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('2ccc7bef-b064-44d4-af10-b8c2dc13eb79', '1fce3eb0-a079-4693-b096-be4d37533c61', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('318e0a31-4aea-431e-961f-40f556a92013', 'ab3ce5fb-2fe0-4593-ad6f-423277dee17b', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('32f0deaa-46e4-476b-b514-d38d59b30baf', '9d10febb-42fc-4f88-a408-d0532b0ec2e9', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('3352af41-cfb8-438d-b4a7-9e27335943b5', 'cb43d15d-c343-45bd-8564-bb3e1ad96fa6', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('33dbb292-d6f8-4774-b2d4-063f7b50e0d4', '0bbd973b-b819-40af-bfd6-f7a45a906579', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('3938d2d8-db4d-4738-abc4-10fef7bd5553', '63ac5dab-616d-45d7-8f38-30dd749df226', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('3a4aa4be-e152-44e1-934d-b213ff485b8f', '2b053c92-b7f5-40d8-ad20-9f29f96fd8ca', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('3aad23f9-b456-4f42-985d-50ec4085b9a5', 'e1a6db2b-2a01-49a4-870f-1fec8ebdddf1', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('3e6e2d57-3072-4349-bf97-55e0a1ebf47e', 'd923867b-4c42-47ec-920d-c620553e3d8c', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('415805e0-0cd7-42fc-8cfe-46f3bd0e7dbd', 'b91f9423-dbe0-410e-8f3d-826dcd9da369', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('41bf8c7b-9328-4d30-84fb-ebeab9e2222e', 'b505b19f-03c7-4fd3-8e39-3cd8a1e61425', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('43da4ee2-9696-4bfe-b59a-d77db2bc4b2c', '2b053c92-b7f5-40d8-ad20-9f29f96fd8ca', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('49f79eb8-4644-42f4-bc4a-97723c8417d0', 'c83d54c4-ea61-4eaa-ae2b-4d6947b15a62', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('4be626fa-6a3a-4572-bcac-521d1f04e4b4', 'ab3ce5fb-2fe0-4593-ad6f-423277dee17b', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('4f6ddb59-b4e3-46a5-a1cc-13d84ac9f8ea', 'cbc5c44f-b1be-479f-8102-ff91588ed734', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('54e02e3b-f2a2-4953-984e-c6e7b2e6795d', '5aec608e-e9c4-482e-94d1-11021e42f47a', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('5a50d82e-eeee-43ac-9837-439c26295d8d', 'f56e0256-5414-4505-92c7-24a251c94d28', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('6d0ea91a-3a33-4416-a4a3-e972aaa69c3e', 'b505b19f-03c7-4fd3-8e39-3cd8a1e61425', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('704fc674-1652-4f9c-9c21-a7786634fb19', 'dbf8f946-8f2d-4ec8-a7bd-e5f63f9a24f2', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('7135a43a-58a8-461c-9008-327033f3d5e2', 'b505b19f-03c7-4fd3-8e39-3cd8a1e61425', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('7226b47a-153b-4b1c-bb97-a39a095867e2', '312df114-d1e0-4222-af10-4557814006af', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('7273de35-3da9-4015-9329-18d7d04677f1', '6e8c3e7f-6720-4351-b65b-b4884cb1a919', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('79de14d9-d728-4f07-85b7-bbeccdf4164e', 'a808196f-88e7-44bf-81e1-676adc7a8c6f', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('830ebcf3-c9ab-4bce-847d-26940bab0fdc', 'e1a6db2b-2a01-49a4-870f-1fec8ebdddf1', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('a6407da3-5e8d-4f06-82e3-d933aee7b6c3', '2b053c92-b7f5-40d8-ad20-9f29f96fd8ca', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('a83f5a39-fb6d-412d-b7ea-f8a4114927ae', '5aec608e-e9c4-482e-94d1-11021e42f47a', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('aa3496c1-2254-4327-ba3d-5854595518d2', 'e88fb99f-34d8-44ec-89f1-6c3131ff4c6e', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('ab994963-374c-4aa8-8d5c-a24f17b6be42', 'cbc5c44f-b1be-479f-8102-ff91588ed734', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('abc03abc-3540-4822-82cb-840381e2e92f', '1fce3eb0-a079-4693-b096-be4d37533c61', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('ac0cb047-adf4-4f8a-bbc2-44b4e5ad0b29', '6707bd58-2186-4b0b-a55d-a2f00dee7e19', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('b932c769-b556-4ed8-8cf5-6c8f6dea5d7f', 'dbf8f946-8f2d-4ec8-a7bd-e5f63f9a24f2', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('bc41a174-5a5e-408e-ac4e-9d341d62f4c7', 'd923867b-4c42-47ec-920d-c620553e3d8c', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('bc7815f4-baaf-46d9-bc8d-f23fe0286e97', '9d10febb-42fc-4f88-a408-d0532b0ec2e9', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('beeb3807-8f10-491f-81d0-fc5b7f7ed2ea', 'de8937a7-8694-4d2d-8d4b-2f7b8e95738d', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c253b9e3-792b-49a1-a774-6aec0bafbd75', 'c83d54c4-ea61-4eaa-ae2b-4d6947b15a62', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c3ca50b7-f518-4e41-b0bb-0140cbbf67cd', '312df114-d1e0-4222-af10-4557814006af', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c4225400-92dc-44d3-b663-658cfd36dafa', 'cbc5c44f-b1be-479f-8102-ff91588ed734', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c42e6099-86c8-4865-b847-2b19e77e9cef', 'afa8247b-b544-44f4-91a4-5401bcdf9749', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c4432dc1-d562-447b-9cd1-9caba6b3bba5', 'f56e0256-5414-4505-92c7-24a251c94d28', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c831e6a5-a023-47bf-8b56-a0246ce30dc1', '6e72abd8-3753-4c14-a0b3-d1957eb052a0', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c8645e0b-13ca-432e-96ee-98dd67df6aeb', '4810648a-f5c1-47e5-bd87-b1944e05c7ad', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c90fa667-272c-4a53-bd42-8a855121ccf2', 'a808196f-88e7-44bf-81e1-676adc7a8c6f', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('c956329c-2206-4e08-a91a-bec66b8df109', '0bbd973b-b819-40af-bfd6-f7a45a906579', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('cbd11121-a099-469d-aa0c-246a124d5f1b', 'a41e5ba3-38fc-4195-8798-bfee071a6874', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('cceba070-fbd2-491e-899f-17cde1cad3b2', 'dbf8f946-8f2d-4ec8-a7bd-e5f63f9a24f2', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('d1db2e4c-f4be-4eb2-8d28-bf30126507da', 'de8937a7-8694-4d2d-8d4b-2f7b8e95738d', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('d3217349-0364-488d-9ecc-bc244b6a2954', '1fce3eb0-a079-4693-b096-be4d37533c61', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('dfcc588a-09d9-4ddc-8d93-80f79d020910', 'b91f9423-dbe0-410e-8f3d-826dcd9da369', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('f16c7127-351d-4010-b00d-3985bdfeeb2b', 'd923867b-4c42-47ec-920d-c620553e3d8c', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('f1e09098-5dca-43d0-9840-257ea68ed6e1', 'de8937a7-8694-4d2d-8d4b-2f7b8e95738d', '911800001000556000', '统计局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('f2cfe741-446e-43e9-acf7-8f2cb32cf807', '312df114-d1e0-4222-af10-4557814006af', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('f44f3e05-d6e3-4adf-9c7d-3927e63db635', '4810648a-f5c1-47e5-bd87-b1944e05c7ad', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('f5797c02-3b99-44cc-bc65-8cbb9859c692', '6e8c3e7f-6720-4351-b65b-b4884cb1a919', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, NULL);
INSERT INTO `negotiation_record_detail` VALUES ('fc505396-db76-48eb-9f17-edaede2020b6', '6e8c3e7f-6720-4351-b65b-b4884cb1a919', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for organization_unit
-- ----------------------------
DROP TABLE IF EXISTS `organization_unit`;
CREATE TABLE `organization_unit`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据行ID',
  `third_party_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方ID',
  `node_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型(UNIT/REGION)',
  `unit_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码/区划代码',
  `unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称/区划名称',
  `unit_region` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区划的所属区划',
  `parent_node_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父节点ID',
  `superior_unit_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级行业主管单位ID',
  `contact_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位定源业务联系人账号',
  `contact_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位定源业务联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位定源业务联系电话',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人账号',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '单位/区划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization_unit
-- ----------------------------
INSERT INTO `organization_unit` VALUES ('a1b2c3d4-e5f6-4789-9012-34567890abc', 'DEFAULT_SOURCE', 'UNIT', '913900001000334000', '国家数据共享主管部门', NULL, NULL, NULL, 'user_a', '李明', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('a3b4c5d6-e7f8-4901-9234-ef012345678', 'DEFAULT_SOURCE', 'REGION', '120102000000', '河东区', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_m', '马超', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('a5b6c7d8-e9f0-4123-9456-abcdef01234', 'DEFAULT_SOURCE', 'REGION', '370200000000', '青岛市', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_y', '冯磊', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('a7b8c9d0-e1f2-4345-9678-90abcdef012', 'DEFAULT_SOURCE', 'REGION', '130000000000', '河北省', NULL, NULL, NULL, 'user_g', '赵勇', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('a9b0c1d2-e3f4-4567-9890-456789abcde', 'DEFAULT_SOURCE', 'REGION', '130200000000', '唐山市', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_s', '高飞', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('b0c1d2e3-f4a5-4678-9901-56789abcdef', 'DEFAULT_SOURCE', 'REGION', '130300000000', '秦皇岛市', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_t', '罗兰', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('b2c3d4e5-f6a7-4890-9123-4567890abcd', 'DEFAULT_SOURCE', 'UNIT', '914500001000889000', '经济和信息化局', NULL, NULL, NULL, 'user_b', '王芳', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('b4c5d6e7-f8a9-4012-9345-f0123456789', 'DEFAULT_SOURCE', 'REGION', '120103000000', '河西区', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_n', '胡月', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('b6c7d8e9-f0a1-4234-9567-bcdef012345', 'DEFAULT_SOURCE', 'REGION', '370300000000', '淄博市', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_z', '蔡云', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('b8c9d0e1-f2a3-4456-9789-0abcdef0123', 'DEFAULT_SOURCE', 'REGION', '370000000000', '山东省', NULL, NULL, NULL, 'user_h', '黄敏', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('c1d2e3f4-a5b6-4789-9012-6789abcdef0', 'DEFAULT_SOURCE', 'UNIT', 'TE370000370000000B', '山东政务服务', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_u', '郑义', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('c3d4e5f6-a7b8-4901-9234-567890abcde', 'DEFAULT_SOURCE', 'UNIT', '911800001000556000', '统计局', NULL, NULL, NULL, 'user_c', '张伟', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('c5d6e7f8-a9b0-4123-9456-0123456789a', 'DEFAULT_SOURCE', 'UNIT', '111310820005501000', '河北政务服务', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_o', '朱军', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('c9d0e1f2-a3b4-4567-9890-abcdef01234', 'DEFAULT_SOURCE', 'UNIT', '12100000000133049Y', '天津市气象局', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_i', '周杰', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('d0e1f2a3-b4c5-4678-9901-bcdef012345', 'DEFAULT_SOURCE', 'UNIT', 'TE1200000000000001', '天津政务服务', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_j', '吴婷', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('d2e3f4a5-b6c7-4890-9123-789abcdef01', 'DEFAULT_SOURCE', 'UNIT', '12100000004502163Q', '山东省气象局', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_v', '谢雨', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('d4e5f6a7-b8c9-4012-9345-67890abcdef', 'DEFAULT_SOURCE', 'UNIT', '11100000000014445H', '国家税务总局', NULL, NULL, NULL, 'user_d', '刘丽', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('d6e7f8a9-b0c1-4234-9567-123456789ab', 'DEFAULT_SOURCE', 'UNIT', '111000007178176000', '河北省邮政管理局', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_p', '郭霞', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('e1f2a3b4-c5d6-4789-9012-cdef0123456', 'DEFAULT_SOURCE', 'UNIT', '111000007178176000', '天津市邮政管理局', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_k', '徐涛', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('e3f4a5b6-c7d8-4901-9234-89abcdef012', 'DEFAULT_SOURCE', 'UNIT', '11100000071781751XM', '山东省邮政管理局', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_w', '宋波', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('e5f6a7b8-c9d0-4123-9456-7890abcdef0', 'DEFAULT_SOURCE', 'UNIT', '111000000000132664', '中华人民共和国农业农村部', NULL, NULL, NULL, 'user_e', '陈刚', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('e7f8a9b0-c1d2-4345-9678-23456789abc', 'DEFAULT_SOURCE', 'UNIT', '121000000002187000', '河北省气象局', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_q', '林强', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('f2a3b4c5-d6e7-4890-9123-def01234567', 'DEFAULT_SOURCE', 'REGION', '120101000000', '和平区', NULL, 'f6a7b8c9-d0e1-4234-9567-890abcdef01', NULL, 'user_l', '孙燕', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('f4a5b6c7-d8e9-4012-9345-9abcdef0123', 'DEFAULT_SOURCE', 'REGION', '370100000000', '济南市', NULL, 'b8c9d0e1-f2a3-4456-9789-0abcdef0123', NULL, 'user_x', '唐雪', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('f6a7b8c9-d0e1-4234-9567-890abcdef01', 'DEFAULT_SOURCE', 'REGION', '120000000000', '天津市', NULL, NULL, NULL, 'user_f', '杨静', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');
INSERT INTO `organization_unit` VALUES ('f8a9b0c1-d2e3-4456-9789-3456789abcd', 'DEFAULT_SOURCE', 'REGION', '130100000000', '石家庄市', NULL, 'a7b8c9d0-e1f2-4345-9678-90abcdef012', NULL, 'user_r', '何梅', '13800000000', '2025-09-23 16:02:01', 'system', '2025-09-23 16:02:01', 'system');

-- ----------------------------
-- Table structure for process_record
-- ----------------------------
DROP TABLE IF EXISTS `process_record`;
CREATE TABLE `process_record`  (
  `processid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行唯一标识',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基准数据元id',
  `flowid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  `processuserid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人id',
  `processusername` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  `processunitcode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人单位code',
  `processunitname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人单位名称',
  `useroperation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `processdatetime` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `sourceactivityname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '源环节',
  `destactivityname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标环节',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  PRIMARY KEY (`processid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of process_record
-- ----------------------------

-- ----------------------------
-- Table structure for source_event_record
-- ----------------------------
DROP TABLE IF EXISTS `source_event_record`;
CREATE TABLE `source_event_record`  (
  `record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录ID',
  `data_element_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据元的唯一标识',
  `data_element_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据元名称',
  `source_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定源类型(确认型核定/认领型核定/协商结果录入/手动定源/导入定源)',
  `source_date` datetime NULL DEFAULT NULL COMMENT '定源时间',
  `operator_account` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定源操作人账号',
  `contact_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定源联系人电话',
  `contact_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定源联系人姓名',
  `source_unit_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数源单位统一社会信用代码',
  `source_unit_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数源单位名称',
  `send_unit_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确定数据单位的单位统一社会信用代码',
  `send_unit_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确定数据单位的单位数源单位名称',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定源事件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_event_record
-- ----------------------------
INSERT INTO `source_event_record` VALUES ('11133f4d-f17c-4046-ba56-a2b73cd160ac', '7a8b9c0d-e1f2-a3b4-c5d6-e7f8a9b0c1d4', '负债总额是否公示', 'claim_approval', '2025-10-31 15:18:30', 'user1_002', '13800000000', '李四', '911800001000556000', '统计局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('1d53fe94-2535-4676-b168-33d6576bde89', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '递延所得税负债期末余额', 'confirmation_approval', '2025-10-31 14:23:41', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('20feb0a9-52c1-45b4-9e27-f30618291d66', '3e4f5a6b-7c8d-9e0f-1a2b-3c4d5e6f7a8b', '短期投资年末数', 'negotiation_result_entry', '2025-10-31 14:27:11', 'user1_002', NULL, NULL, '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('21496937-6857-4428-bfd8-b6427143e760', '4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d', '短期投资期末余额', 'negotiation_result_entry', '2025-10-31 14:26:53', 'user1_002', NULL, NULL, '913900001000334000', '国家数据共享主管部门', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('2d1bd354-7b93-4969-b30b-7d45d71e8220', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', '发放贷款和垫款期末余额', 'negotiation_result_entry', '2025-10-31 14:26:01', 'user1_002', NULL, NULL, '914500001000889000', '经济和信息化局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('42ac4459-65cf-487b-a401-88032f654d08', 'abcdef98-7654-3210-fedc-ba9876543210', '电子邮箱', 'confirmation_approval', '2025-10-31 15:06:26', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('59ae3cde-42b3-4296-b1b2-bf462f32ab5a', '660ab1c2-def3-4567-890a-bcdef1234567', '筹资活动现金金额流量净额', 'claim_approval', '2025-10-31 15:18:22', 'user1_002', '13800000000', '李四', '911800001000556000', '统计局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('6dbe65c6-3848-4e40-9472-76e4016e34c2', '888e8400-e29b-41d4-a716-446655440019', '保障性住房净值期末余额', 'confirmation_approval', '2025-10-31 14:23:41', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('75323940-3767-42b0-9134-45ad502278a5', '2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f', '短期借款期末数', 'confirmation_approval', '2025-10-31 15:18:30', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('7afdc24e-61e4-42ca-9ed9-66895ca2abfd', '222e8400-e29b-41d4-a716-446655440013', '保户质押贷款期末余额', 'negotiation_result_entry', '2025-10-31 14:25:56', 'user1_002', NULL, NULL, '914500001000889000', '经济和信息化局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('7da5f4b5-82b4-49ac-8a09-8e8734812ab5', '990e8400-e29b-41d4-a716-446655440005', '办理使用登记特种设备总台（套）数', 'confirmation_approval', '2025-10-31 14:23:29', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('8e2dc7bc-f432-42ed-b821-4e8f272711e6', 'dd0e8400-e29b-41d4-a716-446655440009', '保户储金及投资款净增加额本期金额', 'negotiation_result_entry', '2025-10-31 14:27:14', 'user1_002', NULL, NULL, '111000000000132664', '中华人民共和国农业农村部', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('9a8152a9-340c-4adb-8ba4-978095f53a4d', 'fedcba98-7654-3210-fedc-ba9876543210', '递延所得税资产期末余额', 'confirmation_approval', '2025-10-31 14:23:41', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('a03410a0-8887-4c8f-845e-e5dce0050093', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1c', '负债期末数合计', 'claim_approval', '2025-10-31 15:18:19', 'user1_002', '13800000000', '李四', '911800001000556000', '统计局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('a98ff045-764c-44a0-a561-e59561bf9f06', '6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', '对外投资支付的现金本年金额', 'confirmation_approval', '2025-10-31 14:23:31', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('d5863897-7697-4af2-a02c-6fd2092eba3a', 'bb0e8400-e29b-41d4-a716-446655440007', '保单红利支出本期金额', 'negotiation_result_entry', '2025-10-31 14:27:17', 'user1_002', NULL, NULL, '111000000000132664', '中华人民共和国农业农村部', '913900001000334000', '国家数据共享主管部门');
INSERT INTO `source_event_record` VALUES ('f8b6fe7f-cbbb-4199-ab80-76343a2a5d65', '9fa0b1c2-d3e4-f5a6-b7c8-d9e0f1a2b3c4', '短期投资期末数', 'confirmation_approval', '2025-10-31 14:23:28', 'user1_002', '13800000000', '李四', '11100000000014445H', '国家税务总局', '913900001000334000', '国家数据共享主管部门');

-- ----------------------------
-- Table structure for source_request_detail
-- ----------------------------
DROP TABLE IF EXISTS `source_request_detail`;
CREATE TABLE `source_request_detail`  (
  `dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据行唯一标识',
  `request_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单唯一标识',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元名称',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元状态',
  `definition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据元定义描述',
  `datatype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `data_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据格式',
  `value_domain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '值域',
  `base_dataelement_dataid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的基准数据元唯一标识',
  `base_dataelement_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的基准数据元名称',
  `provider_unit_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提供单位统一社会信用代码',
  `provider_unit_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提供单位名称',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `create_date` datetime NULL DEFAULT NULL COMMENT '数据元创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '数据元最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据元最后修改人账号',
  PRIMARY KEY (`dataid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定源需求申请单—领域数据元关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_request_detail
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_main
-- ----------------------------
DROP TABLE IF EXISTS `source_request_main`;
CREATE TABLE `source_request_main`  (
  `request_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请单唯一标识',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '申请单描述',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单状态',
  `department_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单提出部门统一社会信用代码',
  `department_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单提出部门名称',
  `requester_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单提出人账号',
  `requester_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单提出人姓名',
  `create_date` datetime NULL DEFAULT NULL COMMENT '申请单创建日期',
  `create_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单创建人账号',
  `last_modify_date` datetime NULL DEFAULT NULL COMMENT '申请单最后修改日期',
  `last_modify_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单最后修改人',
  PRIMARY KEY (`request_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定源需求申请单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_request_main
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

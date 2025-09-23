-- 使用数据库
USE dataelement_management;

-- 1. 先查看 base_data_element 表结构，确认字段
DESC base_data_element;



-- 3. 插入测试数据
INSERT INTO base_data_element (
    dataid,
    data_element_id,
    status,
    name,
    definition,
    datatype,
    data_format,
    value_domain,
    source_unit_code,
    source_unit_name,
    send_date,
    confirm_date,
    create_date,
    create_account,
    last_modify_date,
    last_modify_account
) VALUES
-- 已定源数据1
(
    'test_001',
    'DE001',
    'DESIGNATED_SOURCE',
    '企业名称',
    '企业的正式名称',
    'STRING',
    'VARCHAR(200)',
    '中文字符',
    '91110000123456789A',
    '北京市工商局',
    '2024-01-15 10:00:00',
    '2024-01-20 15:30:00',
    '2024-01-15 10:00:00',
    'admin',
    '2024-01-20 15:30:00',
    'admin'
),

-- 待定源数据
(
    'test_002',
    'DE002',
    'PENDING_SOURCE',
    '注册资本',
    '企业注册时的资本金额',
    'DECIMAL',
    'DECIMAL(18,2)',
    '正数',
    NULL,
    NULL,
    '2024-01-16 11:00:00',
    NULL,
    '2024-01-16 11:00:00',
    'admin',
    '2024-01-16 11:00:00',
    'admin'
),

-- 协商中数据
(
    'test_003',
    'DE003',
    'NEGOTIATING',
    '法定代表人',
    '企业法定代表人姓名',
    'STRING',
    'VARCHAR(50)',
    '中文姓名',
    NULL,
    NULL,
    '2024-01-17 09:00:00',
    NULL,
    '2024-01-17 09:00:00',
    'admin',
    '2024-01-17 09:00:00',
    'admin'
),

-- 已定源数据2 - 不同单位
(
    'test_004',
    'DE004',
    'DESIGNATED_SOURCE',
    '组织机构代码',
    '企业组织机构代码',
    'STRING',
    'VARCHAR(18)',
    '数字字母组合',
    '91110000987654321B',
    '北京市税务局',
    '2024-01-18 14:00:00',
    '2024-01-22 16:45:00',
    '2024-01-18 14:00:00',
    'admin',
    '2024-01-22 16:45:00',
    'admin'
),

-- 待核定数据
(
    'test_005',
    'DE005',
    'PENDING_APPROVAL',
    '经营范围',
    '企业经营业务范围',
    'TEXT',
    'TEXT',
    '中文描述',
    NULL,
    NULL,
    '2024-01-19 08:30:00',
    NULL,
    '2024-01-19 08:30:00',
    'admin',
    '2024-01-19 08:30:00',
    'admin'
);

-- 4. 验证插入的数据
SELECT
    dataid,
    name,
    status,
    source_unit_name,
    send_date,
    confirm_date
FROM base_data_element
WHERE dataid LIKE 'test_%'
ORDER BY send_date DESC;

-- 5. 查看总记录数
SELECT COUNT(*) as total_count FROM base_data_element;

-- 6. 按状态统计
SELECT
    status,
    COUNT(*) as count
FROM base_data_element
GROUP BY status;

-- 7. 如果需要插入 organization_unit 表的测试数据
INSERT INTO organization_unit (
    dataid,
    unit_code,
    unit_name,
    contact_name,
    contact_phone,
    create_date,
    create_account
) VALUES
      (
          'org_001',
          '91110000123456789A',
          '北京市工商局',
          '张三',
          '010-12345678',
          '2024-01-01 00:00:00',
          'admin'
      ),
      (
          'org_002',
          '91110000987654321B',
          '北京市税务局',
          '李四',
          '010-87654321',
          '2024-01-01 00:00:00',
          'admin'
      )
ON DUPLICATE KEY UPDATE
                     unit_name = VALUES(unit_name),
                     contact_name = VALUES(contact_name),
                     contact_phone = VALUES(contact_phone);

-- 8. 验证 organization_unit 数据
SELECT
    unit_code,
    unit_name,
    contact_name,
    contact_phone
FROM organization_unit
WHERE unit_code IN ('91110000123456789A', '91110000987654321B');


-- 插入一些适合手动定源的测试数据
INSERT INTO base_data_element (
    dataid, data_element_id, status, name, definition,
    datatype, data_format, value_domain, source_unit_code,
    source_unit_name, send_date, confirm_date,
    create_date, create_account, last_modify_date, last_modify_account
) VALUES
-- 待定源状态 - 适合手动定源
('manual_test_001', 'MDE001', 'PENDING_SOURCE', '手动定源测试1', '用于测试手动定源功能',
 'STRING', 'VARCHAR(100)', '测试值域', NULL,
 NULL, '2024-01-20 10:00:00', NULL,
 '2024-01-20 10:00:00', 'admin', '2024-01-20 10:00:00', 'admin'),

-- 协商中状态 - 也适合手动定源
('manual_test_002', 'MDE002', 'NEGOTIATING', '手动定源测试2', '协商中状态的数据元',
 'STRING', 'VARCHAR(100)', '测试值域', NULL,
 NULL, '2024-01-21 11:00:00', NULL,
 '2024-01-21 11:00:00', 'admin', '2024-01-21 11:00:00', 'admin'),

-- 任意状态测试 - 因为代码中状态校验被注释了
('manual_test_003', 'MDE003', 'PENDING_APPROVAL', '手动定源测试3', '任意状态测试',
 'STRING', 'VARCHAR(100)', '测试值域', NULL,
 NULL, '2024-01-22 12:00:00', NULL,
 '2024-01-22 12:00:00', 'admin', '2024-01-22 12:00:00', 'admin');

-- 验证测试数据插入
SELECT dataid, name, status, source_unit_code, source_unit_name
FROM base_data_element
WHERE dataid LIKE 'manual_test_%';
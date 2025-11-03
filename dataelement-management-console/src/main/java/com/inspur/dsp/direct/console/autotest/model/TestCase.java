package com.inspur.dsp.direct.console.autotest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 测试用例模型
 *
 * @author AutoTest
 * @since 2025-11-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {

    /**
     * 测试用例ID
     */
    private String id;

    /**
     * API路径
     */
    private String apiPath;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * Controller类名
     */
    private String controllerClass;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 测试用例描述
     */
    private String description;

    /**
     * 请求参数（JSON对象）
     */
    private Map<String, Object> requestParams;

    /**
     * 数据来源说明
     */
    private String dataSource;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否启用
     */
    private Boolean enabled;
}

package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上传文件结果VO
 * 封装上传文件处理结果的响应数据
 * 
 * @author Claude Code
 * @since 2025-09-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadConfirmResultVo {


    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 成功记录数
     */
    private Integer sucessQty;

    /**
     * 失败记录数
     */
    private Integer failQty;


    /**
     * 失败记录详情列表
     * 创建新的FailureDetailVo类，包含以下字段：
     * - serialNumber: 序号
     * - elementName: 数据元名称
     * - unitCode: 数源单位统一社会信用代码
     * - failureReason: 失败原因
     */
    private List<FailureDetailVo> failDetails;
}
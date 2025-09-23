package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 导入协商结果返回DTO - 协商中导入协商结果后的反馈信息
 */
@Data
public class ImportNegotiationReturnDTO {
    /** 总数量 */
    private Long total;

    /** 成功条数 */
    private Long sucessQty;

    /** 失败条数 */
    private Long failQty;

    /** 失败明细列表 */
    private List<ImportNegotiationFailDetailDTO> failDetails;
}
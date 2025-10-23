package com.inspur.dsp.direct.entity.vo;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 导入定数结果响应VO
 * 封装导入定数结果文件处理结果的响应数据
 * 
 * @author Claude Code
 * @since 2025-10-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetermineResultVo {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 成功记录数
     */
    private Integer successQty;

    /**
     * 失败记录数
     */
    private Integer failQty;

    /**
     * 失败记录详情列表
     * 包含失败的定数结果详细信息
     */
    private List<DetermineResultFailureDetailVo> failDetails;
}
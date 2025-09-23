package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.dbentity.NegotiationRecordDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 协商记录明细表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NegotiationRecordDetailInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 协商参与者记录数据ID
     */
    private String recordDetailId;

    /**
     * 协商记录数据ID
     */
    private String recordId;

    /**
     * 协商单位统一社会信用代码
     */
    private String negotiationUnitCode;

    /**
     * 协商单位名称
     */
    private String negotiationUnitName;

    /**
     * 协商结果
     */
    private String negotiationResult;

    /**
     * 记录协商结果用户账号
     */
    private String recorderAccount;

    /**
     * 记录协商结果用户姓名
     */
    private String recorderName;

    /**
     * 协商结果记录日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date resultDate;

    public static NegotiationRecordDetailInfoVo toVos(NegotiationRecordDetail negotiationRecordDetail){
        return NegotiationRecordDetailInfoVo.builder()
        		.recordDetailId(negotiationRecordDetail.getRecordDetailId())
        		.recordId(negotiationRecordDetail.getRecordId())
        		.negotiationUnitCode(negotiationRecordDetail.getNegotiationUnitCode())
        		.negotiationUnitName(negotiationRecordDetail.getNegotiationUnitName())
        		.negotiationResult(negotiationRecordDetail.getNegotiationResult())
        		.recorderAccount(negotiationRecordDetail.getRecorderAccount())
        		.recorderName(negotiationRecordDetail.getRecorderName())
        		.resultDate(negotiationRecordDetail.getResultDate())
        		.build();
    }
}
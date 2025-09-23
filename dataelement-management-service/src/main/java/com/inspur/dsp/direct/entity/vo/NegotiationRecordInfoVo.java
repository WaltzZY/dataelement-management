package com.inspur.dsp.direct.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import com.inspur.dsp.direct.dbentity.NegotiationRecordDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 协商记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NegotiationRecordInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 协商记录数据ID
     */
    private String recordId;

    /**
     * 基准数据元唯一标识
     */
    private String baseDataelementDataid;

    /**
     * 发出日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /**
     * 发出人用户账号
     */
    private String senderAccount;

    /**
     * 发出人姓名
     */
    private String senderName;

    /**
     * 协商事宜说明
     */
    private String negotiationContent;

    /**
     * 协商记录明细数据
     */
    private List<NegotiationRecordDetailInfoVo> negotiationRecordDetailList;

    /**
     * 基准数据元状态
     */
    private String status;
    /**
     * 状态描述
     */
    private String statusDesc;


    /**
     * 转换
     * @param negotiationRecord
     * @return
     */
    public static NegotiationRecordInfoVo toVo(NegotiationRecord negotiationRecord) {
        List<NegotiationRecordDetail> recordNegotiationRecordDetailList = negotiationRecord.getNegotiationRecordDetailList();
        List<NegotiationRecordDetailInfoVo> recordDetailInfoVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recordNegotiationRecordDetailList)) {
            recordDetailInfoVos = recordNegotiationRecordDetailList.stream().filter(Objects::nonNull).map(NegotiationRecordDetailInfoVo::toVos)
                    .collect(Collectors.toList());
        }

        return NegotiationRecordInfoVo.builder()
                .recordId(negotiationRecord.getRecordId())
                .baseDataelementDataid(negotiationRecord.getBaseDataelementDataid())
                .sendDate(negotiationRecord.getSendDate())
                .senderAccount(negotiationRecord.getSenderAccount())
                .senderName(negotiationRecord.getSenderName())
                .negotiationContent(negotiationRecord.getNegotiationContent())
                .negotiationRecordDetailList(recordDetailInfoVos)
                .build();
    }
}
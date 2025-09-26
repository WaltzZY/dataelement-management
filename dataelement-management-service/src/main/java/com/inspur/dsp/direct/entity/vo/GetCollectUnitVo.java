package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCollectUnitVo {
    private String taskId;
    private String status;
    private String statusChinese;
    private String sourceUnitCode;
    private String sourceUnitName;
    private String name;
    private String processingDate;
    private String sendDate;
    private String processingOpinion;
}

package com.inspur.dsp.direct.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailedCountDTO {
    /**
     * 分类id
     */
    private List<String> categoryIds;
    /**
     * 关联地区编码
     */
    private List<String> regionCodes;
    /**
     * 关联单位编码
     */
    private List<String> orgCodes;
    /**
     * 全文检索
     */
    private String searchValue;
    /**
     * 状态，01 待定源，02 待定标，03 已发布，04 已废弃
     */
    private String status;
}

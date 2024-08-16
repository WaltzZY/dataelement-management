package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetDetailedListVO {
    /**
     * 分类名称
     */
    private List<String> categoryList;
    /**
     * 来源单位名称
     */
    private List<String> dataElementCollectOrgNames;
    /**
     * 数据格式
     */
    private String dataElementDataFormat;
    /**
     * 数据类型
     */
    private String dataElementDataType;
    /**
     * 定义
     */
    private String dataElementDefinition;
    /**
     * 数据元id
     */
    private String dataElementId;
    /**
     * 数据元名称
     */
    private String dataElementName;
    /**
     * 数据元状态，01 待定源，02 待定标，03 已发布，04 已废弃
     */
    private String dataElementStatus;
}
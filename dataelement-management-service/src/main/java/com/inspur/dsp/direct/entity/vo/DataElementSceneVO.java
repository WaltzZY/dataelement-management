package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 响应参数
 */
@Data
public class DataElementSceneVO {

    /**
     * 场景id
     */
    private String sceneId;
    /**
     * 场景名称
     */
    private String sceneName;
    /**
     * 场景描述
     */
    private String sceneDesc;
    /**
     * 附件id
     */
    private String attachFileId;
    /**
     * 附件名称
     */
    private String attachFileName;
}

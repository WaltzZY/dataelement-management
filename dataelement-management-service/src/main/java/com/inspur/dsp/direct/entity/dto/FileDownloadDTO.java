package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FileDownloadDTO {

    /**
     * 文件id
     */
    @NotBlank(message = "文件id不许为空")
    private String docId;

    /**
     * 文件名
     */
    @NotBlank(message = "文件name不许为空")
    private String fileName;
}

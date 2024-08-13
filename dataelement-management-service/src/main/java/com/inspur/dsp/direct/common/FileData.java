package com.inspur.dsp.direct.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件内容
     */
    private byte[] fileData;
}

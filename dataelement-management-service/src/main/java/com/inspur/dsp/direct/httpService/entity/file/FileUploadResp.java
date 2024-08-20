package com.inspur.dsp.direct.httpService.entity.file;

import lombok.Data;

@Data
public class FileUploadResp {

    /**
     * OK 成功
     */
    private String msg;
    /**
     * 0000 成功
     */
    private String code;
    /**
     * 文件id
     */
    private String photo_id;
    /**
     * 文件id
     */
    private String docid;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 文件大小
     */
    private int capacity;
    /**
     * 文件类型
     */
    private String mime_type;
    /**
     * 文件hash
     */
    private String hashCode;
    /**
     * 文件名
     */
    private String name;
    /**
     * 上传时间
     */
    private String up_time;
    /**
     * 文件夹id
     */
    private String folder_id;
    private String r_type;
    private String disk_id;
}

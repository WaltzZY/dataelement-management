package com.inspur.dsp.direct.httpService;

import com.inspur.dsp.direct.httpService.entity.file.FileUploadResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 下载rc文件
     *
     * @param docId    文档id
     * @param fileName 文件名
     * @return
     */
    ResponseEntity<byte[]> rcdownLoad(String docId, String fileName);

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    FileUploadResp upload(MultipartFile file);
}

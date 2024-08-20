package com.inspur.dsp.direct.httpService.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.dsp.direct.common.FileData;
import com.inspur.dsp.direct.common.HttpClient;
import com.inspur.dsp.direct.exception.CustomException;
import com.inspur.dsp.direct.httpService.FileService;
import com.inspur.dsp.direct.httpService.entity.file.FileUploadResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ObjectMapper objectMapper;

    /**
     * 文件服务器内网访问地址
     */
    @Value("${spring.application.inner_rcservice}")
    private String innerUrl;


    /**
     * 下载rc文件
     *
     * @param docId    文档id
     * @param fileName 文件名
     * @return
     */
    @Override
    public ResponseEntity<byte[]> rcdownLoad(String docId, String fileName) {
        String fileUrl = innerUrl + "/doc?doc_id=" + docId;
        String contentType = "application/octet-stream;charset=utf-8"; // 默认二进制流
        FileData fileData = HttpClient.httpGetFileMethod(fileUrl);
        try {
            //纯下载方式 文件名应该编码成UTF-8
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode((fileName), "UTF-8").replaceAll("\\+", "%20") + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileData.getFileData());
        } catch (IOException e) {
            throw new CustomException("rc文件下载失败");
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public FileUploadResp upload(MultipartFile file) {
        String uploadUrl = innerUrl + "/upload";
        Map<String, String> data = new HashMap<>();
        String filename = file.getOriginalFilename();
        data.put("name", filename);
        data.put("uid", "");
        data.put("type", "doc");
        data.put("disk_id", "1");
        data.put("real_path", "datafileset");
        data.put("folder_id", "0");
        String jsonString = HttpClient.uploadFile(uploadUrl, data, file);
        log.info("上传文件返回结果：" + jsonString);
        FileUploadResp fileUploadResp = null;
        try {
            fileUploadResp = objectMapper.readValue(jsonString, FileUploadResp.class);
        } catch (JsonProcessingException e) {
            log.error("文件上传结果转换失败", e);
            throw new CustomException("文件上传失败");
        }
        fileUploadResp.setName(filename);
        return fileUploadResp;
    }
}

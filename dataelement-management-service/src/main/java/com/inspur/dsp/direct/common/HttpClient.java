package com.inspur.dsp.direct.common;

import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Component
public class HttpClient {

    public static JSONObject httpPostMethod(String getUrl, String jsonRequest) {
        RestTemplate restTemplate = new RestTemplate();
        getUrl = getUrl.replaceAll(" ", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Connection", "Keep-Alive");
        headers.set("Charset", "UTF-8");
        // 使用HttpEntity包装请求头和请求体
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
        return restTemplate.postForObject(getUrl, request, JSONObject.class);
    }

    public static JSONObject httpGetMethod(String getUrl) {
        RestTemplate restTemplate = new RestTemplate();
        getUrl = getUrl.replaceAll(" ", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "json");
        headers.set("Charset", "UTF-8");
        headers.set("Connection", "Keep-Alive");
        headers.set("accept", "*/*");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(getUrl, HttpMethod.GET, entity, JSONObject.class);
        return exchange.getBody();
    }

    public static FileData httpGetFileMethod(String fileUrl) {
        RestTemplate restTemplate = new RestTemplate();
        // 你可以根据需要调整headers，比如设置认证信息、接受类型等
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                fileUrl,
                HttpMethod.GET,
                null, // 这里我们没有附带请求体，所以是null
                byte[].class,
                URI.create(fileUrl) // 这里创建了一个URI对象，你也可以直接使用String，但URI.create可能更明确
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 如果请求成功，返回文件内容的byte[]
            ContentDisposition contentDisposition = responseEntity.getHeaders().getContentDisposition();
            String filename = contentDisposition.getFilename();
            return new FileData(filename, responseEntity.getBody());
        } else {
            // 处理错误情况，这里简单返回null，实际项目中可能需要抛出异常或进行其他错误处理
            throw new CustomException("文件下载失败");
        }
    }
}

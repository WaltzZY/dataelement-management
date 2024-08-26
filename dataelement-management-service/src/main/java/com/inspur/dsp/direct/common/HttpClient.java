package com.inspur.dsp.direct.common;

import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    /**
     * 处理响应头为text/html,实际响应数据为json的数据
     *
     * @param getUrl
     * @return
     */
    public static JSONObject httpGetMethodHandleHtml(String getUrl) {
        RestTemplate restTemplate = new RestTemplate();
        // 增加自定义解析器
        restTemplate.getMessageConverters().add(new BspMappingJackson2HttpMessageConverter());
        getUrl = getUrl.replaceAll(" ", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
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

    /**
     * 上传文件
     *
     * @param uploadUrl
     * @param data
     * @param file
     * @return
     */
    public static String uploadFile(String uploadUrl, Map<String, String> data, MultipartFile file) {
        // 创建HttpClient对象
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(uploadUrl);

            // 创建MultipartEntityBuilder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 添加文件部分
            builder.addBinaryBody("file", file.getInputStream(), org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, file.getOriginalFilename());

            // 添加其他参数
            for (Map.Entry<String, String> entry : data.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), org.apache.http.entity.ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
            }

            org.apache.http.HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                org.apache.http.HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                }
            }
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new CustomException("文件上传失败");
        }
        return null;
    }

    /**
     * 获取文件流
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String fileUrl) throws IOException {
        CloseableHttpResponse response;
        ByteArrayInputStream byteArrayInputStream;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(fileUrl);
            response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            byte[] byteArray = toByteArray(content);
            byteArrayInputStream = new ByteArrayInputStream(byteArray);
        }
        return byteArrayInputStream;
    }

    /**
     * 从 InputStream 中读取所有数据到 byte 数组。
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        return out.toByteArray();
    }
}

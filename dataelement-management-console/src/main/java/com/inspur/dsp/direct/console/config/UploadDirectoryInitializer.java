package com.inspur.dsp.direct.console.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 应用启动时初始化上传目录
 */
@Component
@Slf4j
public class UploadDirectoryInitializer implements ApplicationRunner {

    /**
     * 从配置文件中获取上传路径
     */
    @Value("${upload.path:/home/upload/}")
    private String uploadPath;

    /**
     * 应用启动后执行初始化操作
     *
     * @param args 应用参数
     * @throws Exception 初始化过程中可能抛出的异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 将配置的路径转换为Path对象
        Path uploadDir = Paths.get(uploadPath);

        // 检查并创建上传目录
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("Upload directory created: {}", uploadDir.toString());
        } else {
            log.info("Upload directory already exists: {}", uploadDir.toString());
        }
    }
}

package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileUploadConfig {

    /**
     * 文件上传路径
     */
    private String uploadPath;

    /**
     * 文件访问URL前缀
     */
    private String accessUrl;
}

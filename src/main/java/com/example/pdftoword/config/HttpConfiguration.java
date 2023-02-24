package com.example.pdftoword.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class HttpConfiguration {

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置单个文件的最大大小
        factory.setMaxFileSize(DataSize.ofMegabytes(100));
        // 设置总文件大小的最大值
        factory.setMaxRequestSize(DataSize.ofMegabytes(1000));
        // 设置临时文件存储的目录
        factory.setLocation("D:/tmp");
        // 开启分块上传
        factory.setFileSizeThreshold(DataSize.ofMegabytes(1));
        return factory.createMultipartConfig();
    }
}


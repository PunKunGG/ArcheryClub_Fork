package com.example.demo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	private final FileStorageProperties fileStorageProperties;

    // ฉีด Bean ที่เราสร้างไว้แล้วเข้ามาใช้งาน
    public MvcConfig(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // --- เปลี่ยนมาใช้วิธีนี้ ---
        // 1. นำ Path จาก properties มาสร้างเป็น Path object
        Path uploadPath = Paths.get(fileStorageProperties.getUploadDir());

        // 2. แปลง Path object ให้เป็น URI ที่ถูกต้อง (จะได้ผลลัพธ์เช่น file:///D:/my-app-uploads/)
        String resourceLocation = uploadPath.toUri().toString();

        registry
            .addResourceHandler("/storage/**")
            .addResourceLocations(resourceLocation); // 3. ใช้ URI ที่แปลงแล้ว
    }
}

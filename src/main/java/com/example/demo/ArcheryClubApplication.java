package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.demo.config.FileStorageProperties;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(FileStorageProperties.class)
public class ArcheryClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArcheryClubApplication.class, args);
	}

}

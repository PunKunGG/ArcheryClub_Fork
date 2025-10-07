package com.example.demo.config; // เปลี่ยน package ให้ตรงกับโปรเจคของหนู

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

/**
 * DataInitializer
 * 
 * คลาสนี้จะรันตอน Spring Boot startup
 * ใช้สร้าง Superadmin ถ้า DB ยังไม่มี
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // ตรวจสอบว่ามี SUPERADMIN อยู่ใน DB หรือยัง
        if (userRepository.findByRole(Role.SUPERADMIN).isEmpty()) {
            User admin = new User();
            admin.setEmail("thitiwut.si@kkumail.com"); // ใช้ email แทน username
            admin.setUsername("ธิติวุฒิ ศรีอมรรัตน์"); // optional
            admin.setPassword(passwordEncoder.encode("123456789")); // รหัสผ่านเริ่มต้น
            admin.setRole(Role.SUPERADMIN); // กำหนด role เป็น SUPERADMIN
            userRepository.save(admin);
            System.out.println("Superadmin created: superadmin / password123");
        } else {
            System.out.println("Superadmin already exists, skipping creation.");
        }
    }
}

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
 * คลาสนี้จะรันตอน Spring Boot startup ใช้สร้าง Superadmin ถ้า DB ยังไม่มี
 */
@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.findByRole(Role.SUPERADMIN).isEmpty()) {
			User admin = new User();
			admin.setEmail("thitiwut.si@kkumail.com");
			admin.setUsername("ธิติวุฒิ ศรีอมรรัตน์");
			admin.setPassword(passwordEncoder.encode("123456789"));
			admin.setPhone("0000000000"); // ✅ เพิ่มบรรทัดนี้
			admin.setStudentid("0000000000"); // ถ้ามีฟิลด์นี้ใน entity
			admin.setRole(Role.SUPERADMIN);
			userRepository.save(admin);
			System.out.println("✅ Superadmin created: thitiwut.si@kkumail.com / 123456789");
		} else {
			System.out.println("Superadmin already exists, skipping creation.");
		}
	}

}

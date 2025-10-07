package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

// ไม่จำเป็นต้อง import CustomUserDetails ที่นี่
// import com.example.demo.service.CustomUserDetails;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // เปิดใช้งาน @PreAuthorize, @PostAuthorize ใน Controller
public class SecurityConfig {

    // ไม่จำเป็นต้อง inject CustomUserDetails ที่นี่แล้ว
    // Spring Security จะหา Bean ที่เป็น UserDetailsService ไปใช้เองโดยอัตโนมัติ

    /**
     * SecurityFilterChain สำหรับ REST API (Stateless)
     * @Order(1) ทำให้ Chain นี้ถูกตรวจสอบก่อน Chain อื่น
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // 1. ใช้กฎนี้กับ URL ที่ขึ้นต้นด้วย /api/ เท่านั้น
            .csrf(csrf -> csrf.disable()) // 2. ปิด CSRF สำหรับ API เพราะเป็น stateless
            .authorizeHttpRequests(auth -> auth
            		
            		.requestMatchers("/api/**").permitAll() // อนุญาตให้ทุก Request ที่ขึ้นต้นด้วย /api/ เข้าถึงได้เลย
                // ✅ เพิ่มการกำหนดสิทธิ์ API ตาม Role
                .requestMatchers("/api/super-admin/**").hasAnyAuthority("SUPERADMIN")
                .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN")
                .requestMatchers("/api/member/**").hasAnyAuthority("MEMBER", "ADMIN", "SUPERADMIN")
                
                .anyRequest().authenticated() // 3. ทุก Request อื่นๆ ใน /api/ ต้องมีการยืนยันตัวตน
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 4. ไม่สร้าง Session
            .httpBasic(withDefaults()); // 5. เปิดใช้งาน HTTP Basic Auth (เหมาะสำหรับทดสอบ)

        return http.build();
    }
	
	
	/*  @Bean
	    @Order(1)
	    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .securityMatcher("/api/**") // 1. ใช้กฎนี้กับ URL ที่ขึ้นต้นด้วย /api/ เท่านั้น
	            .csrf(csrf -> csrf.disable()) // 2. ปิด CSRF สำหรับ API
	            .authorizeHttpRequests(auth -> auth
	                // 3. อนุญาตทุก Request ที่เข้ามาใน /api/ โดยไม่ต้องตรวจสอบสิทธิ์
	                .anyRequest().permitAll() 
	            )
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 4. ไม่สร้าง Session
	            .httpBasic(withDefaults()); // 5. ยังคงเปิด httpBasic ไว้ (แต่ตอนนี้ไม่จำเป็นแล้ว)

	        return http.build();
	    }
*/
    /**
     * SecurityFilterChain สำหรับ Web Application (Stateful)
     * จะทำงานกับทุก Request ที่ไม่ตรงกับ securityMatcher ของ Chain ที่มี @Order(1)
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 1. กำหนด URL ที่ทุกคนสามารถเข้าถึงได้ (Public)
                .requestMatchers("/", "/welcome", "/login", "/register", "/register/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/storage/**").permitAll()

                // 2. ✅ เพิ่มการกำหนดสิทธิ์ตาม Role สำหรับหน้าเว็บต่างๆ
                .requestMatchers("/super-admin/**").hasAnyAuthority("SUPERADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN")
                .requestMatchers("/member/**").hasAnyAuthority("MEMBER", "ADMIN", "SUPERADMIN")

                
                // 3. Request อื่นๆ ที่เหลือทั้งหมด ต้องผ่านการยืนยันตัวตน
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/", true) // กลับไปหน้าหลักหลังล็อกอินสำเร็จ
                .permitAll()
            )
           .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // กลับไปหน้า login หลัง logout
                .permitAll()
           );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


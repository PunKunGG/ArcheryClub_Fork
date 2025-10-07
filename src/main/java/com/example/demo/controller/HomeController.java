package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 🔹 ถ้ายังไม่ได้ล็อกอินหรือเป็น anonymous ให้ไปหน้า welcome
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
			return "redirect:/welcome";
		}

		// 🔹 ถ้าเข้าสู่ระบบแล้ว เช็ก role ตามเดิม
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN")
						|| grantedAuthority.getAuthority().equals("SUPERADMIN"));

		if (isAdmin) {
			return "redirect:/admin/dashboard";
		} else {
			return "redirect:/member/dashboard";
		}
	}

}
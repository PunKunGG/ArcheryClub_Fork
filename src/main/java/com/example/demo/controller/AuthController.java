package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	
	@Autowired
	private UserService userService;
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
	    if (!model.containsAttribute("request")) {
	        model.addAttribute("request", new CreateUserRequest());
	    }
	    return "register";
	}

	
	@PostMapping("/register")
	public String registerUser(
	        @Valid CreateUserRequest request,  // ใช้ @Valid เพื่อให้ตรวจสอบตาม annotation
	        BindingResult result,               //ใช้ตรวจว่ามี error หรือไม่
	        RedirectAttributes redirectAttributes) {

	    if (result.hasErrors()) {
	        // ถ้ามี error ให้ส่งกลับไปหน้า register พร้อมข้อความ
	        redirectAttributes.addFlashAttribute("errorMessage", "กรุณากรอกข้อมูลให้ถูกต้อง");
	        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.request", result);
	        redirectAttributes.addFlashAttribute("request", request);
	        return "redirect:/register";
	    }

	    userService.createUser(request); //ใช้เมธอดที่รองรับ DTO และมี validation
	    redirectAttributes.addFlashAttribute("successMessage", "ลงทะเบียนสำเร็จแล้ว กรุณาเข้าสู่ระบบ");

	    return "redirect:/login";
	}

	
	@GetMapping("/login")
		public String showLoginForm() {
			return "login";
		}
	

}

package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UpdateUserRoleRequest;
import com.example.demo.model.Role;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UpdateUserRoleController {
	
	private UserService userService;
	
	public UpdateUserRoleController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public String showUsers(Model model) {
		model.addAttribute("users",userService.getAllUsers());
		model.addAttribute("roles",Role.values());
		return "super-admin/updateUserRole";
	}
	
	@PostMapping("/updateRole")
	public String updateUserRole(@RequestParam("userId") Long userId,
	        @RequestParam("role") Role role,
			RedirectAttributes redirectAttribute) 
	{
		  try {
		        userService.updateRole(userId, role);
		        redirectAttribute.addFlashAttribute("successMessage", "อัปเดตตำแหน่งสมาชิกสำเร็จแล้ว");
		    } catch (Exception e) {
		        redirectAttribute.addFlashAttribute("errorMessage", "อัปเดตไม่สำเร็จ: " + e.getMessage());
		    }
		return "redirect:/admin/users";
	}
}

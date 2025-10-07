package com.example.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UpdateUserRoleRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')") // บังคับสิทธิ์ Admin
public class UpdateUserRoleRestController {
	
	 private final UserService userService;

	    public UpdateUserRoleRestController(UserService userService) {
	        this.userService = userService;
	    }

	    @PostMapping("/{id}/role")
	    public ResponseEntity<UserResponse> updateUserRole(
	            @PathVariable("id") Long userId,
	            @Valid @RequestBody UpdateUserRoleRequest request) {
	        
	        User updatedUser = userService.updateRole(userId, request.getRole());
	        return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
	    }
}

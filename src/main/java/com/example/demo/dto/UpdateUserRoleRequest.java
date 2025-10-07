package com.example.demo.dto;

import com.example.demo.model.Role;

public class UpdateUserRoleRequest {
	
	private Long userId;
	private Role role;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}

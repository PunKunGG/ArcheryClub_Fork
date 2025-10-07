package com.example.demo.dto;

import com.example.demo.model.Role;

import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleRequestForApi {
	
	 @NotNull(message = "Role cannot be null")
	    private Role role;

	    // Getters and Setters
	    public Role getRole() {
	        return role;
	    }
	    public void setRole(Role role) {
	        this.role = role;
	    }

}

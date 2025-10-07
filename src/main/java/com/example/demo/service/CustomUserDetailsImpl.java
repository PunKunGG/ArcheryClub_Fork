package com.example.demo.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.Role;
import com.example.demo.model.User;

public class CustomUserDetailsImpl implements UserDetails{
	private User user;

	public CustomUserDetailsImpl(User user) {
		this.user = user;
	}

	public String getStudentid() {
		return user.getStudentid();
	}
	
	public String getPassword() {
		return  user.getPassword();
	}
	
	public String getUsername() {
		return user.getUsername();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public String getPhone() {
		return user.getPhone();
	}
	
	public Role getRole() {
		return user.getRole();
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities() { 
		return java.util.List.of(() -> "ROLE_" + user.getRole().name()); 
	}
	
	public boolean isAccountNonExpired() {
		return true;
	}
	
	public boolean isAccountNonLocked() {
		return true;
	}
	
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public boolean isEnabled() { 
		return true; 
	}

	
	

}

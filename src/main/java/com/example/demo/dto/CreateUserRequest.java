package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    @Pattern(
    	    regexp = "^.+@(kku\\.ac\\.th|kkumail\\.com)$", 
    	    message = "กรุณาใช้อีเมลของ KKU (@kku.ac.th หรือ @kkumail.com)"
    	)
    private String email;
    
    
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    @NotBlank(message = "Student ID cannot be empty")
    @Pattern(regexp = ".*-.*", message = "รหัสนักศึกษาต้องมีเครื่องหมายขีด (-)")
    private String studentId;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

    
}
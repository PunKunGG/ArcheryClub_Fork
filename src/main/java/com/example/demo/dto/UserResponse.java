package com.example.demo.dto;

import com.example.demo.model.Role;
import com.example.demo.model.User;

public class UserResponse {
	
	 private Long id;
	    private String username;
	    private String email;
	    private String phone;
	    private String studentId;
	    private Role role;

	    public static UserResponse fromEntity(User user) {
	        if (user == null) return null;
	        
	        UserResponse dto = new UserResponse();
	        dto.setId(user.getId());
	        dto.setUsername(user.getUsername());
	        dto.setEmail(user.getEmail());
	        dto.setPhone(user.getPhone());
	        dto.setStudentId(user.getStudentid());
	        dto.setRole(user.getRole());
	        return dto;
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
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

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}
}

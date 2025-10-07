package com.example.demo.dto;

public class DashboardViewModel {
	
	 private String studentId;
	 private String username;
	 private String roleDisplayName;
	 public String getStudentId() {
		 return studentId;
	 }
	 public void setStudentId(String studentId) {
		 this.studentId = studentId;
	 }
	 public String getUsername() {
		 return username;
	 }
	 public void setUsername(String username) {
		 this.username = username;
	 }
	 public String getRoleDisplayName() {
		 return roleDisplayName;
	 }
	 public void setRoleDisplayName(String roleDisplayName) {
		 this.roleDisplayName = roleDisplayName;
	 }
}

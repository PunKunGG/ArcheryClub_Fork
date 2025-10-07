package com.example.demo.dto;

import java.util.List;

public class MemberDashboardViewModel {
	
	private String studentId;
    private String username;
    private String roleDisplayName;
    private boolean hasLoans;
    private String loanedItemsSummary;
    private String errorMessage;
    private String successMessage;
    private List<AnnouncementDto> announcements; // สามารถใช้ AnnouncementDto ตัวเดียวกับของ Admin ได้
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
	public boolean isHasLoans() {
		return hasLoans;
	}
	public void setHasLoans(boolean hasLoans) {
		this.hasLoans = hasLoans;
	}
	public String getLoanedItemsSummary() {
		return loanedItemsSummary;
	}
	public void setLoanedItemsSummary(String loanedItemsSummary) {
		this.loanedItemsSummary = loanedItemsSummary;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public List<AnnouncementDto> getAnnouncements() {
		return announcements;
	}
	public void setAnnouncements(List<AnnouncementDto> announcements) {
		this.announcements = announcements;
	}
}

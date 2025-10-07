package com.example.demo.model;

public enum Role {
	MEMBER("สมาชิกชมรม"),
	ADMIN("คณะกรรมการ"),
	SUPERADMIN("ประธานชมรม");
	
	private final String displayRole;

	public String getDisplayRole() {
		return displayRole;
	}
	
	Role (String displayRole){
		this.displayRole = displayRole;
	}
}

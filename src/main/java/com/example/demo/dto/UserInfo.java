package com.example.demo.dto;

import com.example.demo.model.User;

public class UserInfo {
	
	private Long id;
    private String username;

    public static UserInfo fromEntity(User user) {
        UserInfo dto = new UserInfo();
        dto.id = user.getId();
        dto.username = user.getUsername();
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
}

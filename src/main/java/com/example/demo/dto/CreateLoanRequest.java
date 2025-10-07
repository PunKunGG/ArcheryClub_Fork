package com.example.demo.dto;

import java.time.LocalDateTime;

public class CreateLoanRequest {
	
	
	private Long itemId;
	private LocalDateTime dueDate;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}
}

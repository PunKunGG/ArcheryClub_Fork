package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.ItemStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateItemRequest {
	
	@NotEmpty(message = "กรุณาเลือกอย่างน้อย 1 รายการ")
    private List<Long> itemIds;
	
	@NotNull(message = "กรุณาเลือกสถานะที่ต้องการเปลี่ยน")
	private ItemStatus newStatus;

	public List<Long> getItemIds() {
		return itemIds;
	}

	

	public ItemStatus getNewStatus() {
		return newStatus;
	}



	public void setNewStatus(ItemStatus newStatus) {
		this.newStatus = newStatus;
	}



	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}
}

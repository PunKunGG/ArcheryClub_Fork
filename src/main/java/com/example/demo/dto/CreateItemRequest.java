package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateItemRequest {

	@NotNull(message="กรุณาเลือกประเภทอุปกรณ์")
	@JsonProperty("equipmentId")
	private Long equipmentId;
	
    @NotNull(message = "กรุณาระบุจำนวน")
    @Min(value = 1, message = "จำนวนต้องมีอย่างน้อย 1")
    private Integer quantity;

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	} 
}

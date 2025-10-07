package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateEquipRequest {

	private Long id; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank(message="EquipName must not be blank")
	@JsonProperty("equipName")
	private String equipName;
	
	@NotBlank(message="prefixName of equip must not be blank")
	@JsonProperty("prefix")
	private String prefix;
	
	@NotNull(message="กรุณาเลือกหมวดหมู่")
	@JsonProperty("categoryId")
	private Long categoryId;

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}

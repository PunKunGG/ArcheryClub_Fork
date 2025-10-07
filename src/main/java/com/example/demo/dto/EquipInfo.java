package com.example.demo.dto;

import com.example.demo.model.Equipment;

public class EquipInfo {
	
	private Long id;
	private String name;
	private String categoryName;
	
	public static EquipInfo fromEntity(Equipment equip) {
	  EquipInfo dto = new EquipInfo();
	  dto.id = equip.getId();
	  dto.name = equip.getEquipName();
	   if (equip.getCategory() != null) {
	     dto.categoryName = equip.getCategory().getName();
	   }
	   return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}

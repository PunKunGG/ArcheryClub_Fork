package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Equipment;

public class EquipResponse {
	
	private Long id;
	private String equipName;
	private String prefix;
	private LocalDateTime createdAt;
	private CategoryInfo category;
	
	public EquipResponse(Long id,String equipName, String prefix, LocalDateTime createdAt,CategoryInfo category) {
		this.category = category;
		this.createdAt = createdAt;
		this.equipName = equipName;
		this.id = id;
		this.prefix = prefix;
	}

	public EquipResponse() {
		
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public CategoryInfo getCategory() {
		return category;
	}

	public void setCategory(CategoryInfo category) {
		this.category = category;
	}
	
	 public static EquipResponse fromEntity(Equipment entity) {
	        EquipResponse dto = new EquipResponse();
	        dto.setId(entity.getId());
	        dto.setEquipName(entity.getEquipName());
	        dto.setPrefix(entity.getPrefix());
	        dto.setCreatedAt(entity.getCreatedAt());

	        // แปลง Category Entity ที่ซับซ้อนให้เป็น CategoryInfo DTO ที่เรียบง่าย
	        // จุดนี้สำคัญมาก เพราะเราดึงข้อมูลจาก 'category' ที่ถูก JOIN FETCH มาแล้ว
	        dto.setCategory(new CategoryInfo(
	            entity.getCategory().getId(),
	            entity.getCategory().getName()
	        ));
	        
	        // dto.setAddByName(entity.getAddBy().getFullName()); // ตัวอย่าง
	        
	        return dto;
	    }
}

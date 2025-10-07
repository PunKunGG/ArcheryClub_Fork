package com.example.demo.dto;

import com.example.demo.model.Item;

public class ItemInfo {
	
	private Long id;
    private String assetCode;

    public static ItemInfo fromEntity(Item item) {
        ItemInfo dto = new ItemInfo();
        dto.id = item.getId();
        dto.assetCode = item.getAssetCode();
        return dto;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
}

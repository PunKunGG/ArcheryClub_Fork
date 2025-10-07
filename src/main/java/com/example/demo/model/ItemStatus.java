package com.example.demo.model;

public enum ItemStatus {
	
	 Available("พร้อมใช้งาน"),
	 Borrowed("ถูกยืม"),
	 Maintenance("ซ่อมบำรุง"),
	 Lost("สูญหาย"),
	 Pending_Return("รอการตรวจสอบ");
	//รายการของค่าคงที่ใน Enum ทั้งหมด จะต้องถูกประกาศไว้เป็นลำดับแรกสุดเสมอ 
	//ก่อนที่จะมีฟิลด์ (fields), constructor, หรือเมธอด (methods) ใดๆ 
	
	 private final String displayName;
	 
	  public String getDisplayName() {
		return displayName;
	}
	  
	  ItemStatus(String displayName) {
	        this.displayName = displayName;
	    }
}

	

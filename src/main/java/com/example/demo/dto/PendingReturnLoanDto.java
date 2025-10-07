package com.example.demo.dto;

import java.time.format.DateTimeFormatter;

import com.example.demo.model.Loan;

public class PendingReturnLoanDto {
	
	private Long id;
	private String assetCode;
	private String equipName;
	private String userName;
	private String formattedPendingReturnAt;	
	private String returnPhotoUrl;
	private boolean hasReturnPhoto;
	private String equipmentName;
	
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
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFormattedPendingReturnAt() {
		return formattedPendingReturnAt;
	}
	public void setFormattedPendingReturnAt(String formattedPendingReturnAt) {
		this.formattedPendingReturnAt = formattedPendingReturnAt;
	}
	public String getReturnPhotoUrl() {
		return returnPhotoUrl;
	}
	public void setReturnPhotoUrl(String returnPhotoUrl) {
		this.returnPhotoUrl = returnPhotoUrl;
	}
	public boolean isHasReturnPhoto() {
		return hasReturnPhoto;
	}
	public void setHasReturnPhoto(boolean hasReturnPhoto) {
		this.hasReturnPhoto = hasReturnPhoto;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	// --- สร้าง Static Factory Method ---
    public static PendingReturnLoanDto fromEntity(Loan loan) {
        PendingReturnLoanDto dto = new PendingReturnLoanDto();
        dto.setId(loan.getId());
        dto.setAssetCode(loan.getItem().getAssetCode());
        dto.setEquipName(loan.getItem().getEquip().getEquipName());
        dto.setUserName(loan.getUser().getUsername());

        // จัดรูปแบบวันที่
        if (loan.getPendingReturnAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm 'น.'");
            dto.setFormattedPendingReturnAt(loan.getPendingReturnAt().format(formatter));
        }

        // เตรียม URL รูปภาพ
        if (loan.getReturnPhotoPath() != null && !loan.getReturnPhotoPath().isEmpty()) {
            dto.setHasReturnPhoto(true);
            // สมมติว่าไฟล์ถูก serve ที่ /storage/ ตาม Controller เดิม
            dto.setReturnPhotoUrl("/storage/" + loan.getReturnPhotoPath());
        } else {
            dto.setHasReturnPhoto(false);
        }

        return dto;
    }

}

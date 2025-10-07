package com.example.demo.dto;

import com.example.demo.model.Item;
import com.example.demo.model.Loan;

public class ItemResponse {
	
	private Long id;
	private String assetCode;
	private String status;
	private EquipInfo equipInfo;
    private LoanInfo loanInfo;
	
	public ItemResponse(Long id,String assetCode,String status,EquipInfo equipInfo,LoanInfo loanInfo) {
		this.assetCode = assetCode;
		this.id = id;
		this.status = status;
		this.loanInfo = loanInfo;
		this.equipInfo = equipInfo;
	}
	
	public ItemResponse() {
		
	}
	
	

	public EquipInfo getEquipInfo() {
		return equipInfo;
	}

	public void setEquipInfo(EquipInfo equipInfo) {
		this.equipInfo = equipInfo;
	}

	public LoanInfo getLoanInfo() {
		return loanInfo;
	}

	public void setLoanInfo(LoanInfo loanInfo) {
		this.loanInfo = loanInfo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	 // Static factory method สำหรับการแปลง
    public static ItemResponse fromEntity(Item item, Loan loan) { // รับ Loan มาด้วย (อาจเป็น null)
        ItemResponse dto = new ItemResponse();
        dto.setId(item.getId());
        dto.setAssetCode(item.getAssetCode());
        dto.setStatus(item.getStatus().name()); // .name() จะได้ค่าเป็น String เช่น "AVAILABLE"

        // สร้าง Nested DTO สำหรับ Equipment
        if (item.getEquip() != null) {
            dto.setEquipInfo(EquipInfo.fromEntity(item.getEquip()));
        }

        // ถ้ามีข้อมูลการยืม (สถานะไม่ใช Available) ก็สร้าง Nested DTO สำหรับ Loan
        if (loan != null) {
            dto.setLoanInfo(LoanInfo.fromEntity(loan));
        }

        return dto;
    }
}

package com.example.demo.dto;

import java.time.format.DateTimeFormatter;

import com.example.demo.model.Loan;

public class LoanHistoryDto {
	
	private String assetCode;
	private String equipName;
	private String formattedLoanDate;       // วันที่ยืมที่จัดรูปแบบแล้ว
    private String formattedReturnDate;     // วันที่คืนที่จัดรูปแบบแล้ว
    private boolean hasReturnDate;          // ใช้สำหรับ th:if เพื่อเช็คว่ามีวันที่คืนหรือไม่
    private String statusText;              // ข้อความสถานะ (เช่น "คืนแล้ว")
    private String statusCssClass;          // class css ของป้ายสถานะ (เช่น "badge bg-success")
	
    // --- เพิ่มเมธอด static fromEntity เข้าไป ---
    public static LoanHistoryDto fromEntity(Loan loan) {
        LoanHistoryDto dto = new LoanHistoryDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm 'น.'");

        dto.setAssetCode(loan.getItem().getAssetCode());
        dto.setEquipName(loan.getItem().getEquip().getEquipName());
        
        if (loan.getCreatedAt() != null) {
            dto.setFormattedLoanDate(loan.getCreatedAt().format(formatter));
        } else {
            dto.setFormattedLoanDate("N/A");
        }

        if (loan.getReturnedAt() != null) {
            dto.setHasReturnDate(true);
            dto.setFormattedReturnDate(loan.getReturnedAt().format(formatter));
        } else {
            dto.setHasReturnDate(false);
        }

        if (loan.getReturnedAt() != null) {
            dto.setStatusText("คืนแล้ว");
            dto.setStatusCssClass("badge bg-success");
        } else if (loan.getPendingReturnAt() != null) {
            dto.setStatusText("รอตรวจสอบ");
            dto.setStatusCssClass("badge bg-warning text-dark");
        } else {
            dto.setStatusText("ยังไม่คืน");
            dto.setStatusCssClass("badge bg-danger");
        }
        return dto;
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
	public String getFormattedLoanDate() {
		return formattedLoanDate;
	}
	public void setFormattedLoanDate(String formattedLoanDate) {
		this.formattedLoanDate = formattedLoanDate;
	}
	public String getFormattedReturnDate() {
		return formattedReturnDate;
	}
	public void setFormattedReturnDate(String formattedReturnDate) {
		this.formattedReturnDate = formattedReturnDate;
	}
	public boolean isHasReturnDate() {
		return hasReturnDate;
	}
	public void setHasReturnDate(boolean hasReturnDate) {
		this.hasReturnDate = hasReturnDate;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getStatusCssClass() {
		return statusCssClass;
	}
	public void setStatusCssClass(String statusCssClass) {
		this.statusCssClass = statusCssClass;
	}
}

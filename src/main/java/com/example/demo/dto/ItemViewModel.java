package com.example.demo.dto;

public class ItemViewModel {
	
	// ข้อมูลพื้นฐานที่ต้องใช้แสดงผล
    private Long id;
    private Long loanId; // ID ของการยืม (ถ้ามี)
    private String assetCode;
    private String equipName;
    private String statusDisplayName;

    // --- นี่คือส่วนสำคัญ: Boolean Flags สำหรับ Thymeleaf ---
    private boolean canBorrow;         // แสดงปุ่ม "ยืม" หรือไม่?
    private boolean canRequestReturn;  // แสดงฟอร์ม "แจ้งคืน" หรือไม่?
    private boolean isPendingReturn;     // แสดงป้าย "รอกรรมการตรวจสอบ" หรือไม่?
    private boolean isLoanedByOther;   // แสดงป้าย "ถูกยืมโดยคนอื่น" หรือไม่?
	public Long getId() {
		return id;
	}
	
	
	public ItemViewModel(Long id, Long loanId, String assetCode, String equipName, String statusDisplayName,
			boolean canBorrow, boolean canRequestReturn, boolean isPendingReturn, boolean isLoanedByOther) {

		this.id = id;
		this.loanId = loanId;
		this.assetCode = assetCode;
		this.equipName = equipName;
		this.statusDisplayName = statusDisplayName;
		this.canBorrow = canBorrow;
		this.canRequestReturn = canRequestReturn;
		this.isPendingReturn = isPendingReturn;
		this.isLoanedByOther = isLoanedByOther;
	}

	public ItemViewModel() {
		
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
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
	public String getStatusDisplayName() {
		return statusDisplayName;
	}
	public void setStatusDisplayName(String statusDisplayName) {
		this.statusDisplayName = statusDisplayName;
	}
	public boolean isCanBorrow() {
		return canBorrow;
	}
	public void setCanBorrow(boolean canBorrow) {
		this.canBorrow = canBorrow;
	}
	public boolean isCanRequestReturn() {
		return canRequestReturn;
	}
	public void setCanRequestReturn(boolean canRequestReturn) {
		this.canRequestReturn = canRequestReturn;
	}
	public boolean isPendingReturn() {
		return isPendingReturn;
	}
	public void setPendingReturn(boolean isPendingReturn) {
		this.isPendingReturn = isPendingReturn;
	}
	public boolean isLoanedByOther() {
		return isLoanedByOther;
	}
	public void setLoanedByOther(boolean isLoanedByOther) {
		this.isLoanedByOther = isLoanedByOther;
	}

    

}

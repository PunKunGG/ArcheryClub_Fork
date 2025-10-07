package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Loan;

public class LoanResponse {
	
	 private Long id;
	 private LocalDateTime loanedAt;
	 private LocalDateTime dueDate;
	 private LocalDateTime pendingReturnAt;
	 private LocalDateTime returnedAt;
	 private UserInfo user;
	 private ItemInfo item;
	 
	 public static LoanResponse fromEntity(Loan loan) {
	        LoanResponse dto = new LoanResponse();
	        dto.setId(loan.getId());
	        dto.setLoanedAt(loan.getCreatedAt()); // สมมติว่าวันที่ยืมคือ createdAt
	        dto.setDueDate(loan.getDueDate());
	        dto.setPendingReturnAt(loan.getPendingReturnAt());
	        dto.setReturnedAt(loan.getReturnedAt());
	        dto.setUser(UserInfo.fromEntity(loan.getUser()));
	        dto.setItem(ItemInfo.fromEntity(loan.getItem()));
	        return dto;
	    }

	 public Long getId() {
		 return id;
	 }

	 public void setId(Long id) {
		 this.id = id;
	 }

	 public LocalDateTime getLoanedAt() {
		 return loanedAt;
	 }

	 public void setLoanedAt(LocalDateTime loanedAt) {
		 this.loanedAt = loanedAt;
	 }

	 public LocalDateTime getDueDate() {
		 return dueDate;
	 }

	 public void setDueDate(LocalDateTime dueDate) {
		 this.dueDate = dueDate;
	 }

	 public LocalDateTime getPendingReturnAt() {
		 return pendingReturnAt;
	 }

	 public void setPendingReturnAt(LocalDateTime pendingReturnAt) {
		 this.pendingReturnAt = pendingReturnAt;
	 }

	 public LocalDateTime getReturnedAt() {
		 return returnedAt;
	 }

	 public void setReturnedAt(LocalDateTime returnedAt) {
		 this.returnedAt = returnedAt;
	 }

	 public UserInfo getUser() {
		 return user;
	 }

	 public void setUser(UserInfo user) {
		 this.user = user;
	 }

	 public ItemInfo getItem() {
		 return item;
	 }

	 public void setItem(ItemInfo item) {
		 this.item = item;
	 }
}

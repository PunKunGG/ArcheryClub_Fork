package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Loan;

public class LoanInfo {
	
	private Long id;
    private String loanedByUsername;
    private LocalDateTime loanedAt;
    private LocalDateTime pendingReturnAt;

    public static LoanInfo fromEntity(Loan loan) {
        LoanInfo dto = new LoanInfo();
        dto.id = loan.getId();
        dto.loanedAt = loan.getCreatedAt();
        dto.pendingReturnAt = loan.getPendingReturnAt();
        if (loan.getUser() != null) {
            dto.loanedByUsername = loan.getUser().getUsername();
        }
        return dto;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoanedByUsername() {
		return loanedByUsername;
	}

	public void setLoanedByUsername(String loanedByUsername) {
		this.loanedByUsername = loanedByUsername;
	}

	public LocalDateTime getLoanedAt() {
		return loanedAt;
	}

	public void setLoanedAt(LocalDateTime loanedAt) {
		this.loanedAt = loanedAt;
	}

	public LocalDateTime getPendingReturnAt() {
		return pendingReturnAt;
	}

	public void setPendingReturnAt(LocalDateTime pendingReturnAt) {
		this.pendingReturnAt = pendingReturnAt;
	}
}

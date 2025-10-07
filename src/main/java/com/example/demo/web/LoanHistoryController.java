package com.example.demo.web;

import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.LoanHistoryDto;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.LoanService;

@Controller
public class LoanHistoryController {
	
	private final LoanService loanService;
	
	public LoanHistoryController(LoanService loanService) {
		this.loanService = loanService;
	}
	
	@GetMapping("/myLoanHistory")
	public String showMyHistory(Model model,
			@AuthenticationPrincipal User user,
			@PageableDefault(size=10,sort="createdAt",direction=Sort.Direction.DESC)Pageable pageable) {
		Page<Loan> loanPage = loanService.findUserHistory(user.getId(), pageable);
		
		Page<LoanHistoryDto> dtoPage = loanPage.map(this::convertToHistoryDto);
		
		model.addAttribute("loanPage",dtoPage);
		
		return "myLoanHistory";
	}
	
	private LoanHistoryDto convertToHistoryDto(Loan loan) {
		LoanHistoryDto dto = new LoanHistoryDto();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm 'น.'");
		
		dto.setAssetCode(loan.getItem().getAssetCode());
        dto.setEquipName(loan.getItem().getEquip().getEquipName());
        if (loan.getCreatedAt() != null) {
            dto.setFormattedLoanDate(loan.getCreatedAt().format(formatter));
        } else {
            dto.setFormattedLoanDate("N/A"); // หรือค่าว่าง ""
        }
        
        // --- Logic การแสดงวันที่คืน ---
        if (loan.getReturnedAt() != null) {
            dto.setHasReturnDate(true);
            dto.setFormattedReturnDate(loan.getReturnedAt().format(formatter));
        } else {
            dto.setHasReturnDate(false);
        }

        // --- Logic การกำหนดสถานะ ---
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

}

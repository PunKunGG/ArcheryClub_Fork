package com.example.demo.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoanHistoryDto;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.LoanServiceImpl;

@RestController
@RequestMapping("/api/my-history")
public class LoanHistoryRestController {
	
	private final LoanServiceImpl loanService;
	
	public LoanHistoryRestController(LoanServiceImpl loanService) {
		this.loanService = loanService;
	}
	
	@GetMapping 
	 public Page<LoanHistoryDto> getMyLoanHistory( // <-- 2. เพิ่ม public
	            @AuthenticationPrincipal User user,
	            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
	        
	        Page<Loan> loanPage = loanService.findUserHistory(user.getId(), pageable);
	        
	        return loanPage.map(LoanHistoryDto::fromEntity);
	    }

}

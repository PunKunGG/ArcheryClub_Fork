package com.example.demo.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CreateLoanRequest;
import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.LoanService;
import com.example.demo.service.LoanServiceImpl;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/loans")
public class LoanRestController {

	 private final LoanServiceImpl loanService; // หรือ LoanServiceImpl

	    public LoanRestController(LoanServiceImpl loanService) {
	        this.loanService = loanService;
	    }

	    // 1. Endpoint สำหรับการยืมอุปกรณ์
	    @PostMapping
	    public ResponseEntity<LoanResponse> createLoan(
	            @Valid @RequestBody CreateLoanRequest req,
	            @AuthenticationPrincipal User user) {
	        
	        Loan newLoan = loanService.createLoan(req.getItemId(), user);
	        LoanResponse response = LoanResponse.fromEntity(newLoan);
	        
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

	    // 2. Endpoint สำหรับการแจ้งคืนอุปกรณ์
	    @PostMapping("/{id}/request-return")
	    public ResponseEntity<LoanResponse> requestReturn(
	            @PathVariable("id") Long loanId,
	            @RequestParam("return_photo") MultipartFile returnPhoto) throws Exception { // <-- เพิ่ม throws Exception ตรงนี้

	        if (returnPhoto.isEmpty()) {
	            return ResponseEntity.badRequest().build();
	        }
	        
	        // ไม่ต้องใช้ try-catch แล้ว
	        Loan updatedLoan = loanService.requestReturn(loanId, returnPhoto);
	        LoanResponse response = LoanResponse.fromEntity(updatedLoan);

	        return ResponseEntity.ok(response);
	    }
}

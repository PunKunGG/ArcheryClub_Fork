package com.example.demo.web;

import com.example.demo.dto.LoanResponse; // Import DTO ที่เคยสร้างไว้
import com.example.demo.dto.PendingReturnLoanDto;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import com.example.demo.service.LoanServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/loans")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')") // <-- กำหนดสิทธิ์ให้เฉพาะ Admin
public class LoanAdminRestController {

    private final LoanServiceImpl loanService;

    public LoanAdminRestController(LoanServiceImpl loanService) {
        this.loanService = loanService;
    }

    // 1. Endpoint สำหรับดึงรายการที่รออนุมัติคืน
    @GetMapping("/pending-return")
    public List<PendingReturnLoanDto> getPendingReturnLoans() {
        List<Loan> pendingLoans = loanService.findPendingReturnLoans();
        
        // แปลง List<Loan> เป็น List<PendingReturnLoanDto> โดยใช้เมธอดที่เราสร้างใน DTO
        return pendingLoans.stream()
                .map(PendingReturnLoanDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. Endpoint สำหรับยืนยันการรับคืน
    @PostMapping("/{id}/confirm-return")
    public ResponseEntity<LoanResponse> confirmReturn(@PathVariable("id") Long loanId) {
        Loan updatedLoan = loanService.confirmReturn(loanId);
        
        // ส่งข้อมูล Loan ฉบับเต็มที่อัปเดตแล้วกลับไป
        return ResponseEntity.ok(LoanResponse.fromEntity(updatedLoan));
    }
}
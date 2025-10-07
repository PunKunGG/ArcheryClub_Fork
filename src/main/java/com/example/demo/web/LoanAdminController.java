package com.example.demo.web;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.PendingReturnLoanDto;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;

@Controller
public class LoanAdminController {
	
	private final LoanService loanService;
	
	public LoanAdminController(LoanService loanService) {
		this.loanService = loanService;
	}
	
	@GetMapping("/admin/loans/pending-return")
	public String showPendingReturn(Model model) {
		List<Loan> pendingLoans = loanService.findPendingReturnLoans();
		
		// 2. Controller ทำการแปลง (จัดจาน) เป็น DTO ด้วยตัวเอง
        List<PendingReturnLoanDto> loanDtos = pendingLoans.stream()
            .map(this::convertToPendingReturnDto)
            .collect(Collectors.toList());

        // 3. ส่ง DTO ไปให้ View
        model.addAttribute("loans", loanDtos);
        
        return "admin/pending_return";
    }
	
	 private PendingReturnLoanDto convertToPendingReturnDto(Loan loan) {
	        PendingReturnLoanDto dto = new PendingReturnLoanDto();
	        dto.setId(loan.getId());
	        dto.setAssetCode(loan.getItem().getAssetCode());
	        dto.setEquipName(loan.getItem().getEquip().getEquipName()); // สมมติตามโครงสร้าง
	        dto.setUserName(loan.getUser().getUsername());
	        //dto.setEquipmentName(loan.getItem().getEquip().getEquipName()); // (อาจจะต้องปรับ path ตามโครงสร้าง Entity ของคุณ)


	        // จัดรูปแบบวันที่
	        if (loan.getPendingReturnAt() != null) {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm 'น.'");
	            dto.setFormattedPendingReturnAt(loan.getPendingReturnAt().format(formatter));
	        }

	        // เตรียม URL รูปภาพ
	        if (loan.getReturnPhotoPath() != null && !loan.getReturnPhotoPath().isEmpty()) {
	            dto.setHasReturnPhoto(true);
	            dto.setReturnPhotoUrl("/storage/" + loan.getReturnPhotoPath());
	        } else {
	            dto.setHasReturnPhoto(false);
	        }

	        return dto;
	    }
	 
	 @PostMapping("/admin/loans/{id}/confirm-return")
	    public String confirmReturn(@PathVariable("id") Long loanId, RedirectAttributes redirectAttributes) {
	        try {
	            loanService.confirmReturn(loanId);
	            redirectAttributes.addFlashAttribute("successMessage", "ยืนยันการรับคืนสำเร็จ");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
	        }
	        return "redirect:/admin/loans/pending-return"; // กลับไปหน้าเดิม
	    }
	

}

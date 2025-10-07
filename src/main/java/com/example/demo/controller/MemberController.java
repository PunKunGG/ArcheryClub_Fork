package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.MemberDashboardViewModel;
import com.example.demo.model.Announcement;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.LoanService;

@Controller
@RequestMapping("/member")
@PreAuthorize("isAuthenticated()") // หรือจะใช้ hasRole('MEMBER') เพื่อความชัดเจนก็ได้
public class MemberController {

    private final LoanService loanService;

    public MemberController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        
        MemberDashboardViewModel viewModel = new MemberDashboardViewModel();
        
        // --- 1. เตรียมข้อมูลผู้ใช้ ---
        viewModel.setStudentId(user.getStudentid());
        viewModel.setUsername(user.getUsername());
        viewModel.setRoleDisplayName(getRoleDisplayName(user.getRole().name()));

        // --- 2. เตรียมข้อมูลการยืม ---
        List<Loan> activeLoans = loanService.findActiveLoansByUser(user.getId());
        viewModel.setHasLoans(!activeLoans.isEmpty());
        if (viewModel.isHasLoans()) {
            String summary = activeLoans.stream()
                .map(loan -> loan.getItem().getEquip().getEquipName() + " (" + loan.getItem().getAssetCode() + ")")
                .collect(Collectors.joining(", "));
            viewModel.setLoanedItemsSummary(summary);
        }

      /*  List<Announcement> announcements = announcementService.findAll();
        List<AnnouncementDto> announcementDtos = announcements.stream()
            .map(this::convertToAnnouncementDto)
            .collect(Collectors.toList());
        viewModel.setAnnouncements(announcementDtos); // อย่าลืมเพิ่ม Setter ใน ViewModel ของคุณ
*/
        // --- 4. ✅ นำ comment ออก เพื่อส่งข้อมูลไปที่หน้าเว็บ ---
        model.addAttribute("dashboard", viewModel);

        return "memberDashboard";
    }

    // --- Helper Methods ---
    private String getRoleDisplayName(String roleName) {
        switch (roleName) {
            case "SUPERADMIN": return "ประธานชมรม";
            case "ADMIN": return "กรรมการ";
            default: return "สมาชิกชมรม";
        }
    }

  /*  private AnnouncementDto convertToAnnouncementDto(Announcement announcement) {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setId(announcement.getId());
        dto.setMessage(announcement.getMessage());
        dto.setHasImage(announcement.getImgPath() != null && !announcement.getImgPath().isEmpty());
        if (dto.isHasImage()) {
            dto.setImageUrl("/storage/" + announcement.getImgPath());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
        if (announcement.getCreatedAt() != null) {
            dto.setFormattedCreatedAt(announcement.getCreatedAt().format(formatter));
        } else {
            dto.setFormattedCreatedAt("N/A");
        }
        return dto;
    }*/
}
package com.example.demo.controller;

import com.example.demo.dto.AdminDashboardViewModel;
import com.example.demo.dto.AnnouncementDto;
import com.example.demo.model.Announcement;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.LoanService;
import com.example.demo.service.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERADMIN')")
public class AdminController {

    private final LoanService loanService;
    private final UserService userService;

    // Constructor to inject services...
    public AdminController(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        
        AdminDashboardViewModel viewModel = new AdminDashboardViewModel();
        
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

        // --- 3. เตรียมข้อมูลประกาศ ---
       /* List<Announcement> announcements = announcementService.findAll();
        List<AnnouncementDto> announcementDtos = announcements.stream()
            .map(this::convertToAnnouncementDto)
            .collect(Collectors.toList());
        viewModel.setAnnouncements(announcementDtos);*/
        
        // --- 4. จัดการ Flash Message (ถ้ามี) ---
        if (redirectAttributes.getFlashAttributes().containsKey("success")) {
            viewModel.setSuccessMessage((String) redirectAttributes.getFlashAttributes().get("success"));
        }

        model.addAttribute("dashboard", viewModel);
        return "admin/adminDashboard";
    }
    
    @GetMapping("/member-list")
    public String showMemberList(Model model) {
        // ดึงข้อมูล User ทั้งหมดจาก Service
        model.addAttribute("users", userService.getAllUsers());
        // ส่งไปที่ไฟล์ HTML ที่จะสร้างในขั้นตอนต่อไป
        return "admin/member-list";
    }

    // --- Helper Methods (Logic อยู่ที่นี่ทั้งหมด) ---
    private String getRoleDisplayName(String roleName) {
        switch (roleName) {
            case "SUPERADMIN": return "ประธานชมรม";
            case "ADMIN": return "กรรมการ";
            default: return "สมาชิกชมรม";
        }
    }

 /*   private AnnouncementDto convertToAnnouncementDto(Announcement announcement) {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setId(announcement.getId());
        dto.setMessage(announcement.getMessage());
        dto.setHasImage(announcement.getImgPath() != null && !announcement.getImgPath().isEmpty());
        if (dto.isHasImage()) {
            dto.setImageUrl("/storage/" + announcement.getImgPath());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
        dto.setFormattedCreatedAt(announcement.getCreatedAt().format(formatter));
        return dto;
    }*/
    
    
    
}
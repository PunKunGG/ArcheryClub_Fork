package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // --- บรรทัดสำคัญที่สุด ---
        // เราจะพิมพ์ค่า Role ที่แท้จริงออกมาดูใน Console
        //System.out.println(">>>>>> AUTHENTICATION DETAILS: " + authentication.getAuthorities() + " <<<<<<");

        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ADMIN") ||
                grantedAuthority.getAuthority().equals("SUPERADMIN")
            );

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/member/dashboard";
        }
    }
}
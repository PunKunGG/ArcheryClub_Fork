package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Loan;
import com.example.demo.model.User;

public interface LoanService {
	
	/**
     * ค้นหารายการยืมทั้งหมดที่อยู่ในสถานะรอยืนยันการคืน
     */
    List<Loan> findPendingReturnLoans();
    
    Loan confirmReturn(Long loanId);

    /**
     * ดึงข้อมูลการยืมด้วย ID
     */
    Loan getById(Long id);

    /**
     * ดึงข้อมูลการยืมทั้งหมด
     */
    List<Loan> getAll();

    /**
     * สร้างรายการยืมใหม่สำหรับ Item และ User ที่ระบุ
     */
    Loan createLoan(Long itemId, User user);

    /**
     * ผู้ใช้แจ้งขอคืน Item พร้อมแนบไฟล์รูปภาพ
     */
    Loan requestReturn(Long loanId, MultipartFile file) throws Exception;
    
    Page<Loan> findUserHistory(Long userId, Pageable pageable);
    
    List<Loan> findActiveLoansByUser(Long userId);
    
    

}

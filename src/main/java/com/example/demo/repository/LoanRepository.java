package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.Loan;

public interface LoanRepository extends JpaRepository<Loan,Long> {
	boolean existsByItemAndReturnedAtIsNull(Item item);//for check item status
	 List<Loan> findByItemStatus(ItemStatus status);
	 
	 Page<Loan> findByUserId(Long userId, Pageable pageable);
	 
	 List<Loan> findByUserIdAndReturnedAtIsNull(Long userId);
	 
	 //api
	 // ดึงข้อมูลการยืมทั้งหมดที่ยังไม่เสร็จสิ้น (ยังไม่มีการคืน)
	    List<Loan> findAllByReturnedAtIsNull();
	    
	    // ดึงข้อมูลการยืมล่าสุดของ Item ID ที่กำหนด ซึ่งยังไม่มีการคืน
	    Optional<Loan> findFirstByItemIdAndReturnedAtIsNullOrderByIdDesc(Long itemId);

}

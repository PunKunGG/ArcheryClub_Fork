package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.FileStorageProperties;
import com.example.demo.dto.CreateLoanRequest;
import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.web.ResourceNotFoundException;


@Service
public class LoanServiceImpl implements LoanService{
	
	private final LoanRepository loanRepo;
	private final ItemRepository itemRepo;
	private final Path fileStorageLocation; 
	 
	 @Autowired
	public LoanServiceImpl(LoanRepository loanRepo,ItemRepository itemRepo,
			 FileStorageProperties fileStorageProperties) {
		this.loanRepo = loanRepo;
		this.itemRepo = itemRepo;
		
		 // ดึงค่า path จาก object ที่รับเข้ามา
        String uploadDir = fileStorageProperties.getUploadDir();
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
	}
	
	@Transactional(readOnly=true)
	public Loan getById(Long id) {
		return loanRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item %l not found".formatted(id)));
	}
	
	@Transactional(readOnly=true)
	public List<Loan> getAll(){
		return loanRepo.findAll();
	}
	
	@Transactional
	public Loan createLoan(Long itemId, User user) {
	    Item item = itemRepo.findById(itemId)
	        .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
		
		/*Loan newLoan = new Loan();
		newLoan.setItem(item);
		newLoan.setUser(user);
		newLoan.setDueDate(req.getDueDate());
		return loanRepo.save(newLoan);*/
		
		// 1. ตรวจสอบว่าสถานะว่าง
		if (item.getStatus() != ItemStatus.Available) {
		    throw new IllegalStateException("Item not available");
		}
	    // 2. สร้าง loan ใหม่
		Loan newLoan = new Loan();
	    newLoan.setItem(item);
	    newLoan.setUser(user);

	    LocalDateTime dueAtMidnight = LocalDate.now().atTime(23, 59, 59);
	    newLoan.setDueDate(dueAtMidnight);

	    item.setStatus(ItemStatus.Borrowed); // Spring จะอัปเดตให้โดยอัตโนมัติ

	    return loanRepo.save(newLoan);
	}
	
	@Transactional
	public Loan requestReturn(Long loanId,MultipartFile file) throws Exception {
		Loan loan = loanRepo.findById(loanId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

		Item itemToUpdate = loan.getItem();
	    itemToUpdate.setStatus(ItemStatus.Pending_Return); 
		 // 2. จัดการบันทึกไฟล์
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        
        Path targetLocation = this.fileStorageLocation.resolve(newFileName);
        
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 3. อัปเดตข้อมูลใน Entity Loan
        loan.setReturnPhotoPath(newFileName); 
        loan.setPendingReturnAt(LocalDateTime.now()); 

        // 4. บันทึกการเปลี่ยนแปลงลงฐานข้อมูล
Loan savedLoan = loanRepo.save(loan); // บันทึกการเปลี่ยนแปลง
        
        return savedLoan;
	}
	
	  @Transactional(readOnly = true)
	    public List<Loan> findPendingReturnLoans() {
	        // ผมได้เพิ่มโค้ดการทำงานจริงสำหรับเมธอดนี้ โดยไปค้นหา Item ที่มีสถานะเป็น Pending_Return
	        // ซึ่งสอดคล้องกับ Logic ในเมธอด requestReturn ของคุณครับ
	        return loanRepo.findByItemStatus(ItemStatus.Pending_Return);
	    }
	  
	  @Transactional
	    public Loan confirmReturn(Long loanId) {
	        // 1. ค้นหา Loan ที่ต้องการ
	        Loan loan = loanRepo.findById(loanId)
	            .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

	        // 2. ค้นหา Item ที่ผูกกับ Loan นี้
	        Item item = loan.getItem();

	        // 3. เปลี่ยนสถานะ Item กลับเป็น "Available"
	        item.setStatus(ItemStatus.Available);
	        itemRepo.save(item);

	        // 4. อัปเดตข้อมูลใน Loan (เช่น วันที่คืนจริง) และลบข้อมูลการแจ้งคืน
	        loan.setReturnedAt(LocalDateTime.now());
	        loan.setPendingReturnAt(null);
	        loan.setReturnPhotoPath(null);
	        Loan savedLoan = loanRepo.save(loan);

	        // 5. คืนค่า object ที่บันทึกแล้วกลับไปให้ Controller
	        return savedLoan; 
	        // หรือถ้าต้องการลบประวัติการยืมไปเลย ก็ใช้ loanRepo.delete(loan);
	    }
	  
	  @Transactional(readOnly = true)
	    public Page<Loan> findUserHistory(Long userId, Pageable pageable) {
	        // เรียกใช้เมธอดใหม่จาก Repository โดยส่ง pageable object ต่อไปให้
	        return loanRepo.findByUserId(userId, pageable);
	    }
	  
	  @Transactional(readOnly = true)
	    public List<Loan> findActiveLoansByUser(Long userId) {
	        // เรียกใช้เมธอดจาก Repository ที่เราสร้างไว้
	        return loanRepo.findByUserIdAndReturnedAtIsNull(userId);
	    }
	  
	  
}

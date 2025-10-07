package com.example.demo.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryResponse;
import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.UpdateCategoryRequest;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
	private final CategoryService service;
	
	public CategoryApiController(CategoryService service) {
		this.service = service;
	}
	
	 @GetMapping
	    public ResponseEntity<List<CategoryResponse>> getAll() {
	        List<CategoryResponse> response = service.getAll().stream()
	                .map(CategoryResponse::fromEntity).collect(Collectors.toList());
	        return ResponseEntity.ok(response);
	    }

	 @PostMapping
	    public ResponseEntity<CategoryResponse> create(
	            @Valid @RequestBody CreateCategoryRequest req,
	            @AuthenticationPrincipal User creator // 3. ดึง User ที่ล็อกอินอยู่
	    ) {
	        // 4. เรียกใช้เมธอดของ Service ที่ถูกต้อง
	        Category savedCategory = service.createCategory(req, creator);
	        
	        // 5. แปลง Entity ที่ได้กลับมาเป็น Response DTO
	        CategoryResponse response = CategoryResponse.fromEntity(savedCategory);
	        
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }
	 
	 @DeleteMapping("/{id}")
	 public ResponseEntity<Void> deleteCategory(@PathVariable Long id,@AuthenticationPrincipal User deleteBy) { // <-- เปลี่ยน return type เป็น Void
	     // ไม่ต้องเช็ค existsById ที่นี่แล้ว เพราะ Service จัดการให้แล้ว
	     service.softDelete(id,deleteBy);
	     
	     // เมื่อลบสำเร็จ คืนค่าเป็น 204 No Content ซึ่งเป็นมาตรฐานของ REST API สำหรับการลบ
	     return ResponseEntity.noContent().build(); 
	 }
	 
	 // PUT (Update)
	    @PutMapping("/{id}")
	    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest req) {
	        Category updated = service.updateCategory(id, req);
	        return ResponseEntity.ok(CategoryResponse.fromEntity(updated));
	    }
	 
	 
}

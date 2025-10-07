package com.example.demo.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.UpdateCategoryRequest;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.web.ResourceNotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {
	
	private final CategoryRepository repo;
	
	public CategoryService(CategoryRepository repo) {
		this.repo = repo;
	}
	
	 @Transactional
	    public Category createCategory(CreateCategoryRequest dto, User creator) {
	        Category newCategory = new Category();
	        newCategory.setName(dto.getName());
	        newCategory.setCreatedBy(creator);
	        return repo.save(newCategory);
	    }
	
	 @Transactional(readOnly = true)
	    public Category getById(Long id) {
	        return repo.findById(id)
	        		.orElseThrow(() -> new ResourceNotFoundException("Category %l not found".formatted(id)));
	    }
	 
	 @Transactional(readOnly = true)
	 public List<Category> getAll(){
		 return repo.findAll();
	 }
	 
	 @Transactional
	    public Category updateCategory(Long id, UpdateCategoryRequest dto) {
	        Category category = getById(id);
	        category.setName(dto.getName());
	        return repo.save(category); 
	    }
	 
	 public boolean isCategoryNameExists(String name) {
	        return repo.findByName(name).isPresent();
	    }
	 
	  // เปลี่ยนมาใช้ Method deleteById เพื่อให้ @SQLDelete ทำงาน
	    public void deleteById(Long id) {
	        if (!repo.existsById(id)) {
	            throw new ResourceNotFoundException("Cannot find Category with ID: " + id);
	        }
	        repo.deleteById(id);
	    }

	    // เพิ่ม Method กู้คืนข้อมูล 
	    public void restoreById(Long id) {
	        Category category = repo.findDeletedById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("ไม่พบหมวดหมู่ที่ถูกลบ ID: " + id));
	        category.setDeletedAt(null);
	        category.setDeletedBy(null);
	        repo.save(category);
	    }

	    // เพิ่ม Method ค้นหาข้อมูลในถังขยะ
	    public List<Category> findAllDeleted() {
	        return repo.findAllDeleted();
	    }
	 
	/* public void delete(Long id) {
		 if (!repo.existsById(id)) {
				throw new ResourceNotFoundException("Category %d not found".formatted(id));
			}
			repo.deleteById(id);
	 }*/
	 
	 public void softDelete(Long id, User deletedBy) {
	        // 1. ค้นหา Category ที่ต้องการลบ
	        Category category = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Cannot find Category with ID: " + id));

	        // 2. ตั้งค่าสถานะการลบ และระบุคนลบ
	        category.setDeletedAt(LocalDateTime.now());
	        category.setDeletedBy(deletedBy);

	        // 3. บันทึกการเปลี่ยนแปลง (เป็นการ UPDATE ไม่ใช่ DELETE)
	        repo.save(category);
	    }
	 
	 @Transactional(readOnly = true)
	 public String getCategoryNameById(Long id) {
	     Category category = getById(id);
	     return category.getName(); // คืนชื่อหมวดหมู่
	 }

}

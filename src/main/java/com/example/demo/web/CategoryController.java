package com.example.demo.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.CategoryResponse;
import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.UpdateCategoryRequest;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories") // แนะนำให้ใช้ @RequestMapping ที่ระดับคลาสเพื่อจัดกลุ่ม URL
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERADMIN')")
public class CategoryController {

    private final CategoryService service;

    // 1. แก้ไข Dependency Injection ให้ถูกต้อง
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // 2. ใช้ @GetMapping ว่างๆ เพื่อรับ Path จาก @RequestMapping ("/admin/categories")
    @GetMapping
    public String showCategoryPage(Model model) {
        model.addAttribute("categories", service.getAll());
        
        // ส่ง DTO เปล่าสำหรับฟอร์ม ถ้ายังไม่มี object ที่ถูกส่งกลับมาจากตอน validation error
        if (!model.containsAttribute("newCategory")) {
            model.addAttribute("newCategory", new CreateCategoryRequest());
        }
        
        return "admin/adminCategory"; // ชื่อไฟล์ HTML ของคุณ
    }

    // 3. ใช้ @PostMapping ว่างๆ เพื่อรับข้อมูลจากฟอร์มที่ส่งมาที่ "/admin/categories"
    @PostMapping
    public String processCreateCategory(
            @Valid @ModelAttribute("newCategory") CreateCategoryRequest req, // 4. แก้ชื่อ object ให้ตรงกับฟอร์ม
            BindingResult bindingResult,
            @AuthenticationPrincipal User creator,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
    	
    	if (service.isCategoryNameExists(req.getName())) {
            // ผูก Error เข้ากับ field 'name' ในฟอร์ม
            bindingResult.rejectValue("name", "error.category", "ชื่อหมวดหมู่นี้มีอยู่ในระบบแล้ว");
        }
    	
        // ถ้า Validation ไม่ผ่าน ให้กลับไปที่หน้าเดิมพร้อม error
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", service.getAll()); 
            return "admin/adminCategory"; // 5. แก้ไข return path ให้กลับไปที่หน้าฟอร์มเดิม
        }

        service.createCategory(req, creator);
        redirectAttributes.addFlashAttribute("successMessage", "บันทึกหมวดหมู่ใหม่เรียบร้อยแล้ว");
        
        return "redirect:/admin/categories"; // Redirect กลับไปที่หน้าแสดงรายการ
    }
    
    @PostMapping("/{id}/delete")
    public String processDeleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // เรียกใช้ method deleteById เพื่อให้ @SQLDelete ทำงาน
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "ลบหมวดหมู่เรียบร้อยแล้ว");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/categories";
    }
    
    @GetMapping("/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
    	Category editCategory = service.getById(id);
    	UpdateCategoryRequest updateDto = new UpdateCategoryRequest();
    	updateDto.setName(editCategory.getName());
    	updateDto.setId(editCategory.getId());
    	model.addAttribute("updateCategory",updateDto);
    	return "editCategory";
    }
    
    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable Long id,@Valid @ModelAttribute("updateCategory") UpdateCategoryRequest req,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
    	 if (bindingResult.hasErrors()) {
    	        return "editCategory"; 
    	    }

    	    try {
    	        service.updateCategory(id, req);
    	        redirectAttributes.addFlashAttribute("successMessage", "แก้ไขหมวดหมู่เรียบร้อยแล้ว");
    	    } catch (ResourceNotFoundException ex) {
    	        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
    	    }
    	    
    	    return "redirect:/admin/categories";
    }
    
    @GetMapping("/trash")
    public String showTrashPage(Model model) {
        model.addAttribute("deletedCategories", service.findAllDeleted());
        // คุณจะต้องสร้างไฟล์ HTML ที่ชื่อว่า "categoryTrash.html" ด้วยนะครับ
        return "admin/categoryTrash";
    }

    @PostMapping("/{id}/restore")
    public String processRestoreCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.restoreById(id);
            redirectAttributes.addFlashAttribute("successMessage", "กู้คืนหมวดหมู่สำเร็จแล้ว");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/categories/trash";
    }
}
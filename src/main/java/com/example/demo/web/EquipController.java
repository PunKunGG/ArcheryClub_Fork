package com.example.demo.web;

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

import com.example.demo.dto.CreateEquipRequest;
import com.example.demo.dto.UpdateEquipRequest;
import com.example.demo.model.Equipment;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EquipService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/equips")
public class EquipController {
	
	private final EquipService service;
	private final CategoryService categoryService;
	
	public EquipController(EquipService service,CategoryService categoryService) {
		this.service = service;
		this.categoryService = categoryService; 
	}
	
	@GetMapping
	public String showEquip(Model model) {
		model.addAttribute("equips",service.getAll());
		/*if(!model.containsAttribute("newEquip")) {
			model.addAttribute("newEquip",new CreateEquipRequest());
		}*/
		return "admin/adminEquip";
	}
	
	@GetMapping("/add")
    public String showAddForm(Model model) {
        // 1. สร้าง DTO เปล่าๆ สำหรับผูกกับฟอร์ม
        model.addAttribute("newEquip", new CreateEquipRequest());
        // 2. ดึงข้อมูล Categories ทั้งหมดเพื่อไปสร้าง dropdown
        model.addAttribute("categories", categoryService.getAll());
        // 3. บอกให้แสดงผลด้วยไฟล์ HTML ที่ชื่อว่า "addEquip.html"
        return "admin/addEquip"; 
    }
	
	@PostMapping("/add")
	public String createEquip(@Valid @ModelAttribute("newEquip") CreateEquipRequest req,
			BindingResult bindingResult,
			@AuthenticationPrincipal User creator,
            RedirectAttributes redirectAttributes,
            Model model
	) {
		
		if (service.isEquipNameExists(req.getEquipName())) {
            // เพิ่มข้อความ error กลับไปที่ Model เพื่อให้ฟอร์มแสดงผล
            // ใช้ withInput() เพื่อส่งข้อมูลที่กรอกไว้กลับไปที่ฟอร์มด้วย
            redirectAttributes.addFlashAttribute("errorMessage", "ชื่ออุปกรณ์ '" + req.getEquipName() + "' มีอยู่ในระบบแล้ว");
            redirectAttributes.addFlashAttribute("newEquip", req); // ส่ง object ที่กรอกไว้กลับไป
            return "redirect:/admin/equips/add"; // Redirect กลับไปที่หน้าฟอร์ม
        }
		
		if (service.isPrefixExists(req.getPrefix())) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Prefix '" + req.getPrefix() + "' มีอุปกรณ์อื่นใช้งานแล้ว");
	        redirectAttributes.addFlashAttribute("newEquip", req);
	        return "redirect:/admin/equips/add"; // กลับไปหน้าฟอร์ม
	    }
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("equips",service.getAll());
			return "admin/addEquip";
		}
		
		service.createEquip(req, creator);
		redirectAttributes.addFlashAttribute("successMessage","บันทึกอุปกรณ์เรียบร้อยแล้ว");
		
		return "redirect:/admin/equips";
	}
	
	 @PostMapping("/{id}/delete")
	    public String deleteEquip(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        try {
	            // เปลี่ยนมาเรียกใช้ deleteById เพื่อให้ @SQLDelete ทำงาน
	            service.deleteById(id);
	            redirectAttributes.addFlashAttribute("successMessage", "ลบอุปกรณ์เรียบร้อยแล้ว");
	        } catch (ResourceNotFoundException ex) {
	            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
	        }
	        return "redirect:/admin/equips";
	    }
	 
	 @GetMapping("/{id}/edit")
	 public String editEquip(@PathVariable Long id,Model model) {
		 Equipment equip = service.getById(id);
		 UpdateEquipRequest updateDto = new UpdateEquipRequest();
		 updateDto.setId(equip.getId());
		 updateDto.setCategoryId(equip.getCategory().getId());
		 updateDto.setEquipName(equip.getEquipName());
		 updateDto.setPrefix(equip.getPrefix());
		 model.addAttribute("updateEquip",updateDto);
		 model.addAttribute("categories", categoryService.getAll());
		 return "admin/editEquip";
	 }
	 
	 @PostMapping("/{id}/update")
	 public String updateEquip(@PathVariable Long id,@Valid @ModelAttribute("updateEquip") UpdateEquipRequest req,
	            BindingResult bindingResult,
	            RedirectAttributes redirectAttributes,
	            Model model) {
	    	 if (bindingResult.hasErrors()) {
	    		 model.addAttribute("categories", categoryService.getAll());
	    	        return "admin/editEquip"; 
	    	    }

	    	    try {
	    	        service.updateEquip(id, req);
	    	        redirectAttributes.addFlashAttribute("successMessage", "แก้ไขอุปกรณ์เรียบร้อยแล้ว");
	    	    } catch (ResourceNotFoundException ex) {
	    	        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
	    	    }
	    	    
	    	    return "redirect:/admin/equips";
	    }
	 
	  @GetMapping("/trash")
	    public String showTrashPage(Model model) {
	        // ไปดึงข้อมูลทั้งหมดที่ถูก "soft delete" มาแสดง
	        model.addAttribute("deletedEquips", service.findAllDeleted());
	        return "admin/equipTrash"; // <<< สร้างไฟล์ HTML ชื่อ equipTrash.html
	    }

	    // ✅ 2. เพิ่ม POST Mapping สำหรับการกู้คืน
	    @PostMapping("/{id}/restore")
	    public String processRestoreEquip(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        try {
	            service.restoreById(id);
	            redirectAttributes.addFlashAttribute("successMessage", "กู้คืนอุปกรณ์สำเร็จแล้ว");
	        } catch (ResourceNotFoundException e) {
	            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
	        }
	        // จะให้กลับไปหน้าถังขยะ หรือหน้าหลักก็ได้
	        return "redirect:/admin/equips/trash";
	    }

}

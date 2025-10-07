package com.example.demo.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.demo.dto.CreateItemRequest;
import com.example.demo.dto.ItemSummaryViewModel;
import com.example.demo.dto.UpdateEquipRequest;
import com.example.demo.dto.UpdateItemRequest;
import com.example.demo.model.Equipment;
import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.User;
import com.example.demo.service.EquipService;
import com.example.demo.service.ItemService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/items")
public class ItemController {
	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
	  
	private final EquipService equipService;
	private final ItemService itemService;
	
	public ItemController(EquipService equipService, ItemService itemService) {
		this.equipService = equipService;
		this.itemService = itemService;
	}
	
	/*@GetMapping
	public String showItem(Model model) {
		List<Equipment> equips = equipService.getAll();
		model.addAttribute("items",itemService.getAll());
		// ส่งไปที่ view
	    model.addAttribute("equips", equips);
		model.addAttribute("statuses",ItemStatus.values());
		model.addAttribute("bulkUpdateRequest",new UpdateItemRequest());
		return "adminItem";
	}*/
	
	@GetMapping
    public String showItemSummary(Model model) {
        // 1. เรียก Service เพื่อเอา "กล่อง" ข้อมูลทั้งหมดที่เตรียมไว้แล้วมา
        ItemSummaryViewModel summary = equipService.getEquipmentStockSummary();
        
        // 2. ส่ง "กล่อง" ทั้งใบไปให้ View ภายใต้ชื่อ "summary"
        model.addAttribute("summary", summary);

        return "admin/adminItem";
    }



	
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("newItem",new CreateItemRequest());
		/*model.addAttribute("equip",equipService.getAll());*/
		List<Equipment> equipmentList = equipService.getAll();
		if(equipmentList == null) {
		    equipmentList = Collections.emptyList();
		}
		model.addAttribute("equip", equipmentList);
// --- เพิ่มบรรทัดนี้เพื่อ Debug ---
	    logger.info("Content of 'equip' in model: {}", model.getAttribute("equip"));

		return "admin/addItem";
		
	/*	 List<Equipment> allEquipment = equipService.getAll(); // ตัวอย่างการดึงข้อมูล
	        
	        // 2. เพิ่ม List เข้าไปใน Model และต้องตั้งชื่อให้ตรงกับใน HTML
	        //    บรรทัดนี้สำคัญมาก! ชื่อ "equip" ต้องตรงกับ ${equip}
	        model.addAttribute("equip", allEquipment); 
	        
	        // 3. ส่ง Model ที่มีข้อมูลไปยังหน้า addItem.html
	        return "addItem"; */
	}
	
	@PostMapping("/add")
	public String createItem(@Valid @ModelAttribute("newItem") CreateItemRequest req,
			BindingResult bindingResult,
			@AuthenticationPrincipal User creator,
            RedirectAttributes redirectAttributes,
            Model model) 
	{
			if(bindingResult.hasErrors()) {
				List<Equipment> equipmentList = equipService.getAll();
	            model.addAttribute("equip", equipmentList != null ? equipmentList : Collections.emptyList());
	            return "admin/addItem";
			}
			
			itemService.createMultipleItems(req, creator);
			redirectAttributes.addFlashAttribute("successMessage","บันทึกอุปกรณ์เรียบร้อยแล้ว");
			
			return "redirect:/admin/items";
	}
	
/*	@GetMapping("/bulk-edit")
    public String editItem(@PathVariable Long id, Model model) {
        Item item = itemService.getById(id);
        UpdateItemRequest updateDto = new UpdateItemRequest();
        
        // แนะนำ: ควรตั้งชื่อ model attribute ให้สอดคล้องกัน
        // ในที่นี้หน้า add ใช้ "equip" แต่หน้า edit ใช้ "categories"
        model.addAttribute("item", item); // ส่ง item ที่จะแก้ไขไปด้วย
        model.addAttribute("updateItem", updateDto);

        // เพื่อความสอดคล้อง ควรใช้ชื่อ "equip" หรือ "equipments" เหมือนกับหน้า add
        model.addAttribute("equipments", equipService.getAll()); 
        model.addAttribute("statuses", ItemStatus.values());
        return "editBulkItem";
    }
	*/
	
	/*@GetMapping("/bulk-edit")
	public String editItem(Model model) {
	    model.addAttribute("items", itemService.getAll()); // ส่งหลายอัน
	    model.addAttribute("equipments", equipService.getAll());
	    model.addAttribute("statuses", ItemStatus.values());

	    return "admin/editBulkItem";
	}*/
	
	@GetMapping("/bulk-edit")
	public String showBulkEditForm(Model model, 
            @RequestParam(name = "equipId", required = false) Long equipId) {

List<Item> itemsToDisplay;

// ถ้ามี equipId ส่งมาด้วย (เช่น /bulk-edit?equipId=1)
if (equipId != null) {
// ให้ดึง Item เฉพาะของ Equipment นั้นๆ
itemsToDisplay = itemService.findByEquipmentId(equipId);
// (ทางเลือก) ส่งชื่อ Equipment ไปแสดงที่หัวข้อด้วย
Equipment filteredEquipment = equipService.getById(equipId);
model.addAttribute("filteredEquipmentName", filteredEquipment.getEquipName());
} else {
// ถ้าไม่มี equipId ส่งมา ก็แสดง Item ทั้งหมดเหมือนเดิม
itemsToDisplay = itemService.getAll();
}

model.addAttribute("items", itemsToDisplay);
model.addAttribute("equipments", equipService.getAll());
model.addAttribute("statuses", ItemStatus.values());
model.addAttribute("bulkUpdateRequest", new UpdateItemRequest()); 

return "admin/editBulkItem";
}

	
	
	
	@PostMapping("/bulk-update")
	 public String updateEquip(
	            @Valid @ModelAttribute("bulkUpdateRequest") UpdateItemRequest req,
	            BindingResult bindingResult,
	            RedirectAttributes redirectAttributes) {
		 if (bindingResult.hasErrors()) {
	            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
	            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + errorMessage);
	            return "redirect:/admin/items";
	        }

	        itemService.updateItem(req.getItemIds(), req.getNewStatus());
	        redirectAttributes.addFlashAttribute("successMessage", "อัปเดตสถานะ " + req.getItemIds().size() + " รายการเรียบร้อยแล้ว");

	        return "redirect:/admin/items";
	    }
}
package com.example.demo.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ItemResponse;
import com.example.demo.dto.UpdateItemRequest;
import com.example.demo.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemRestController {
	
	 private final ItemService itemService;

	    public ItemRestController(ItemService itemService) {
	        this.itemService = itemService;
	    }

	    // GET /api/items -> ดึงข้อมูล Item ทั้งหมดพร้อมสถานะการยืม
	    @GetMapping
	    public List<ItemResponse> getAllItemsWithLoanStatus() {
	        // เราจะสร้างเมธอดใหม่ใน Service เพื่อจัดการความซับซ้อนทั้งหมด
	        return itemService.getAllItemsForApi();
	    }
	    
	    @PostMapping("/bulk-status-update")
	    public ResponseEntity<?> bulkUpdateItemStatus(
	            @Valid @RequestBody UpdateItemRequest req) {
	        
	        try {
	            itemService.updateItem(req.getItemIds(), req.getNewStatus());
	            
	            String message = "Successfully updated status of " + req.getItemIds().size() + " items to " + req.getNewStatus().name();
	            
	            // สร้าง Response object แบบง่ายๆ เพื่อส่งข้อความกลับไป
	            Map<String, Object> response = Map.of(
	                "message", message,
	                "updatedCount", req.getItemIds().size()
	            );

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            // จัดการ Error กรณีที่เกิดปัญหา (เช่น item id ไม่มีอยู่จริง)
	            Map<String, String> errorResponse = Map.of("error", e.getMessage());
	            return ResponseEntity.badRequest().body(errorResponse);
	        }
	    }

	    // GET /api/items/{id} -> ดึงข้อมูล Item ชิ้นเดียว
	    @GetMapping("/{id}")
	    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
	        ItemResponse response = itemService.getItemByIdForApi(id);
	        return ResponseEntity.ok(response);
	    }
	    
	    

}

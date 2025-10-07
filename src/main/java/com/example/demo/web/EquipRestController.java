package com.example.demo.web;

import java.util.List;

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

import com.example.demo.dto.CreateEquipRequest;
import com.example.demo.dto.EquipResponse;
import com.example.demo.dto.UpdateEquipRequest;
import com.example.demo.model.Equipment;
import com.example.demo.model.User;
import com.example.demo.service.EquipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/equips")
public class EquipRestController {
    
	private final EquipService equipService;

    public EquipRestController(EquipService equipService) {
        this.equipService = equipService;
    }

    
    @GetMapping
    public ResponseEntity<List<EquipResponse>> getAllEquipments() {
        List<EquipResponse> equipments = equipService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }
    
    @PostMapping
    public ResponseEntity<EquipResponse> createEquipment(
            @Valid @RequestBody CreateEquipRequest req,
            @AuthenticationPrincipal User creator) {
        
        Equipment newEquipEntity = equipService.createEquip(req, creator);
        EquipResponse response = equipService.getEquipmentById(newEquipEntity.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipResponse> getEquipmentById(@PathVariable Long id) {
        EquipResponse equip = equipService.getEquipmentById(id);
        // การจัดการ not found ใน service จะดีกว่า แต่แบบนี้ก็ใช้ได้
        return ResponseEntity.ok(equip);
    }

    // --- NEW METHOD: PUT (Update) ---
    @PutMapping("/{id}")
    public ResponseEntity<EquipResponse> updateEquipment(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateEquipRequest req) {
        
        EquipResponse updatedEquip = equipService.updateEquip(id, req);
        return ResponseEntity.ok(updatedEquip);
    }

    // --- NEW METHOD: DELETE (Soft Delete) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(
            @PathVariable Long id, 
            @AuthenticationPrincipal User remover) {
        
        // เราจะสร้างเมธอด softDelete ใน Service เพื่อรับ User ที่เป็นคนลบด้วย
        equipService.softDelete(id, remover);
        return ResponseEntity.noContent().build(); // คืนค่า 204 No Content
    }

}

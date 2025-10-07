package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateEquipRequest;
import com.example.demo.dto.EquipResponse;
import com.example.demo.dto.EquipmentSummaryDto;
import com.example.demo.dto.ItemSummaryViewModel;
import com.example.demo.dto.UpdateEquipRequest;
import com.example.demo.model.Category;
import com.example.demo.model.Equipment;
import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EquipRepository;
import com.example.demo.web.ResourceNotFoundException;



@Service
public class EquipService {
	
	private final CategoryRepository categoryRepo;
	 @Autowired
	private final EquipRepository equipRepo;

	
	public EquipService(EquipRepository equipRepo,CategoryRepository categoryRepo) {
		this.equipRepo = equipRepo;
		this.categoryRepo = categoryRepo;
	}
	
	@Transactional
    public Equipment createEquip(CreateEquipRequest req, User creator) {
        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Equipment newEquipment = new Equipment();
        newEquipment.setEquipName(req.getEquipName());
        newEquipment.setPrefix(req.getPrefix());
        newEquipment.setCategory(category);
        newEquipment.setAddBy(creator);
        return equipRepo.save(newEquipment);
    }
	
	// ✅ แก้ไข: Method `getById` ให้ดึงเฉพาะข้อมูลที่ยังไม่ถูกลบ
		@Transactional(readOnly=true)
		public Equipment getById(Long id) {
		    return equipRepo.findByIdAndDeletedAtIsNull(id) // <<< เปลี่ยนมาเรียกใช้ method ใหม่
		        	.orElseThrow(() -> new ResourceNotFoundException("Equip %d not found".formatted(id)));
		}
	
	 
	/* @Transactional(readOnly = true)
	 public List<Equipment> getAll(){
		 return equipRepo.findAll();
	 }*/
	
	// ✅ แก้ไข: Method `getAll` ให้ดึงเฉพาะข้อมูลที่ยังไม่ถูกลบ
    @Transactional(readOnly = true)
    public List<Equipment> getAll() {
        return equipRepo.findAllByDeletedAtIsNull(); // <<< เปลี่ยนมาเรียกใช้ method ใหม่
    }
	
    
	
	/*@Transactional(readOnly = true)
	public List<Equipment> getAll(){
	    List<Equipment> list = equipRepo.findAll();
	    return list != null ? list : Collections.emptyList();
	}*/

	 
	/* @Transactional
	 public Equipment updateEquip(Long id,UpdateEquipRequest dto) {
		 Equipment equip = getById(id);
		 Category category = categoryRepo.findById(dto.getCategoryId())
				 .orElseThrow(() -> new ResourceNotFoundException("Cannot find Category with ID: " + id));
		 equip.setEquipName(dto.getEquipName());
		 equip.setPrefix(dto.getPrefix());
		 equip.setCategory(category);
		 return equipRepo.save(equip);
	 }*/
    
    @Transactional
	 public EquipResponse updateEquip(Long id,UpdateEquipRequest dto) { // <-- 1. เปลี่ยน return type
		 Equipment equip = getById(id);
		 Category category = categoryRepo.findById(dto.getCategoryId())
				 .orElseThrow(() -> new ResourceNotFoundException("Cannot find Category with ID: " + dto.getCategoryId()));
		 equip.setEquipName(dto.getEquipName());
		 equip.setPrefix(dto.getPrefix());
		 equip.setCategory(category);
		 
        Equipment savedEquip = equipRepo.save(equip);
		 
        // 2. แปลง Entity ที่บันทึกแล้วให้เป็น DTO ก่อนส่งกลับ
        Equipment detailedEquip = equipRepo.findByIdWithDetails(savedEquip.getId()).get();
        return EquipResponse.fromEntity(detailedEquip);
	 }
	 
	 public void deleteById(Long id) {
	        if (!equipRepo.existsById(id)) {
	            throw new ResourceNotFoundException("Cannot find Equip with ID: " + id);
	        }
	        // คำสั่งนี้จะไป Trigger @SQLDelete ใน Entity โดยอัตโนมัติ
	        equipRepo.deleteById(id);
	    }
		 
		 // ... method อื่นๆ ที่เหลือ (isEquipNameExists, restoreById, findAllDeleted) ยังคงเหมือนเดิม ...
		 
		 public boolean isEquipNameExists(String equipName) {
		        return equipRepo.findByEquipName(equipName).isPresent();
		    }
		 
		 public boolean isPrefixExists(String prefix) {
		        return equipRepo.findByPrefix(prefix).isPresent();
		    }
		 
		 public void restoreById(Long id) {
		        Equipment equip = equipRepo.findDeletedById(id)
		                .orElseThrow(() -> new ResourceNotFoundException("ไม่พบอุปกรณ์ที่ถูกลบ ID: " + id));
		        equip.setDeletedAt(null);
		        equip.setRemoveBy(null);
		        equipRepo.save(equip);
		    }

		    public List<Equipment> findAllDeleted() {
	            // โค้ดสำหรับ Debug ที่เคยใส่ไว้ สามารถเอาออกได้
		        return equipRepo.findAllByDeletedAtIsNotNull();
		    }
		    
		    @Transactional(readOnly = true)
		    public ItemSummaryViewModel getEquipmentStockSummary() {
		        // 1. ดึงข้อมูล Equip ทั้งหมด (ที่ยังไม่ถูกลบ)
		        List<Equipment> equips = equipRepo.findAll();

		        // 2. สร้าง Map สำหรับเก็บผลการนับ
		        Map<Long, Map<String, Long>> equipStatusCounts = new HashMap<>();
		        
		        // 3. วน Loop เพื่อนับจำนวน Item ตามสถานะ (Logic เดิมที่อยู่ใน Controller)
		        for (Equipment equip : equips) {
		            Map<String, Long> counts = new HashMap<>();
		            if (equip.getItems() != null) {
		                for (Item item : equip.getItems()) {
		                    String statusName = item.getStatus().name();
		                    counts.put(statusName, counts.getOrDefault(statusName, 0L) + 1);
		                }
		            }
		            equipStatusCounts.put(equip.getId(), counts);
		        }

		        // 4. สร้าง "กล่อง" ViewModel แล้วบรรจุข้อมูลทั้งหมดลงไป
		        ItemSummaryViewModel viewModel = new ItemSummaryViewModel();
		        viewModel.setEquipments(equips);
		        viewModel.setStatusCounts(equipStatusCounts);
		        viewModel.setStatuses(Arrays.asList(ItemStatus.values()));

		        return viewModel; // 5. ส่ง "กล่อง" ที่มีข้อมูลครบถ้วนกลับไปให้ Controller
		    }
		    
		    public List<EquipResponse> getAllEquipments() {
		        // เรียกใช้เมธอดใหม่ที่ใช้ JOIN FETCH
		        return equipRepo.findAllWithDetails().stream()
		                .map(EquipResponse::fromEntity) // แปลงเป็น DTO ที่นี่ ภายใน Transaction
		                .collect(Collectors.toList());
		    }
		    
		    @Transactional
		    public void softDelete(Long id, User remover) {
		        Equipment equip = equipRepo.findById(id)
		            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

		        equip.setDeletedAt(LocalDateTime.now());
		        equip.setRemoveBy(remover); // << บันทึกคนลบ
		        
		        equipRepo.save(equip);
		    }
		    
		    @Transactional(readOnly = true)
		    public EquipResponse getEquipmentById(Long id) {
		        // ใช้ Repository ที่ Join Fetch มาเพื่อป้องกัน LazyInitializationException
		        Equipment equip = equipRepo.findByIdWithDetails(id) 
		                .orElseThrow(() -> new ResourceNotFoundException("Equip not found with id: " + id));
		        
		        // แปลง Entity เป็น DTO ก่อนส่งกลับ
		        return EquipResponse.fromEntity(equip);
		    }
		    
		    
}

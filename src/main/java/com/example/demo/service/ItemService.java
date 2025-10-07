package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateItemRequest;
import com.example.demo.dto.ItemResponse;
import com.example.demo.dto.ItemSummaryViewModel;
import com.example.demo.model.Equipment;
import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.repository.EquipRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.web.ResourceNotFoundException;

@Service
public class ItemService {
	@Autowired
	private final ItemRepository itemRepo;
	private final EquipRepository equipRepo;
	private final LoanRepository loanRepo;
	
	// สร้าง Object เปล่าๆ ขึ้นมาเพื่อใช้เป็นตัว Lock ป้องกัน Race Condition
    private final Object lock = new Object();
	
	public ItemService(ItemRepository itemRepo,EquipRepository equipRepo,LoanRepository loanRepo) {
		this.equipRepo = equipRepo;
		this.itemRepo = itemRepo;
		this.loanRepo = loanRepo;
	}
	
	@Transactional(readOnly = true)
    public ItemSummaryViewModel getEquipmentStockSummary() {
        List<Equipment> equips = equipRepo.findAll(); // ดึงข้อมูล Equip ทั้งหมด

        // Logic การนับที่ย้ายมาจาก Controller
        Map<Long, Map<String, Long>> equipStatusCounts = new HashMap<>();
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

        // สร้าง ViewModel แล้วบรรจุข้อมูลทั้งหมดลงไป
        ItemSummaryViewModel viewModel = new ItemSummaryViewModel();
        viewModel.setEquipments(equips);
        viewModel.setStatusCounts(equipStatusCounts);
        viewModel.setStatuses(Arrays.asList(ItemStatus.values()));

        return viewModel; // ส่ง "กล่อง" ที่มีข้อมูลครบถ้วนกลับไป
    }
	
	@Transactional(readOnly=true)
	public Item getById(Long id) {
		return itemRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item %l not found".formatted(id)));
	}
	
	@Transactional(readOnly=true)
	public List<Item> getAll(){
		return itemRepo.findAll();
	}
	
	@Transactional
	public List<Item> createMultipleItems(CreateItemRequest req, User creator) {
		Equipment equip = equipRepo.findById(req.getEquipmentId())
				.orElseThrow(() -> new ResourceNotFoundException("Equip not found with id: " + req.getEquipmentId()));
		
		List<Item> itemsToSave = new ArrayList<>();
        
        synchronized (lock) {
            //ค้นหาเลขรันล่าสุดจาก DB โดยใช้เมธอดที่เราสร้างใหม่
        	Long lastRunningNo = itemRepo.findMaxRunningNoByEquipId(equip.getId());
        	if (lastRunningNo == null) {
        	    lastRunningNo = 0L;
        	}

            //วนลูปสร้าง Item ตามจำนวน quantity
            for (int i = 0; i < req.getQuantity(); i++) {
                
                lastRunningNo++;
                
                String assetCode = equip.getPrefix() + "-" + String.format("%04d", lastRunningNo);
                
                Item newItem = new Item();
                newItem.setEquip(equip);
                newItem.setCreatedBy(creator);
                newItem.setAssetCode(assetCode);
                newItem.setRunningNo(lastRunningNo); // กำหนดเลขรันให้ Item แต่ละชิ้น

                itemsToSave.add(newItem);
            }
        } //จบ synchronized

        //บันทึก Item ทั้งหมดที่สร้างขึ้นในครั้งเดียว และ return ผลลัพธ์
        return itemRepo.saveAll(itemsToSave);
	}
	
	@Transactional
	public void updateItem(List<Long> itemIds, ItemStatus newStatus) {
	    List<Item> items = itemRepo.findAllById(itemIds);
	    for (Item item : items) {
	        item.setStatus(newStatus);
	    }
	    itemRepo.saveAll(items);
	}
	
	@Transactional(readOnly = true)
    public List<Item> findByEquipmentId(Long equipId) {
        return itemRepo.findByEquipId(equipId);
    }
	
	  @Transactional(readOnly = true)
	    public List<Item> findItemsForLoanPage(Long categoryId) {
	        if (categoryId != null && categoryId > 0) {
	            // ถ้ามี categoryId ถูกส่งมา, ให้ค้นหาตาม category
	            return itemRepo.findByCategoryId(categoryId);
	        } else {
	            // ถ้าไม่มี categoryId (เป็น null), ให้ดึงข้อมูลทั้งหมด
	            return itemRepo.findAll();
	        }
	    }
	  
	  // --- 3. เพิ่มเมธอดใหม่สำหรับ GET /api/items ---
	    @Transactional(readOnly = true)
	    public List<ItemResponse> getAllItemsForApi() {
	        // a. ดึงข้อมูลหลักทั้งหมดที่จำเป็น
	        List<Item> allItems = itemRepo.findAllWithEquipDetails(); // สร้างเมธอดนี้ใน Repository
	        List<Loan> activeLoans = loanRepo.findAllByReturnedAtIsNull(); // ดึงข้อมูลการยืมที่ยังไม่คืนทั้งหมด

	        // b. สร้าง Map เพื่อให้ค้นหา Loan ของแต่ละ Item ได้เร็วขึ้น
	        Map<Long, Loan> itemToLoanMap = activeLoans.stream()
	                .collect(Collectors.toMap(loan -> loan.getItem().getId(), loan -> loan));

	        // c. วนลูปเพื่อแปลง Item แต่ละชิ้นให้เป็น DTO ที่สมบูรณ์
	        return allItems.stream()
	                .map(item -> {
	                    Loan loanForItem = itemToLoanMap.get(item.getId());
	                    return ItemResponse.fromEntity(item, loanForItem); // ส่ง cả Item และ Loan (ที่อาจเป็น null) ไปให้ DTO
	                })
	                .collect(Collectors.toList());
	    }
	    
	// --- 4. เพิ่มเมธอดใหม่สำหรับ GET /api/items/{id} ---
	    @Transactional(readOnly = true)
	    public ItemResponse getItemByIdForApi(Long id) {
	        Item item = itemRepo.findByIdWithEquipDetails(id) // สร้างเมธอดนี้ใน Repository
	                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
	        
	        // ค้นหาการยืมล่าสุดของ Item ชิ้นนี้ที่ยังไม่ถูกคืน
	        Loan loanForItem = loanRepo.findFirstByItemIdAndReturnedAtIsNullOrderByIdDesc(id)
	                .orElse(null); // ถ้าไม่เจอก็เป็น null

	        return ItemResponse.fromEntity(item, loanForItem);
	    }

}

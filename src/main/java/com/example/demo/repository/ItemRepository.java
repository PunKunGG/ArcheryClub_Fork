package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Item;

import jakarta.persistence.LockModeType;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>{
	
	// Spring Data JPA จะสร้าง query ให้เองจากชื่อเมธอด
    //Optional<Item> findByAssetCode(String assetCode);
	
	// เมธอดนี้จะดึงข้อมูล Equipment พร้อมกับทำการ "Lock" แถวนั้นในฐานข้อมูล
    // เพื่อป้องกันไม่ให้ Transaction อื่นเข้ามาอ่านหรือแก้ไขข้อมูลนี้จนกว่าเราจะทำงานเสร็จ
	/*@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Item> findWithLockingById(Long id);*/
	
	// ใช้ JPQL Query เพื่อหาค่า runningNo ที่มากที่สุดของ equip_id ที่ระบุ
    // COALESCE(..., 0) หมายถึง ถ้ายังไม่มีข้อมูลให้ใช้ค่า 0 แทน
    @Query("SELECT COALESCE(MAX(i.runningNo), 0) FROM Item i WHERE i.equip.id = :equipId")
    Long findMaxRunningNoByEquipId(@Param("equipId") Long equipId);

    // Spring Data JPA จะสร้าง Query ให้เองจากชื่อ Method
    // เพื่อค้นหา Item ทั้งหมดที่มี equip.id ตรงกับที่ส่งมา
  /*  @Query("SELECT i FROM Item i JOIN FETCH i.equip")
    List<Item> findByEquipId(Long equipId);*/
    
    @Query("SELECT i FROM Item i JOIN FETCH i.equip WHERE i.equip.id = :equipId")
    List<Item> findByEquipId(@Param("equipId") Long equipId);
    
    // เราใช้ JOIN เพื่อเชื่อมโยง Item -> Equipment -> Category
    @Query("SELECT i FROM Item i JOIN i.equip e WHERE e.category.id = :categoryId")
    List<Item> findByCategoryId(@Param("categoryId") Long categoryId);
    
    //api
 // ดึง Item พร้อม Equipment และ Category เพื่อแก้ปัญหา N+1
    @Query("SELECT i FROM Item i JOIN FETCH i.equip e JOIN FETCH e.category")
    List<Item> findAllWithEquipDetails();

    @Query("SELECT i FROM Item i JOIN FETCH i.equip e JOIN FETCH e.category WHERE i.id = :id")
    Optional<Item> findByIdWithEquipDetails(@Param("id") Long id);
    

}

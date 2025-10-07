package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Equipment;

@Repository
public interface EquipRepository extends JpaRepository<Equipment, Long> {
    
	 // 1. สำหรับดึงข้อมูลทั้งหมดที่ "ยังไม่ถูกลบ"
    List<Equipment> findAllByDeletedAtIsNull();

    // 2. สำหรับดึงข้อมูลด้วย ID เฉพาะที่ "ยังไม่ถูกลบ"
    Optional<Equipment> findByIdAndDeletedAtIsNull(Long id);
    
    @Query("SELECT DISTINCT e FROM Equipment e LEFT JOIN FETCH e.items")
    List<Equipment> findAll();
    
    Optional<Equipment> findByEquipName(String equipName);
    
    Optional<Equipment> findByPrefix(String prefix);
    
    @Query(value = "SELECT * FROM equips WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Equipment> findAllByDeletedAtIsNotNull();

    // ✅ 2. ค้นหาข้อมูลที่ถูกลบด้วย ID
    @Query(value = "SELECT * FROM equips WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<Equipment> findDeletedById(@Param("id") Long id);
    
    // ใช้ JOIN FETCH เพื่อดึงข้อมูล Equipment พร้อมกับ Category และ User มาใน Query เดียว
    @Query("SELECT e FROM Equipment e JOIN FETCH e.category JOIN FETCH e.addBy")
    List<Equipment> findAllWithDetails();

    @Query("SELECT e FROM Equipment e JOIN FETCH e.category JOIN FETCH e.addBy WHERE e.id = :id")
    Optional<Equipment> findByIdWithDetails(Long id);

}


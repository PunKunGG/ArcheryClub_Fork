package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

	
	 // ✅ 1. เพิ่ม method สำหรับค้นหาชื่อซ้ำ
    Optional<Category> findByName(String name);

    // ✅ 2. เพิ่ม Native Query สำหรับหาข้อมูลที่ถูกลบทั้งหมด (เพื่อข้าม @Where clause)
    @Query(value = "SELECT * FROM categories WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Category> findAllDeleted();

    // ✅ 3. เพิ่ม Native Query สำหรับหาข้อมูลที่ถูกลบด้วย ID
    @Query(value = "SELECT * FROM categories WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<Category> findDeletedById(@Param("id") Long id);
}

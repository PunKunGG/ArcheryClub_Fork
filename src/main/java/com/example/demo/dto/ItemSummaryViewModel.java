package com.example.demo.dto;

import com.example.demo.model.Equipment;
import com.example.demo.model.ItemStatus;

import java.util.List;
import java.util.Map;

// DTO นี้ทำหน้าที่เป็น "กล่อง" เก็บข้อมูลทั้งหมดสำหรับหน้า adminItem
public class ItemSummaryViewModel {

    private List<Equipment> equipments;
    private List<ItemStatus> statuses;
    private Map<Long, Map<String, Long>> statusCounts;

    // Getters and Setters
    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public List<ItemStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ItemStatus> statuses) {
        this.statuses = statuses;
    }

    public Map<Long, Map<String, Long>> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<Long, Map<String, Long>> statusCounts) {
        this.statusCounts = statusCounts;
    }
}
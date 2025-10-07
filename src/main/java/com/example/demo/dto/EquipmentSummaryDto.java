package com.example.demo.dto;

import java.util.Map;

import com.example.demo.model.Equipment;
import com.example.demo.model.ItemStatus;

public class EquipmentSummaryDto {

	private Equipment equipment;
    private Map<ItemStatus, Long> statusCounts;
    private long totalCount;
	public Equipment getEquipment() {
		return equipment;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	public Map<ItemStatus, Long> getStatusCounts() {
		return statusCounts;
	}
	public void setStatusCounts(Map<ItemStatus, Long> statusCounts) {
		this.statusCounts = statusCounts;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}

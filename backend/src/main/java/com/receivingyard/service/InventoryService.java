package com.receivingyard.service;

import com.receivingyard.entity.InventoryItem;

import java.util.List;

public interface InventoryService {
    List<InventoryItem> getAllInventory();
    InventoryItem getInventoryByLotNumber(String lotNumber);
}


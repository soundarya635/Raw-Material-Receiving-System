package com.receivingyard.service.impl;

import com.receivingyard.entity.InventoryItem;
import com.receivingyard.exception.ResourceNotFoundException;
import com.receivingyard.repository.InventoryRepository;
import com.receivingyard.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItem getInventoryByLotNumber(String lotNumber) {
        return inventoryRepository.findByLotNumber(lotNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "lotNumber", lotNumber));
    }
}


package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.entity.InventoryItem;
import com.receivingyard.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryItem>>> getAllInventory() {
        List<InventoryItem> list = inventoryService.getAllInventory();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/lot/{lotNumber}")
    public ResponseEntity<ApiResponse<InventoryItem>> getInventoryByLotNumber(
            @PathVariable String lotNumber) {
        InventoryItem item = inventoryService.getInventoryByLotNumber(lotNumber);
        return ResponseEntity.ok(ApiResponse.ok(item));
    }
}


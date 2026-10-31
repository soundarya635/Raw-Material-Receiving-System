package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.dto.PurchaseOrderRequestDTO;
import com.receivingyard.dto.PurchaseOrderResponseDTO;
import com.receivingyard.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDTO>> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequestDTO request) {
        PurchaseOrderResponseDTO response = purchaseOrderService.createPurchaseOrder(request);
        return new ResponseEntity<>(ApiResponse.ok("Purchase Order created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDTO>>> getAllPurchaseOrders() {
        List<PurchaseOrderResponseDTO> list = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{poNumber}")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDTO>> getPurchaseOrderByPoNumber(
            @PathVariable String poNumber) {
        PurchaseOrderResponseDTO response = purchaseOrderService.getPurchaseOrderByPoNumber(poNumber);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDTO>>> searchPurchaseOrders(
            @RequestParam(required = false) String query) {
        List<PurchaseOrderResponseDTO> list = purchaseOrderService.searchPurchaseOrders(query);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}


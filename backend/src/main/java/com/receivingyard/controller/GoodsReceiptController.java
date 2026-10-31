package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.dto.GoodsReceiptRequestDTO;
import com.receivingyard.dto.GoodsReceiptResponseDTO;
import com.receivingyard.service.GoodsReceiptService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    @Autowired
    public GoodsReceiptController(GoodsReceiptService goodsReceiptService) {
        this.goodsReceiptService = goodsReceiptService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GoodsReceiptResponseDTO>> recordReceipt(
            @Valid @RequestBody GoodsReceiptRequestDTO request) {
        GoodsReceiptResponseDTO response = goodsReceiptService.recordReceipt(request);
        return new ResponseEntity<>(ApiResponse.ok("Goods receipt recorded successfully. Lot created under QUARANTINE.", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GoodsReceiptResponseDTO>>> getAllReceipts() {
        List<GoodsReceiptResponseDTO> list = goodsReceiptService.getAllReceipts();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{grnNumber}")
    public ResponseEntity<ApiResponse<GoodsReceiptResponseDTO>> getReceiptByGrnNumber(
            @PathVariable String grnNumber) {
        GoodsReceiptResponseDTO response = goodsReceiptService.getReceiptByGrnNumber(grnNumber);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}


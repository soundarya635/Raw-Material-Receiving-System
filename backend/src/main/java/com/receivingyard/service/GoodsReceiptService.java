package com.receivingyard.service;

import com.receivingyard.dto.GoodsReceiptRequestDTO;
import com.receivingyard.dto.GoodsReceiptResponseDTO;

import java.util.List;

public interface GoodsReceiptService {
    GoodsReceiptResponseDTO recordReceipt(GoodsReceiptRequestDTO request);
    List<GoodsReceiptResponseDTO> getAllReceipts();
    GoodsReceiptResponseDTO getReceiptByGrnNumber(String grnNumber);
}


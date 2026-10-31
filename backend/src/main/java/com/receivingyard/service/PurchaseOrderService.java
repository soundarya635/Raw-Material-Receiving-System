package com.receivingyard.service;

import com.receivingyard.dto.PurchaseOrderRequestDTO;
import com.receivingyard.dto.PurchaseOrderResponseDTO;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO request);
    List<PurchaseOrderResponseDTO> getAllPurchaseOrders();
    PurchaseOrderResponseDTO getPurchaseOrderByPoNumber(String poNumber);
    List<PurchaseOrderResponseDTO> searchPurchaseOrders(String query);
}


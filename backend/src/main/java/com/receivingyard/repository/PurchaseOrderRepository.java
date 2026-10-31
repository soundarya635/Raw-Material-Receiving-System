package com.receivingyard.repository;

import com.receivingyard.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    boolean existsByPoNumber(String poNumber);
    List<PurchaseOrder> findByPoNumberContainingIgnoreCase(String poNumber);
    List<PurchaseOrder> findAllByOrderByCreatedAtDesc();
}


package com.receivingyard.repository;

import com.receivingyard.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByLotNumber(String lotNumber);
    List<InventoryItem> findAllByOrderByUpdatedAtDesc();

    @Query("SELECT COALESCE(SUM(i.receivedQuantity), 0) FROM InventoryItem i")
    BigDecimal sumReceivedQuantity();

    @Query("SELECT COALESCE(SUM(i.pendingQuantity), 0) FROM InventoryItem i")
    BigDecimal sumPendingQuantity();

    @Query("SELECT COALESCE(SUM(i.acceptedQuantity), 0) FROM InventoryItem i")
    BigDecimal sumAcceptedQuantity();

    @Query("SELECT COALESCE(SUM(i.rejectedQuantity), 0) FROM InventoryItem i")
    BigDecimal sumRejectedQuantity();
}


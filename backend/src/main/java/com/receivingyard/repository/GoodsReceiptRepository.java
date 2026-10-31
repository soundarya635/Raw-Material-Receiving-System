package com.receivingyard.repository;

import com.receivingyard.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    Optional<GoodsReceipt> findByGrnNumber(String grnNumber);
    boolean existsByGrnNumber(String grnNumber);
    List<GoodsReceipt> findAllByOrderByCreatedAtDesc();
    List<GoodsReceipt> findTop10ByOrderByCreatedAtDesc();
}


package com.receivingyard.repository;

import com.receivingyard.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {
    Optional<Lot> findByLotNumber(String lotNumber);
    boolean existsByLotNumber(String lotNumber);
    List<Lot> findByStatus(String status);
    List<Lot> findAllByOrderByCreatedAtDesc();
}


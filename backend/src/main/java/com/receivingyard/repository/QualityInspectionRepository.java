package com.receivingyard.repository;

import com.receivingyard.entity.QualityInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long> {
    Optional<QualityInspection> findTopByLotNumberOrderByCreatedAtDesc(String lotNumber);
    List<QualityInspection> findByOverallResult(String overallResult);
    List<QualityInspection> findAllByOrderByCreatedAtDesc();
    long countByOverallResult(String overallResult);
}


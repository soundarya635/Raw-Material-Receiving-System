package com.receivingyard.repository;

import com.receivingyard.entity.InspectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionResultRepository extends JpaRepository<InspectionResult, Long> {
    List<InspectionResult> findByInspectionId(Long inspectionId);
}


package com.receivingyard.repository;

import com.receivingyard.entity.CoaDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoaDocumentRepository extends JpaRepository<CoaDocument, Long> {
    List<CoaDocument> findByLotNumber(String lotNumber);
    List<CoaDocument> findAllByOrderByUploadedAtDesc();
}


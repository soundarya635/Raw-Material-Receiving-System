package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.dto.DispositionRequestDTO;
import com.receivingyard.dto.QualityInspectionRequestDTO;
import com.receivingyard.dto.QualityInspectionResponseDTO;
import com.receivingyard.entity.Lot;
import com.receivingyard.service.QualityInspectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality-inspections")
public class QualityInspectionController {

    private final QualityInspectionService qualityInspectionService;

    @Autowired
    public QualityInspectionController(QualityInspectionService qualityInspectionService) {
        this.qualityInspectionService = qualityInspectionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QualityInspectionResponseDTO>> recordInspection(
            @Valid @RequestBody QualityInspectionRequestDTO request) {
        QualityInspectionResponseDTO response = qualityInspectionService.recordInspection(request);
        return new ResponseEntity<>(ApiResponse.ok("Quality inspection recorded successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QualityInspectionResponseDTO>>> getAllInspections() {
        List<QualityInspectionResponseDTO> list = qualityInspectionService.getAllInspections();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/lot/{lotNumber}")
    public ResponseEntity<ApiResponse<QualityInspectionResponseDTO>> getInspectionByLotNumber(
            @PathVariable String lotNumber) {
        QualityInspectionResponseDTO response = qualityInspectionService.getInspectionByLotNumber(lotNumber);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/quarantined-lots")
    public ResponseEntity<ApiResponse<List<Lot>>> getQuarantinedLots() {
        List<Lot> lots = qualityInspectionService.getQuarantinedLots();
        return ResponseEntity.ok(ApiResponse.ok(lots));
    }

    @PostMapping("/disposition")
    public ResponseEntity<ApiResponse<Lot>> submitDisposition(
            @Valid @RequestBody DispositionRequestDTO request) {
        Lot lot = qualityInspectionService.submitDisposition(request);
        return ResponseEntity.ok(ApiResponse.ok("Disposition authorized and inventory updated successfully", lot));
    }
}


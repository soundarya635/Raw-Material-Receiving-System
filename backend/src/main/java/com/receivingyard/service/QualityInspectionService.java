package com.receivingyard.service;

import com.receivingyard.dto.DispositionRequestDTO;
import com.receivingyard.dto.QualityInspectionRequestDTO;
import com.receivingyard.dto.QualityInspectionResponseDTO;
import com.receivingyard.entity.Lot;

import java.util.List;

public interface QualityInspectionService {
    QualityInspectionResponseDTO recordInspection(QualityInspectionRequestDTO request);
    List<QualityInspectionResponseDTO> getAllInspections();
    QualityInspectionResponseDTO getInspectionByLotNumber(String lotNumber);
    List<Lot> getQuarantinedLots();
    Lot submitDisposition(DispositionRequestDTO request);
}


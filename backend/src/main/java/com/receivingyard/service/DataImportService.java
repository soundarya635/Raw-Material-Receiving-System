package com.receivingyard.service;

import com.receivingyard.dto.DataImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DataImportService {
    DataImportResultDTO importExcel(MultipartFile file);
    DataImportResultDTO importCsv(MultipartFile file);
    DataImportResultDTO importPdfCoa(MultipartFile file, String lotNumber);
}


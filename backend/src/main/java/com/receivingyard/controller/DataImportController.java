package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.dto.DataImportResultDTO;
import com.receivingyard.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class DataImportController {

    private final DataImportService dataImportService;

    @Autowired
    public DataImportController(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DataImportResultDTO>> importExcel(
            @RequestParam("file") MultipartFile file) {
        DataImportResultDTO result = dataImportService.importExcel(file);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DataImportResultDTO>> importCsv(
            @RequestParam("file") MultipartFile file) {
        DataImportResultDTO result = dataImportService.importCsv(file);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DataImportResultDTO>> importPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "lotNumber", required = false) String lotNumber) {
        DataImportResultDTO result = dataImportService.importPdfCoa(file, lotNumber);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}


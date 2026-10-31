package com.receivingyard.service.impl;

import com.opencsv.CSVReader;
import com.receivingyard.dto.DataImportResultDTO;
import com.receivingyard.entity.CoaDocument;
import com.receivingyard.entity.Lot;
import com.receivingyard.entity.PurchaseOrder;
import com.receivingyard.repository.CoaDocumentRepository;
import com.receivingyard.repository.LotRepository;
import com.receivingyard.repository.PurchaseOrderRepository;
import com.receivingyard.service.DataImportService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class DataImportServiceImpl implements DataImportService {

    private static final Logger log = LoggerFactory.getLogger(DataImportServiceImpl.class);

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CoaDocumentRepository coaDocumentRepository;
    private final LotRepository lotRepository;

    @Autowired
    public DataImportServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                                 CoaDocumentRepository coaDocumentRepository,
                                 LotRepository lotRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.coaDocumentRepository = coaDocumentRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    @Transactional
    public DataImportResultDTO importExcel(MultipartFile file) {
        DataImportResultDTO result = new DataImportResultDTO(file.getOriginalFilename(), "Excel (.xlsx / .xls)");

        if (file.isEmpty()) {
            result.getMessages().add("Failed: Uploaded Excel file is empty.");
            result.getErrorDetails().add("Uploaded file contains 0 bytes.");
            return result;
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() <= 1) {
                result.getMessages().add("Failed: Excel sheet contains no data rows.");
                result.getErrorDetails().add("At least one header row and one data row are required.");
                return result;
            }

            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> colMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = formatter.formatCellValue(cell).trim().toLowerCase();
                colMap.put(header, cell.getColumnIndex());
            }

            // Required columns validation
            boolean hasPo = colMap.containsKey("po number") || colMap.containsKey("ponumber") || colMap.containsKey("po_number");
            boolean hasSupplier = colMap.containsKey("supplier name") || colMap.containsKey("supplier") || colMap.containsKey("supplier_name");
            boolean hasMaterial = colMap.containsKey("material name") || colMap.containsKey("material") || colMap.containsKey("material_name");
            boolean hasQty = colMap.containsKey("ordered quantity") || colMap.containsKey("quantity") || colMap.containsKey("ordered_quantity");
            boolean hasUnit = colMap.containsKey("unit") || colMap.containsKey("uom");

            if (!hasPo || !hasSupplier || !hasMaterial || !hasQty || !hasUnit) {
                result.getMessages().add("Unknown or Invalid Excel Structure.");
                result.getErrorDetails().add("Expected columns: 'PO Number', 'Supplier Name', 'Material Name', 'Ordered Quantity', 'Unit'.");
                result.getErrorDetails().add("Detected columns in file: " + colMap.keySet());
                return result;
            }

            int poCol = colMap.getOrDefault("po number", colMap.getOrDefault("ponumber", colMap.getOrDefault("po_number", 0)));
            int supCol = colMap.getOrDefault("supplier name", colMap.getOrDefault("supplier", colMap.getOrDefault("supplier_name", 1)));
            int matCol = colMap.getOrDefault("material name", colMap.getOrDefault("material", colMap.getOrDefault("material_name", 2)));
            int qtyCol = colMap.getOrDefault("ordered quantity", colMap.getOrDefault("quantity", colMap.getOrDefault("ordered_quantity", 3)));
            int unitCol = colMap.getOrDefault("unit", colMap.getOrDefault("uom", 4));
            Integer tolCol = colMap.get("tolerance percentage");
            if (tolCol == null) tolCol = colMap.get("tolerance");

            int success = 0;
            int failed = 0;
            List<Map<String, Object>> importedRows = new ArrayList<>();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String poNum = formatter.formatCellValue(row.getCell(poCol)).trim();
                String supName = formatter.formatCellValue(row.getCell(supCol)).trim();
                String matName = formatter.formatCellValue(row.getCell(matCol)).trim();
                String qtyStr = formatter.formatCellValue(row.getCell(qtyCol)).trim();
                String unitStr = formatter.formatCellValue(row.getCell(unitCol)).trim();
                String tolStr = tolCol != null ? formatter.formatCellValue(row.getCell(tolCol)).trim() : "5.0";

                if (poNum.isEmpty() && supName.isEmpty() && matName.isEmpty()) {
                    continue; // Skip blank rows
                }

                if (poNum.isEmpty() || supName.isEmpty() || matName.isEmpty() || qtyStr.isEmpty() || unitStr.isEmpty()) {
                    failed++;
                    result.getErrorDetails().add("Row " + (r + 1) + ": Missing required field (PO, Supplier, Material, Quantity, or Unit).");
                    continue;
                }

                BigDecimal qty;
                try {
                    qty = new BigDecimal(qtyStr.replaceAll(",", ""));
                    if (qty.compareTo(BigDecimal.ZERO) <= 0) {
                        failed++;
                        result.getErrorDetails().add("Row " + (r + 1) + ": Quantity must be greater than zero.");
                        continue;
                    }
                } catch (Exception ex) {
                    failed++;
                    result.getErrorDetails().add("Row " + (r + 1) + ": Invalid numeric quantity: '" + qtyStr + "'.");
                    continue;
                }

                BigDecimal tol = BigDecimal.valueOf(5.00);
                try {
                    if (!tolStr.isEmpty()) {
                        tol = new BigDecimal(tolStr.replaceAll("%", "").trim());
                    }
                } catch (Exception ignored) {}

                // Save or update PO in PostgreSQL
                PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNum)
                        .orElse(new PurchaseOrder());
                po.setPoNumber(poNum);
                po.setSupplierName(supName);
                po.setMaterialName(matName);
                po.setOrderedQuantity(qty);
                po.setUnit(unitStr);
                po.setTolerancePercentage(tol);
                po.setStatus("OPEN");
                purchaseOrderRepository.save(po);

                success++;
                Map<String, Object> record = new HashMap<>();
                record.put("poNumber", poNum);
                record.put("supplierName", supName);
                record.put("materialName", matName);
                record.put("orderedQuantity", qty);
                record.put("unit", unitStr);
                record.put("tolerancePercentage", tol);
                importedRows.add(record);
            }

            result.setTotalRecords(success + failed);
            result.setSuccessfulRecords(success);
            result.setFailedRecords(failed);
            result.getMessages().add(String.format("Excel Import Finished: %d records saved to PostgreSQL, %d failed.", success, failed));
            result.setExtractedData(importedRows);

        } catch (Exception ex) {
            log.error("Excel import failed", ex);
            result.getMessages().add("Failed to parse Excel file: " + ex.getMessage());
            result.getErrorDetails().add(ex.toString());
        }

        return result;
    }

    @Override
    @Transactional
    public DataImportResultDTO importCsv(MultipartFile file) {
        DataImportResultDTO result = new DataImportResultDTO(file.getOriginalFilename(), "CSV (.csv)");

        if (file.isEmpty()) {
            result.getMessages().add("Failed: Uploaded CSV file is empty.");
            result.getErrorDetails().add("Uploaded file contains 0 bytes.");
            return result;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> allRows = reader.readAll();
            if (allRows == null || allRows.size() <= 1) {
                result.getMessages().add("Failed: CSV contains no data rows.");
                result.getErrorDetails().add("Header row and at least one data row are required.");
                return result;
            }

            String[] header = allRows.get(0);
            Map<String, Integer> colMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                colMap.put(header[i].trim().toLowerCase(), i);
            }

            boolean hasPo = colMap.containsKey("po number") || colMap.containsKey("ponumber") || colMap.containsKey("po_number");
            boolean hasSupplier = colMap.containsKey("supplier name") || colMap.containsKey("supplier") || colMap.containsKey("supplier_name");
            boolean hasMaterial = colMap.containsKey("material name") || colMap.containsKey("material") || colMap.containsKey("material_name");
            boolean hasQty = colMap.containsKey("ordered quantity") || colMap.containsKey("quantity") || colMap.containsKey("ordered_quantity");
            boolean hasUnit = colMap.containsKey("unit") || colMap.containsKey("uom");

            if (!hasPo || !hasSupplier || !hasMaterial || !hasQty || !hasUnit) {
                result.getMessages().add("Unknown or Invalid CSV Structure.");
                result.getErrorDetails().add("Expected headers: 'PO Number', 'Supplier Name', 'Material Name', 'Ordered Quantity', 'Unit'.");
                result.getErrorDetails().add("Detected headers: " + colMap.keySet());
                return result;
            }

            int poCol = colMap.getOrDefault("po number", colMap.getOrDefault("ponumber", colMap.getOrDefault("po_number", 0)));
            int supCol = colMap.getOrDefault("supplier name", colMap.getOrDefault("supplier", colMap.getOrDefault("supplier_name", 1)));
            int matCol = colMap.getOrDefault("material name", colMap.getOrDefault("material", colMap.getOrDefault("material_name", 2)));
            int qtyCol = colMap.getOrDefault("ordered quantity", colMap.getOrDefault("quantity", colMap.getOrDefault("ordered_quantity", 3)));
            int unitCol = colMap.getOrDefault("unit", colMap.getOrDefault("uom", 4));
            Integer tolCol = colMap.get("tolerance percentage");
            if (tolCol == null) tolCol = colMap.get("tolerance");

            int success = 0;
            int failed = 0;
            List<Map<String, Object>> importedRows = new ArrayList<>();

            for (int r = 1; r < allRows.size(); r++) {
                String[] row = allRows.get(r);
                if (row.length <= Math.max(poCol, Math.max(supCol, Math.max(matCol, Math.max(qtyCol, unitCol))))) {
                    failed++;
                    result.getErrorDetails().add("Row " + (r + 1) + ": Row has fewer columns than header requires.");
                    continue;
                }

                String poNum = row[poCol].trim();
                String supName = row[supCol].trim();
                String matName = row[matCol].trim();
                String qtyStr = row[qtyCol].trim();
                String unitStr = row[unitCol].trim();
                String tolStr = tolCol != null && tolCol < row.length ? row[tolCol].trim() : "5.0";

                if (poNum.isEmpty() && supName.isEmpty() && matName.isEmpty()) continue;

                if (poNum.isEmpty() || supName.isEmpty() || matName.isEmpty() || qtyStr.isEmpty() || unitStr.isEmpty()) {
                    failed++;
                    result.getErrorDetails().add("Row " + (r + 1) + ": Missing required field.");
                    continue;
                }

                BigDecimal qty;
                try {
                    qty = new BigDecimal(qtyStr.replaceAll(",", ""));
                    if (qty.compareTo(BigDecimal.ZERO) <= 0) {
                        failed++;
                        result.getErrorDetails().add("Row " + (r + 1) + ": Quantity must be > 0.");
                        continue;
                    }
                } catch (Exception ex) {
                    failed++;
                    result.getErrorDetails().add("Row " + (r + 1) + ": Invalid quantity number: " + qtyStr);
                    continue;
                }

                BigDecimal tol = BigDecimal.valueOf(5.00);
                try {
                    if (!tolStr.isEmpty()) tol = new BigDecimal(tolStr.replaceAll("%", "").trim());
                } catch (Exception ignored) {}

                PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNum)
                        .orElse(new PurchaseOrder());
                po.setPoNumber(poNum);
                po.setSupplierName(supName);
                po.setMaterialName(matName);
                po.setOrderedQuantity(qty);
                po.setUnit(unitStr);
                po.setTolerancePercentage(tol);
                po.setStatus("OPEN");
                purchaseOrderRepository.save(po);

                success++;
                Map<String, Object> record = new HashMap<>();
                record.put("poNumber", poNum);
                record.put("supplierName", supName);
                record.put("materialName", matName);
                record.put("orderedQuantity", qty);
                record.put("unit", unitStr);
                record.put("tolerancePercentage", tol);
                importedRows.add(record);
            }

            result.setTotalRecords(success + failed);
            result.setSuccessfulRecords(success);
            result.setFailedRecords(failed);
            result.getMessages().add(String.format("CSV Import Finished: %d records saved to PostgreSQL, %d failed.", success, failed));
            result.setExtractedData(importedRows);

        } catch (Exception ex) {
            log.error("CSV import failed", ex);
            result.getMessages().add("Failed to parse CSV file: " + ex.getMessage());
            result.getErrorDetails().add(ex.toString());
        }

        return result;
    }

    @Override
    @Transactional
    public DataImportResultDTO importPdfCoa(MultipartFile file, String lotNumber) {
        DataImportResultDTO result = new DataImportResultDTO(file.getOriginalFilename(), "PDF (Certificate of Analysis)");

        if (file.isEmpty()) {
            result.getMessages().add("Failed: Uploaded PDF file is empty.");
            result.getErrorDetails().add("Uploaded file contains 0 bytes.");
            return result;
        }

        // Validate or find Lot
        String targetLot = lotNumber != null && !lotNumber.isBlank() ? lotNumber.trim() : null;
        if (targetLot != null) {
            Optional<Lot> lotOpt = lotRepository.findByLotNumber(targetLot);
            if (lotOpt.isEmpty()) {
                result.getMessages().add("Validation warning: Lot number '" + targetLot + "' was not found in the database.");
            }
        } else {
            // Pick most recent lot if available, or create general lot link
            List<Lot> lots = lotRepository.findAllByOrderByCreatedAtDesc();
            if (!lots.isEmpty()) {
                targetLot = lots.get(0).getLotNumber();
            } else {
                targetLot = "LOT-GENERAL-CoA";
            }
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);

            CoaDocument coa = new CoaDocument();
            coa.setLotNumber(targetLot);
            coa.setFileName(file.getOriginalFilename());
            coa.setFileType(file.getContentType() != null ? file.getContentType() : "application/pdf");
            coa.setFileSize(file.getSize());
            coa.setExtractedText(extractedText);
            coa.setUploadedAt(Instant.now());

            CoaDocument saved = coaDocumentRepository.save(coa);

            result.setTotalRecords(1);
            result.setSuccessfulRecords(1);
            result.getMessages().add("CoA PDF successfully uploaded, text extracted via PDFBox, and stored in PostgreSQL for Lot: " + targetLot);

            Map<String, Object> data = new HashMap<>();
            data.put("id", saved.getId());
            data.put("lotNumber", targetLot);
            data.put("fileName", file.getOriginalFilename());
            data.put("fileSize", file.getSize());
            data.put("extractedCharacters", extractedText != null ? extractedText.length() : 0);
            data.put("textPreview", extractedText != null && extractedText.length() > 500 
                    ? extractedText.substring(0, 500) + "..." 
                    : extractedText);
            result.setExtractedData(data);

        } catch (Exception ex) {
            log.error("PDF CoA extraction failed", ex);
            result.getMessages().add("Failed to process PDF file: " + ex.getMessage());
            result.getErrorDetails().add(ex.toString());
        }

        return result;
    }
}


# Data Source Analysis & Discovery Report

## 1. Executive Summary & File Inspection Status

- **Inspection Date**: October 2026
- **Project**: Raw Material Receiving Yard and Quality Inspection Management System (Phase 1)
- **Current File Discovery Status**: 
  - Comprehensive inspection of workspace directory (`c:\Users\ASUS\Desktop\RawMaterialQC`) confirmed that **no proprietary company sample data files (Excel, CSV, PDF, or ERP dumps) are currently placed in the project**.
  - As required by the Phase 1 specifications, **no fake proprietary company data or fabricated field schemas have been invented**.
  - A comprehensive, source-independent ingestion model and flexible data processing pipeline have been designed based on core manufacturing industry standards (ISA-95 / ISO 9001 / cGMP receiving guidelines) to be immediately adaptable once customer files arrive.

---

## 2. Anticipated Data Sources & Ingestion Modalities

The system is designed to ingest data from heterogeneous sources without altering business logic:

| Source Type | Protocol / Format | Frequency / Trigger | Target Domain Information |
| :--- | :--- | :--- | :--- |
| **Excel Workbooks** | `.xlsx`, `.xls` (Apache POI / OpenCSV) | Manual upload or shared directory polling | Bulk Purchase Orders, Material Masters, Historic Quality Logs |
| **CSV Flat Files** | Comma / Tab / Pipe delimited | Manual batch upload or scheduled SFTP/local sync | Receiving yard tally sheets, weighbridge weight records |
| **PDF Documents** | Unstructured / Semi-structured PDF | Upload via UI or Cloud Drive sync | Certificate of Analysis (CoA) from vendors, Bills of Lading, Delivery Notes |
| **Google Sheets** | Google Drive / Sheets API v4 | Webhook / Polling sync | Live yard truck logging, vendor shipment pre-alerts |
| **Google Drive** | Shared Folder REST API | Event-driven or hourly batch scan | CoA document repositories, scanned driver delivery slips |
| **External ERP** | REST API / Webhooks (e.g., SAP, NetSuite) | Near-real-time push or scheduled pull | Purchase Order generation, vendor master catalog, inventory reservations |
| **Manual UI Entry** | Direct Browser Form Submission | Real-time on gate arrival | Truck check-in, immediate yard physical inspection, disposition override |

---

## 3. Data Domain Identification & Key Entities

Through requirement decomposition, the following key entities and data elements are mapped across all incoming sources:

### A. Purchase Order (PO) Details
- **PO Number**: Unique purchasing contract reference (e.g., `PO-2026-10492`).
- **Order Date & Expected Delivery Date**: For arrival scheduling and supplier on-time delivery tracking.
- **Supplier Reference**: Supplier ID or unique code linking to approved vendor list.
- **Line Items**: Line number, material code, ordered quantity, unit of measure, unit price, and pre-negotiated tolerance.

### B. Goods Receipt Note (GRN) & Truck Arrival
- **GRN Number**: System-generated or yard-assigned receipt record (e.g., `GRN-2026-0081`).
- **Delivery Documentation**: Vendor delivery note number, bill of lading, airway bill.
- **Logistics Details**: Carrier name, truck registration / vehicle license plate number, driver full name.
- **Yard Receiver**: User ID or badge number of the yard personnel checking the delivery.
- **Arrival Timestamp**: Exact date/time the truck entered the receiving bay.

### C. Lot & Batch Tracking
- **Internal Lot Number**: Application-generated unique traceable identifier (e.g., `LOT-20261031-0001`).
- **Supplier Lot Number**: The batch number printed on vendor packaging or CoA.
- **Quantities Partition**:
  - `Received Quantity`: Total physically counted/weighed at gate.
  - `Quarantined Quantity`: Quantity held under quality lockdown (100% of received stock initially).
  - `Approved Quantity`: Quantity meeting all quality standards after inspection.
  - `Rejected Quantity`: Quantity failing inspection or exceeding allowed variance.
  - `Released Quantity`: Approved stock transferred into active production inventory.

### D. Certificate of Analysis (CoA)
- **Document Metadata**: File name, cryptographic hash (SHA-256 for tampering prevention), file size, MIME type.
- **Vendor CoA Results**: Tested parameters, test dates, vendor approval signatures.
- **Verification Status**: Sign-off flag and timestamp by QC inspector confirming CoA validity prior to yard sampling.

### E. Quality Specifications & Test Results
- **Specification Parameters**: Parameter name (e.g., Viscosity, Purity %, Density, Moisture content).
- **Measurement Types**: 
  - `NUMERIC`: Evaluated against inclusive lower limit (`min_value`) and upper limit (`max_value`).
  - `QUALITATIVE`: Evaluated against strict pass/fail or expected categorical values (e.g., "Clear Liquid", "White Flakes").
- **Criticality**: `is_mandatory` flag. Any failure in a mandatory parameter halts lot approval immediately.

---

## 4. Cross-Source Identification & Conflict Handling

Different data sources may provide overlapping or contradictory information. The following conflict matrix dictates system behavior:

```
[ Incoming Source Data ] 
            │
            ▼
┌─────────────────────────┐
│ Schema & Syntax Check   │ ──(Invalid)──> [ Quarantine Ingestion Record & Alert Admin ]
└───────────┬─────────────┘
            │ (Valid)
            ▼
┌─────────────────────────┐
│ Duplicate & Identity    │ ──(Duplicate)─> [ Retain Prior Version, Log Ingestion Conflict ]
│ Detection Engine        │
└───────────┬─────────────┘
            │ (New / Update)
            ▼
┌─────────────────────────┐
│ Authoritative Hierarchy │
│ Resolution              │
└───────────┬─────────────┘
```

### Hierarchy of Authority
1. **Purchase Order Data**: Authoritative source is the ERP System or Official PO Import. Yard receiver edits are disallowed on PO lines; variance must be captured on the Goods Receipt, not by modifying the PO.
2. **Physical Receiving Quantities**: Authoritative source is the Weighbridge / Yard Scale physical tally entered at the gate.
3. **Quality Results**: Authoritative source is the Internal Quality Lab Inspection. Supplier CoA values serve as preliminary reference only and cannot override failed internal tests.

### Handling Missing Fields & Inconsistent Formats
- **Missing Non-Mandatory Fields**: Defaulted to standard defaults (e.g., default tolerance ±5.0% if omitted on PO item).
- **Missing Mandatory Fields** (e.g., PO Number, Material Code, Quantity): Record is rejected into `data_source_records` staging table with exact validation errors, preventing silent data corruption.
- **Inconsistent Units of Measure**: Handled through standardized Unit Conversion Service (e.g., metric tonnes to kilograms, gallons to liters).

---

## 5. Architectural Adapter Pattern for Ingestion

To decouple the core domain from external formats, the system uses the **Hexagonal Adapter Pattern**:

```
[Excel / CSV / PDF / API / Drive]
              │
              ▼
    [ Source Input Adapter ]
              │ (Normalized DTO)
              ▼
   [ Data Ingestion Pipeline ]
  ├─ 1. Format Detection
  ├─ 2. Extraction & Parsing
  ├─ 3. Field Mapping
  ├─ 4. Business Validation
  ├─ 5. Duplicate Detection
  ├─ 6. Entity Reconciliation
  └─ 7. Persistence & Audit Log
              │
              ▼
   [ Core Domain Entities ]
```

When client sample files arrive, a new parser implementing `SourceDataAdapter<T>` can be plugged in without changing entity models or business rules.


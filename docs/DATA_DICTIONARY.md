# Data Dictionary

## Raw Material Receiving Yard & Quality Inspection Management System

This document specifies the preliminary relational schema, tables, attributes, data types, constraints, and business significance for the database model.

---

### 1. `users`
System actors interacting with the receiving yard, quality control, warehouse, and system administration.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique user identifier |
| `username` | VARCHAR(100) | NOT NULL, UNIQUE | User login identifier |
| `full_name` | VARCHAR(255) | NOT NULL | Complete legal / employee name |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Official email for notifications |
| `role` | VARCHAR(50) | NOT NULL | User role (`WAREHOUSE_OPERATOR`, `QUALITY_INSPECTOR`, `QUALITY_MANAGER`, `SYSTEM_ADMIN`) |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | User active status |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Audit creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Audit last update timestamp |

---

### 2. `suppliers`
Vendor organizations delivering raw materials to the manufacturing plant.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique supplier identifier |
| `code` | VARCHAR(50) | NOT NULL, UNIQUE | Vendor ERP / business code (e.g., `SUP-CHEM-01`) |
| `name` | VARCHAR(255) | NOT NULL | Legal name of vendor |
| `contact_email`| VARCHAR(255) | NULLABLE | Primary vendor contact email |
| `phone` | VARCHAR(50) | NULLABLE | Vendor telephone number |
| `address` | TEXT | NULLABLE | Vendor registered or dispatch address |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Supplier active authorization flag |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 3. `materials`
Raw material definitions managed in the receiving yard.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique material identifier |
| `code` | VARCHAR(50) | NOT NULL, UNIQUE | Part/Material SKU code (e.g., `MAT-RES-101`) |
| `name` | VARCHAR(255) | NOT NULL | Material commercial description |
| `description` | TEXT | NULLABLE | Technical description / grade specifications |
| `unit_of_measure` | VARCHAR(20) | NOT NULL | Standard measurement unit (`KG`, `LITER`, `MT`, `EA`) |
| `default_tolerance_percent` | NUMERIC(5,2) | NOT NULL, DEFAULT 5.00 | Default allowed ± receiving tolerance percentage |
| `requires_coa`| BOOLEAN | NOT NULL, DEFAULT TRUE | Indicates if CoA is strictly mandatory prior to inspection |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether material is approved for procurement |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 4. `purchase_orders`
Procurement contracts issued to suppliers specifying raw material orders.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique PO internal identifier |
| `po_number` | VARCHAR(100) | NOT NULL, UNIQUE | Official PO number |
| `supplier_id` | BIGINT | NOT NULL, FK -> `suppliers(id)` | Associated vendor |
| `order_date` | DATE | NOT NULL | Date PO was placed |
| `expected_delivery_date` | DATE | NULLABLE | Target delivery window |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'ISSUED' | Lifecycle status (`DRAFT`, `ISSUED`, `PARTIALLY_RECEIVED`, `COMPLETED`, `CANCELLED`) |
| `total_amount`| NUMERIC(15,2)| NULLABLE | Commercial value |
| `currency` | VARCHAR(10) | DEFAULT 'USD' | Currency code |
| `notes` | TEXT | NULLABLE | Purchasing special instructions |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 5. `purchase_order_items`
Individual line items on purchase orders.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique item identifier |
| `purchase_order_id` | BIGINT | NOT NULL, FK -> `purchase_orders(id)` | Parent purchase order |
| `material_id` | BIGINT | NOT NULL, FK -> `materials(id)` | Raw material requested |
| `line_number` | INT | NOT NULL | Sequential line index on PO |
| `ordered_quantity` | NUMERIC(15,3)| NOT NULL | Total quantity contracted |
| `received_quantity`| NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Cumulative physical quantity received to date |
| `unit_of_measure` | VARCHAR(20) | NOT NULL | Unit of measure |
| `unit_price` | NUMERIC(15,2)| NULLABLE | Unit cost |
| `tolerance_percent`| NUMERIC(5,2)| NOT NULL, DEFAULT 5.00 | Line-specific acceptable variance (e.g., ±5.00%) |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | Status (`PENDING`, `PARTIALLY_RECEIVED`, `COMPLETED`, `OVER_RECEIVED`) |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 6. `goods_receipts`
Physical delivery intake records recorded at the yard gate.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique GRN internal ID |
| `grn_number` | VARCHAR(100) | NOT NULL, UNIQUE | Goods Receipt Note identification number |
| `purchase_order_id` | BIGINT | NOT NULL, FK -> `purchase_orders(id)` | Corresponding purchase order |
| `delivery_note_number` | VARCHAR(100) | NULLABLE | Supplier delivery challan / invoice number |
| `carrier_name` | VARCHAR(150) | NULLABLE | Freight logistics provider |
| `vehicle_number` | VARCHAR(50) | NULLABLE | Truck registration plate |
| `driver_name` | VARCHAR(150) | NULLABLE | Delivery driver name |
| `received_by` | BIGINT | NULLABLE, FK -> `users(id)` | Operator receiving the delivery |
| `arrival_timestamp` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Gate entry timestamp |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'RECEIVED' | GRN status (`RECEIVED`, `INSPECTED`, `COMPLETED`, `REJECTED`) |
| `remarks` | TEXT | NULLABLE | Delivery bay notes / observations |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 7. `lots`
Granular batch tracking units. **Every newly received lot is initially quarantined by design.**

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique lot identifier |
| `lot_number` | VARCHAR(100) | NOT NULL, UNIQUE | Internal lot barcode/code |
| `goods_receipt_id` | BIGINT | NOT NULL, FK -> `goods_receipts(id)` | Associated goods receipt |
| `po_item_id` | BIGINT | NOT NULL, FK -> `purchase_order_items(id)` | Associated PO line item |
| `material_id` | BIGINT | NOT NULL, FK -> `materials(id)` | Material in this lot |
| `supplier_lot_number` | VARCHAR(100) | NULLABLE | Batch number on supplier packaging |
| `received_quantity` | NUMERIC(15,3)| NOT NULL | Total quantity received in this lot |
| `quarantined_quantity`| NUMERIC(15,3)| NOT NULL | Quantity locked in quarantine (initially = `received_quantity`) |
| `approved_quantity` | NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Quantity passed by QA inspection |
| `rejected_quantity` | NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Quantity failed and rejected |
| `released_to_production_quantity` | NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Quantity moved to active production inventory |
| `unit_of_measure` | VARCHAR(20) | NOT NULL | Unit of measure |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'QUARANTINED' | Status (`QUARANTINED`, `IN_INSPECTION`, `PARTIALLY_APPROVED`, `FULLY_APPROVED`, `REJECTED`, `RELEASED`) |
| `manufacturing_date`| DATE | NULLABLE | Lot production date |
| `expiry_date` | DATE | NULLABLE | Expiry / retest date |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

> **Constraint Rule**: `quarantined_quantity + approved_quantity + rejected_quantity = received_quantity`

---

### 8. `coa_documents`
Certificate of Analysis files linked to received lots.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Document identifier |
| `lot_id` | BIGINT | NOT NULL, FK -> `lots(id)` | Linked lot |
| `document_name` | VARCHAR(255) | NOT NULL | Stored document name |
| `file_path` | TEXT | NOT NULL | Path or secure cloud URL |
| `file_hash` | VARCHAR(128) | NULLABLE | SHA-256 integrity hash |
| `mime_type` | VARCHAR(100) | NULLABLE | e.g., `application/pdf` |
| `file_size_bytes` | BIGINT | NULLABLE | File size |
| `source_type` | VARCHAR(50) | NOT NULL, DEFAULT 'MANUAL_UPLOAD' | Ingestion source (`MANUAL_UPLOAD`, `GOOGLE_DRIVE`, `ERP`, `EMAIL`) |
| `is_verified` | BOOLEAN | NOT NULL, DEFAULT FALSE | Whether QA confirmed authenticity |
| `verified_by` | BIGINT | NULLABLE, FK -> `users(id)` | Inspector verifying |
| `verification_timestamp` | TIMESTAMPTZ | NULLABLE | Timestamp of verification |
| `remarks` | TEXT | NULLABLE | Verification remarks |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |

---

### 9. `quality_specifications`
Standard testing parameters for raw materials.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique specification identifier |
| `material_id` | BIGINT | NOT NULL, FK -> `materials(id)` | Material being governed |
| `parameter_name` | VARCHAR(150) | NOT NULL | Test parameter (e.g., "Viscosity", "Water Content") |
| `parameter_type` | VARCHAR(50) | NOT NULL, DEFAULT 'NUMERIC' | `NUMERIC` or `QUALITATIVE` |
| `min_value` | NUMERIC(15,4)| NULLABLE | Minimum acceptable bound (inclusive) |
| `max_value` | NUMERIC(15,4)| NULLABLE | Maximum acceptable bound (inclusive) |
| `target_value` | NUMERIC(15,4)| NULLABLE | Ideal specification target |
| `expected_text_value` | VARCHAR(255)| NULLABLE | Expected qualitative string |
| `unit` | VARCHAR(50) | NULLABLE | Physical testing unit |
| `is_mandatory` | BOOLEAN | NOT NULL, DEFAULT TRUE | If TRUE, failure blocks lot approval |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Specification active toggle |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 10. `quality_inspections`
Physical sampling and laboratory inspection records for lots.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique inspection ID |
| `inspection_number`| VARCHAR(100) | NOT NULL, UNIQUE | Inspection reference code |
| `lot_id` | BIGINT | NOT NULL, FK -> `lots(id)` | Sampled lot |
| `inspector_id` | BIGINT | NULLABLE, FK -> `users(id)` | Inspector executing tests |
| `inspection_date` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Testing timestamp |
| `overall_status` | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | Status (`PENDING`, `IN_PROGRESS`, `PASSED`, `FAILED`, `CONDITIONALLY_APPROVED`) |
| `sampling_quantity`| NUMERIC(15,3)| NOT NULL | Quantity pulled for testing |
| `sample_unit` | VARCHAR(20) | NOT NULL | Unit of sampled quantity |
| `observations` | TEXT | NULLABLE | Inspector notes and lab commentary |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record update timestamp |

---

### 11. `inspection_results`
Individual parameter measurements within a quality inspection.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Result identifier |
| `inspection_id` | BIGINT | NOT NULL, FK -> `quality_inspections(id)` | Parent inspection |
| `specification_id` | BIGINT | NULLABLE, FK -> `quality_specifications(id)` | Master specification tested |
| `parameter_name` | VARCHAR(150) | NOT NULL | Parameter name |
| `measured_numeric_value` | NUMERIC(15,4)| NULLABLE | Recorded numerical result |
| `measured_text_value` | VARCHAR(255)| NULLABLE | Recorded qualitative result |
| `min_limit` | NUMERIC(15,4)| NULLABLE | Evaluated lower bound (inclusive) |
| `max_limit` | NUMERIC(15,4)| NULLABLE | Evaluated upper bound (inclusive) |
| `is_compliant` | BOOLEAN | NOT NULL | Compliance result (`true` = within bounds) |
| `failure_reason` | TEXT | NULLABLE | Explanation if non-compliant |
| `tested_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Test execution timestamp |

---

### 12. `lot_dispositions`
Authorized sign-off and quantity partitioning decision.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Disposition identifier |
| `lot_id` | BIGINT | NOT NULL, FK -> `lots(id)` | Lot being dispositioned |
| `decision` | VARCHAR(50) | NOT NULL | Decision (`APPROVE_FULL`, `APPROVE_PARTIAL`, `REJECT_FULL`, `REJECT_PARTIAL`, `QUARANTINE`) |
| `approved_quantity` | NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Authorized accepted quantity |
| `rejected_quantity` | NUMERIC(15,3)| NOT NULL, DEFAULT 0.000 | Authorized rejected quantity |
| `authorized_by` | BIGINT | NOT NULL, FK -> `users(id)` | QA manager signing off |
| `authorization_date`| TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Sign-off timestamp |
| `justification_notes`| TEXT | NOT NULL | Regulatory reasoning for decision |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Record creation timestamp |

---

### 13. `inventory_transactions`
Ledger of material movements ensuring inventory traceability.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Transaction ledger ID |
| `transaction_reference`| VARCHAR(100)| NOT NULL, UNIQUE | Audit transaction reference code |
| `lot_id` | BIGINT | NOT NULL, FK -> `lots(id)` | Affected lot |
| `material_id` | BIGINT | NOT NULL, FK -> `materials(id)` | Material moved |
| `transaction_type` | VARCHAR(50) | NOT NULL | Transaction (`RECEIPT_TO_QUARANTINE`, `QUARANTINE_TO_APPROVED`, `QUARANTINE_TO_REJECTED`, `APPROVED_TO_PRODUCTION`) |
| `quantity` | NUMERIC(15,3)| NOT NULL | Quantity transferred |
| `unit_of_measure` | VARCHAR(20) | NOT NULL | Unit of measure |
| `from_location` | VARCHAR(100) | NULLABLE | Origin location / bay |
| `to_location` | VARCHAR(100) | NOT NULL | Destination warehouse / production line |
| `performed_by` | BIGINT | NULLABLE, FK -> `users(id)` | User executing transaction |
| `transaction_timestamp`| TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Timestamp of physical movement |
| `notes` | TEXT | NULLABLE | Transaction audit remarks |

---

### 14. `data_source_records`
Agnostic data ingestion metadata and error tracing for all incoming files/payloads.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Ingestion record ID |
| `source_identifier` | VARCHAR(150) | NOT NULL | Source name, file name, or API endpoint |
| `source_type` | VARCHAR(50) | NOT NULL | Type (`EXCEL`, `CSV`, `PDF`, `GOOGLE_SHEETS`, `GOOGLE_DRIVE`, `ERP_API`, `MANUAL_UPLOAD`) |
| `original_file_name`| VARCHAR(255) | NULLABLE | Original filename if file-based |
| `storage_location` | TEXT | NULLABLE | Archival storage path |
| `checksum` | VARCHAR(128) | NULLABLE | SHA-256 checksum to prevent duplicate processing |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'RECEIVED' | Processing status (`RECEIVED`, `PARSED`, `VALIDATED`, `FAILED`, `COMPLETED`) |
| `total_records` | INT | NOT NULL, DEFAULT 0 | Count of records parsed |
| `successful_records`| INT | NOT NULL, DEFAULT 0 | Count of successfully ingested records |
| `failed_records` | INT | NOT NULL, DEFAULT 0 | Count of failed / rejected records |
| `error_details` | JSONB | NULLABLE | Structured error list and line numbers |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Ingestion creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Ingestion update timestamp |

---

### 15. `synchronization_history`
Batch synchronization runs and recurring poll history.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Sync execution ID |
| `data_source_id` | BIGINT | NULLABLE, FK -> `data_source_records(id)` | Parent ingestion record |
| `sync_type` | VARCHAR(50) | NOT NULL, DEFAULT 'MANUAL' | Trigger (`MANUAL`, `SCHEDULED_POLL`, `WEBHOOK`) |
| `start_time` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Start timestamp |
| `end_time` | TIMESTAMPTZ | NULLABLE | Finish timestamp |
| `status` | VARCHAR(50) | NOT NULL, DEFAULT 'RUNNING' | Status (`RUNNING`, `SUCCESS`, `PARTIAL_FAILURE`, `FAILED`) |
| `records_processed`| INT | NOT NULL, DEFAULT 0 | Number of entities updated |
| `error_summary` | TEXT | NULLABLE | High level failure synopsis |

---

### 16. `audit_logs`
Immutable compliance and regulatory audit trail.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Audit log ID |
| `entity_name` | VARCHAR(100) | NOT NULL | Entity table (e.g., `lots`, `lot_dispositions`) |
| `entity_id` | VARCHAR(100) | NOT NULL | Target record primary key |
| `action` | VARCHAR(50) | NOT NULL | Operation (`CREATE`, `UPDATE`, `DELETE`, `STATUS_CHANGE`, `DISPOSITION_OVERRIDE`) |
| `performed_by` | BIGINT | NULLABLE, FK -> `users(id)` | User who executed the action |
| `timestamp` | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Exact server timestamp |
| `previous_state` | JSONB | NULLABLE | Snapshot before mutation |
| `new_state` | JSONB | NULLABLE | Snapshot after mutation |
| `ip_address` | VARCHAR(50) | NULLABLE | Client IP address |
| `reason` | TEXT | NULLABLE | Mandatory reason for overrides |


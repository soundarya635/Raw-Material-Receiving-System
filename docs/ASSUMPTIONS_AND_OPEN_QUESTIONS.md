# Assumptions and Open Questions

## 1. Project Context & Data Discovery Baseline

As established in the Phase 1 discovery audit:
- **No client-specific sample files (Excel sheets, CSV exports, CoA PDFs, or ERP API definitions) were provided in the initial repository**.
- All data models, entity relationships, and database schemas created in Phase 1 represent **preliminary, extensible standards** designed around manufacturing quality standards (ISA-95 and ISO 9001).
- No company-specific proprietary naming conventions or fictitious schemas have been assumed.

---

## 2. Key Architectural Assumptions

### A. Procurement & Quantity Matching
1. **Quantity Tolerance Calculation**:
   - Initial receiving tolerance is assumed to be **±5.0%** of the PO item ordered quantity.
   - Boundaries are **inclusive**: a received quantity between $95.0\%$ and $105.0\%$ of ordered quantity is considered an acceptable delivery variance.
   - Any quantity arriving outside this bracket is flagged as an over-delivery or short-delivery exception requiring yard supervisor confirmation.
2. **Multi-Delivery (Split Shipments)**:
   - A single Purchase Order line item may be fulfilled across multiple truck deliveries (multiple Goods Receipts and Lots).
   - The cumulative received quantity across all lots is compared against the PO item ordered quantity.

### B. Quality & Quarantine Management
3. **Strict Initial Quarantine**:
   - Every physical lot entering the receiving yard is placed in **QUARANTINED** status immediately upon GRN creation.
   - Quarantined stock cannot be used, picked, or released for production under any condition until authorized sign-off.
4. **Certificate of Analysis (CoA) Requirement**:
   - Materials marked with `requires_coa = true` cannot proceed to final QC approval without an attached and verified CoA document.
5. **Quality Test Thresholds**:
   - Test specification limits (`min_value`, `max_value`) are **inclusive**: values exactly on the boundary are considered compliant.
   - Any failed mandatory test prevents full approval of that quantity.
6. **Partial Acceptance & Rejection**:
   - If a lot is partially acceptable (e.g. 800 kg accepted, 200 kg rejected due to damaged containers or localized contamination), the system partitions the quantities into distinct sub-quantities with strict mass balance conservation (`quarantined + approved + rejected = received`).
   - Only the authorized approved quantity can be transitioned to active production inventory.

### C. Data Ingestion Architecture
7. **Source-Agnostic Processing**:
   - Source data may arrive via Excel, CSV, PDF, Google Drive sync, or REST APIs.
   - An adapter layer standardizes external data formats into canonical internal DTOs before any database writing occurs.
   - Original raw files are preserved with cryptographic hashes (SHA-256) for regulatory traceability.

---

## 3. Open Questions & Information Needed From Company

The following questions must be clarified with the client/manufacturing stakeholders before finalizing Phase 2 business logic:

| # | Topic | Question for Client Stakeholders | Impact on Phase 2 Implementation |
| :--- | :--- | :--- | :--- |
| **Q1** | **Sample Files** | Can the client provide 2-3 real or anonymized samples of: (a) Purchase Orders, (b) Delivery Challans / GRNs, (c) Supplier CoA PDFs, and (d) Quality Test Spec sheets? | Determines the specific parser implementations for Excel/CSV/PDF and column mapping rules. |
| **Q2** | **Tolerance Policies** | Is the ±5% tolerance uniform across all materials, or should certain high-value or hazardous materials have custom tolerance percentages (e.g. ±1% or 0%)? | Schema already supports per-material override `materials.default_tolerance_percent` and `purchase_order_items.tolerance_percent`. |
| **Q3** | **Weighbridge / Gate Scale** | How is receiving weight captured? Manually entered by the gate operator, or integrated with an automated truck scale / weighbridge via IoT/serial/IP? | Determines whether an IoT hardware driver or standard web form is needed at gate check-in. |
| **Q4** | **CoA Parsing** | Are supplier CoAs provided as scanned images or digital text PDFs? Does the company expect automated OCR/table extraction from CoA PDFs, or manual verification by QC inspectors? | Governs whether an OCR/LLM document parsing pipeline is required in the ingestion adapter. |
| **Q5** | **ERP Integration** | Does the company use an ERP (e.g., SAP, Oracle NetSuite, Microsoft Dynamics)? If yes, what integration method is preferred: REST API, SFTP file drop, or direct database view? | Informs the scheduling and authentication configuration of the ERP ingestion adapter. |
| **Q6** | **Lot Numbering Format** | Does the company have a standardized lot numbering convention (e.g. `LOT-YYYYMMDD-XXXX`), or do they track strictly by the supplier's lot number? | Guides the internal lot sequence generator algorithm. |
| **Q7** | **Physical Quarantine Barcoding** | Will operators scan QR codes or Barcodes (Code 128 / GS1-128) during physical material movement in the yard? | Determines frontend mobile/tablet barcode scanner support. |
| **Q8** | **Approval Authority** | What specific roles are legally authorized to sign off on partial acceptance or rejection dispositions? | Configures Role-Based Access Control (RBAC) security filters in backend controllers. |


-- =============================================================================
-- RAW MATERIAL RECEIVING & QUALITY INSPECTION SYSTEM
-- Clean MVP Database Schema (9 Tables)
-- =============================================================================

-- 1. Suppliers
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    contact_email VARCHAR(255),
    phone VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Materials
CREATE TABLE IF NOT EXISTS materials (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    unit VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Purchase Orders
CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGSERIAL PRIMARY KEY,
    po_number VARCHAR(100) NOT NULL UNIQUE,
    supplier_name VARCHAR(255) NOT NULL,
    material_name VARCHAR(255) NOT NULL,
    ordered_quantity NUMERIC(15, 3) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    tolerance_percentage NUMERIC(5, 2) NOT NULL DEFAULT 5.00,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Goods Receipts
CREATE TABLE IF NOT EXISTS goods_receipts (
    id BIGSERIAL PRIMARY KEY,
    grn_number VARCHAR(100) NOT NULL UNIQUE,
    po_number VARCHAR(100) NOT NULL REFERENCES purchase_orders(po_number) ON DELETE RESTRICT,
    supplier VARCHAR(255) NOT NULL,
    material VARCHAR(255) NOT NULL,
    truck_number VARCHAR(50) NOT NULL,
    received_quantity NUMERIC(15, 3) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    arrival_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_within_tolerance BOOLEAN NOT NULL DEFAULT TRUE,
    discrepancy_reason TEXT,
    lot_number VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Lots
CREATE TABLE IF NOT EXISTS lots (
    id BIGSERIAL PRIMARY KEY,
    lot_number VARCHAR(100) NOT NULL UNIQUE,
    grn_number VARCHAR(100) NOT NULL REFERENCES goods_receipts(grn_number) ON DELETE RESTRICT,
    po_number VARCHAR(100) NOT NULL,
    material VARCHAR(255) NOT NULL,
    received_quantity NUMERIC(15, 3) NOT NULL,
    accepted_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    rejected_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    quarantined_quantity NUMERIC(15, 3) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'QUARANTINED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. CoA Documents
CREATE TABLE IF NOT EXISTS coa_documents (
    id BIGSERIAL PRIMARY KEY,
    lot_number VARCHAR(100) NOT NULL REFERENCES lots(lot_number) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    extracted_text TEXT,
    uploaded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. Quality Inspections
CREATE TABLE IF NOT EXISTS quality_inspections (
    id BIGSERIAL PRIMARY KEY,
    lot_number VARCHAR(100) NOT NULL REFERENCES lots(lot_number) ON DELETE RESTRICT,
    inspector_name VARCHAR(255) NOT NULL DEFAULT 'Quality Inspector',
    inspection_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    overall_result VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PASS, FAIL, PENDING
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Inspection Results
CREATE TABLE IF NOT EXISTS inspection_results (
    id BIGSERIAL PRIMARY KEY,
    inspection_id BIGINT NOT NULL REFERENCES quality_inspections(id) ON DELETE CASCADE,
    test_parameter VARCHAR(150) NOT NULL,
    min_value NUMERIC(15, 4) NOT NULL,
    max_value NUMERIC(15, 4) NOT NULL,
    actual_value NUMERIC(15, 4) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    is_pass BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Inventory
CREATE TABLE IF NOT EXISTS inventory (
    id BIGSERIAL PRIMARY KEY,
    lot_number VARCHAR(100) NOT NULL UNIQUE REFERENCES lots(lot_number) ON DELETE RESTRICT,
    material VARCHAR(255) NOT NULL,
    received_quantity NUMERIC(15, 3) NOT NULL,
    accepted_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    rejected_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    pending_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    production_available_quantity NUMERIC(15, 3) NOT NULL DEFAULT 0.000,
    unit VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'QUARANTINED',
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- Initial Sample Data for Testing (Section 7)
-- -----------------------------------------------------------------------------
INSERT INTO suppliers (name, code, contact_email, phone) VALUES
('Acme Metals Ltd', 'SUP-001', 'sales@acmemetals.com', '+1-555-0101'),
('Global Copper Corp', 'SUP-002', 'orders@globalcopper.com', '+1-555-0102'),
('Apex Aluminium Ind', 'SUP-003', 'support@apexaluminium.com', '+1-555-0103')
ON CONFLICT (code) DO NOTHING;

INSERT INTO materials (name, code, unit) VALUES
('Steel', 'MAT-STEEL-01', 'kg'),
('Copper', 'MAT-COPPER-02', 'kg'),
('Aluminium', 'MAT-ALUM-03', 'kg')
ON CONFLICT (code) DO NOTHING;

-- Initial Required Purchase Orders (Section 7)
-- PO-001: Steel, 100 kg, tolerance 5%
-- PO-002: Copper, 200 kg, tolerance 5%
-- PO-003: Aluminium, 150 kg, tolerance 5%
INSERT INTO purchase_orders (po_number, supplier_name, material_name, ordered_quantity, unit, tolerance_percentage, status) VALUES
('PO-001', 'Acme Metals Ltd', 'Steel', 100.000, 'kg', 5.00, 'OPEN'),
('PO-002', 'Global Copper Corp', 'Copper', 200.000, 'kg', 5.00, 'OPEN'),
('PO-003', 'Apex Aluminium Ind', 'Aluminium', 150.000, 'kg', 5.00, 'OPEN')
ON CONFLICT (po_number) DO NOTHING;

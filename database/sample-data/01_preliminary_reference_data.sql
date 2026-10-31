-- =============================================================================
-- RAW MATERIAL RECEIVING YARD & QUALITY INSPECTION MANAGEMENT SYSTEM
-- Preliminary Sample Reference Data for Development and Testing
-- NOTE: Marked strictly as PRELIMINARY until actual supplier/material catalogs arrive.
-- =============================================================================

-- Default Users across manufacturing roles
INSERT INTO users (username, full_name, email, role, is_active) VALUES
('operator.john', 'John Doe (Yard Receiver)', 'john.receiver@example.com', 'WAREHOUSE_OPERATOR', TRUE),
('inspector.sarah', 'Sarah Jenkins (QC Inspector)', 'sarah.qc@example.com', 'QUALITY_INSPECTOR', TRUE),
('manager.robert', 'Robert Vance (QA Manager)', 'robert.qa@example.com', 'QUALITY_MANAGER', TRUE),
('admin.system', 'System Administrator', 'admin@example.com', 'SYSTEM_ADMIN', TRUE)
ON CONFLICT (username) DO NOTHING;

-- Standard Raw Material Suppliers
INSERT INTO suppliers (code, name, contact_email, phone, address, is_active) VALUES
('SUP-CHEM-01', 'Apex Chemical Synthetics Ltd', 'sales@apexchem.example.com', '+1-555-0101', '101 Industrial Pkwy, Sector 4', TRUE),
('SUP-MET-02', 'Titanium Alloys & Metals Corp', 'orders@titaniumalloys.example.com', '+1-555-0102', '45 Foundry Blvd, Industrial Zone', TRUE),
('SUP-POLY-03', 'Prime Polymer Solutions', 'support@primepolymers.example.com', '+1-555-0103', '88 Polymer Way, Suite 12', TRUE)
ON CONFLICT (code) DO NOTHING;

-- Standard Manufacturing Raw Materials
INSERT INTO materials (code, name, description, unit_of_measure, default_tolerance_percent, requires_coa, is_active) VALUES
('MAT-RES-101', 'Industrial Epoxy Resin Grade-A', 'High purity epoxy resin for composite manufacturing', 'KG', 5.00, TRUE, TRUE),
('MAT-TIT-202', 'Titanium Dioxide Powder (TiO2)', 'White pigment and opacifier with minimum 98.5% purity', 'KG', 5.00, TRUE, TRUE),
('MAT-SOL-303', 'High-Purity Acetone Solvent', 'Industrial grade degreasing solvent', 'LITER', 3.00, TRUE, TRUE)
ON CONFLICT (code) DO NOTHING;

-- Quality Specifications for Material MAT-RES-101
INSERT INTO quality_specifications (material_id, parameter_name, parameter_type, min_value, max_value, target_value, unit, is_mandatory, is_active)
SELECT id, 'Viscosity at 25C', 'NUMERIC', 11000.00, 15000.00, 13000.00, 'mPa.s', TRUE, TRUE FROM materials WHERE code = 'MAT-RES-101'
ON CONFLICT DO NOTHING;

INSERT INTO quality_specifications (material_id, parameter_name, parameter_type, min_value, max_value, target_value, unit, is_mandatory, is_active)
SELECT id, 'Epoxide Equivalent Weight (EEW)', 'NUMERIC', 182.00, 192.00, 187.00, 'g/eq', TRUE, TRUE FROM materials WHERE code = 'MAT-RES-101'
ON CONFLICT DO NOTHING;

INSERT INTO quality_specifications (material_id, parameter_name, parameter_type, min_value, max_value, target_value, unit, is_mandatory, is_active)
SELECT id, 'Water Content (Karl Fischer)', 'NUMERIC', 0.00, 0.10, 0.02, '% wt', TRUE, TRUE FROM materials WHERE code = 'MAT-RES-101'
ON CONFLICT DO NOTHING;


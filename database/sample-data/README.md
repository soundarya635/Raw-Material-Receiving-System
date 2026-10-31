# Sample Data & Preliminary Seed Files

## 1. Directory Purpose
This directory stores baseline SQL seed data, reference catalogs, and preliminary test datasets for the **Raw Material Receiving Yard and Quality Inspection Management System**.

## 2. Discovery Audit Note
- During the Phase 1 Data Discovery inspection, **no real proprietary company sample files (Excel, CSV, or CoA PDFs) were present**.
- The preliminary reference file `01_preliminary_reference_data.sql` provides initial user roles, standard manufacturing raw materials (Epoxy Resin, Titanium Dioxide, Acetone), suppliers, and laboratory quality testing specifications.
- **Important**: These records are strictly baseline references and will be updated or replaced once actual supplier and material catalogs are provided by the client.

## 3. Applying Seed Data to PostgreSQL
To manually populate the baseline reference data into the active PostgreSQL database:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -h localhost -p 5433 -U postgres -d receivingyard_db -f "01_preliminary_reference_data.sql"
```


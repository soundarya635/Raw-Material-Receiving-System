# Raw Material Receiving Yard & Quality Inspection Management System

Enterprise-grade manufacturing execution and quality control platform governing raw material gate receiving, Purchase Order line-item reconciliation, strict default quarantine custody, laboratory inspection against inclusive specification limits, authorized quality disposition sign-off, and production inventory release.

---

## 1. Phase 1 Architecture Foundation & Deliverables

This repository implements **Phase 1: Project Foundation, Data Discovery & Architecture**:

1. **Complete Project Directory Structure**: Modular frontend (`React + Vite`), backend (`Java 21 + Spring Boot 3.3.5` under package `com.receivingyard`), database (`PostgreSQL 18 + Flyway`), and documentation.
2. **PostgreSQL Relational Schema Managed by Flyway**: 16 domain tables supporting users, suppliers, materials, purchase orders, goods receipts, lots, CoAs, quality specs, inspections, dispositions, inventory ledger, data source staging, sync history, and audit trails.
3. **Source-Agnostic Ingestion Architecture**: Decoupled 10-stage pipeline and adapter pattern ready to ingest Excel, CSV, PDF, Google Drive/Sheets, and ERP APIs without altering core domain models.
4. **Manufacturing Quality Invariants**:
   - Every newly received lot enters `QUARANTINED` status immediately.
   - Quantity tolerance boundaries are strictly **inclusive** (configurable default $\pm 5\%$).
   - Quality test min/max boundaries are strictly **inclusive**.
   - Mandatory test failures prevent approval of affected quantities.
   - Partial acceptance and rejection supported with strict mass conservation balance.
   - Only approved quantities can be released into active production inventory.
5. **API Health & Verification**: Thin REST controllers, DTO communication layer, global exception handler (`@RestControllerAdvice`), live PostgreSQL probe.
6. **Responsive React Console**: Real-time health status indicator, interactive tolerance validator, receiving intake simulator, and quality inspection station.

---

## 2. Technology Stack

- **Frontend**: React 18, Vite 5, Lucide Icons, Fetch API Client
- **Backend**: Java 21, Spring Boot 3.3.5, Spring Data JPA, Hibernate, Bean Validation
- **Database**: PostgreSQL 18 (Local cluster on port 5433, database `receivingyard_db`)
- **Database Migration**: Flyway 10.x (`V1__initial_schema.sql`)
- **Architecture**: Clean Layered / Hexagonal Adapter Architecture (`com.receivingyard`)
- **API Communication**: RESTful JSON with standardized `ApiResponse<T>` envelopes

---

## 3. Project Directory Structure

```
RawMaterialQC/
├── backend/
│   ├── src/main/java/com/receivingyard/
│   │   ├── config/                  # CORS, Web MVC configuration
│   │   ├── controller/              # HealthCheckController (thin REST controllers)
│   │   ├── service/                 # HealthCheckService interface & implementation
│   │   ├── repository/              # Spring Data JPA repositories (User, Material, Supplier)
│   │   ├── entity/                  # JPA entities (User, Material, Supplier)
│   │   ├── dto/                     # DTOs: ApiResponse, HealthStatusDTO, ErrorResponseDTO
│   │   ├── enums/                   # LotStatus, UserRole, InspectionStatus, DispositionDecision
│   │   ├── exception/               # GlobalExceptionHandler (@RestControllerAdvice)
│   │   ├── integration/             # SourceDataAdapter, DataIngestionPayload (Agnostic)
│   │   └── validation/              # ToleranceCalculator (Inclusive ±5% tolerance checks)
│   ├── src/main/resources/
│   │   ├── application.properties   # Environment-based configuration
│   │   └── db/migration/
│   │       └── V1__initial_schema.sql # 16 Flyway migration tables
│   ├── src/test/java/com/receivingyard/
│   │   └── ToleranceCalculatorTest.java # Unit tests for inclusive bounds
│   ├── pom.xml
│   └── mvnw.cmd
├── frontend/
│   ├── src/
│   │   ├── components/              # HealthStatusBadge, MetricCard, ToleranceChecker
│   │   ├── layouts/                 # MainLayout with navigation & live status
│   │   ├── pages/                   # DashboardPage, ReceivingYardPage, QualityInspectionPage, DataDiscoveryPage
│   │   ├── services/                # api.js REST client
│   │   ├── hooks/                   # useHealthCheck polling hook
│   │   ├── utils/                   # formatters.js (dates, uptime, tolerance evaluation)
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── index.css
│   ├── package.json
│   └── vite.config.js
├── database/
│   ├── schema/
│   │   └── schema.sql               # Canonical DDL schema matching Flyway V1
│   ├── sample-data/
│   │   ├── 01_preliminary_reference_data.sql # Seed users, suppliers, materials, specs
│   │   └── README.md
│   └── pgdata/                      # PostgreSQL data directory (Git-ignored)
├── docs/
│   ├── DATA_SOURCE_ANALYSIS.md      # File discovery analysis, cross-source conflict handling
│   ├── DATA_DICTIONARY.md           # Table and column dictionary with constraints
│   ├── ENTITY_RELATIONSHIPS.md      # Mermaid ER diagram & cardinality specifications
│   ├── ASSUMPTIONS_AND_OPEN_QUESTIONS.md # Core assumptions & client questions
│   └── SYSTEM_ARCHITECTURE.md       # High-level architecture, 10-stage pipeline, workflow
├── start-all.bat                    # One-click Windows startup script
├── start-postgres.bat               # Starts PostgreSQL cluster on port 5433
├── start-backend.bat                # Runs Spring Boot backend on port 8080
├── start-frontend.bat               # Runs Vite frontend on port 5173
└── README.md
```

---

## 4. How to Run Locally

### Quick Start (All Services)
Double-click `start-all.bat` or run:
```powershell
.\start-all.bat
```

### Manual Individual Start
1. **Start PostgreSQL**:
   ```powershell
   & "C:\Program Files\PostgreSQL\18\bin\pg_ctl.exe" -D "database\pgdata" -o "-p 5433" -l "database\postgres.log" start
   ```

2. **Start Spring Boot Backend**:
   ```powershell
   cd backend
   .\mvnw.cmd spring-boot:run
   ```
   *Backend starts on `http://localhost:8080` and automatically runs Flyway migrations.*

3. **Start React Frontend**:
   ```powershell
   cd frontend
   npm.cmd run dev
   ```
   *Frontend starts on `http://localhost:5173` with proxy to backend.*

---

## 5. API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/health` | Comprehensive health check (PostgreSQL status, Flyway migration version, uptime, Java/OS details) |
| `GET` | `/api/status` | Simple backend operational ping |

---

## 6. Data Discovery Audit Summary

- **Inspection Status**: Inspection confirmed that **no proprietary company sample data files (Excel, CSV, PDF, or ERP dumps) were provided in the initial repository**.
- **Compliance**: As required by Phase 1 instructions, no fake company data was fabricated. Preliminary extensible models have been created based on ISA-95 and ISO 9001 receiving standards.
- **Detailed Documentation**: See [docs/DATA_SOURCE_ANALYSIS.md](file:///c:/Users/ASUS/Desktop/RawMaterialQC/docs/DATA_SOURCE_ANALYSIS.md) and [docs/ASSUMPTIONS_AND_OPEN_QUESTIONS.md](file:///c:/Users/ASUS/Desktop/RawMaterialQC/docs/ASSUMPTIONS_AND_OPEN_QUESTIONS.md).


# Warehouse Manager Application — Project Analysis

## Overview

A Java Swing desktop application for warehouse/business management (67 Java source files). Built with Maven, FlatLaf (macOS-themed UI), MySQL, and Hibernate/JPA. Features include user authentication, inventory management, invoicing with PDF export, analytics dashboard with pie/line/bar charts, and a custom animated navigation menu.

**Author**: MinhCreatorVN (original menu system by Raven)

---

## Current Architecture

```
src/main/java/minhcreator/
├── main/Application.java              -- Entry point, JFrame lifecycle, Hibernate init
├── component/
│   ├── page/                          -- Screen-level (Login, Sign_up, Dashboard, etc.)
│   ├── ModularPanel/                  -- Reusable panels (DashPanel, WarehousePanel, etc.)
│   ├── menu/                          -- Custom animated nav menu (by Raven)
│   │   └── mode/                      -- Light/Dark toggle + Accent color picker
│   ├── form/                          -- Base form containers (MainForm, SimpleForm)
│   ├── model/                         -- UI data models (Product, ModelCard)
│   ├── stock/                         -- Stock badge renderer (TableBadgeCellRenderer)
│   ├── Security/                      -- Field validation
│   ├── Card.java, PopUp.java, etc.    -- Reusable Swing widgets
├── service/                           -- Business logic (WarehouseService, orderService)
├── entity/                            -- JPA entities (User, Product, Inventory, etc.)
├── functional/
│   ├── database/                      -- HibernateUtil, DB (legacy JDBC), TableInitializer
│   │   └── dao/                       -- Hibernate DAOs (UserDAO, ProductDAO, InventoryDAO, etc.)
│   ├── session/sessionManager.java    -- User session (userId-based, unified schema)
│   ├── location/TimeManager.java      -- Date/time formatting utilities
│   ├── fontRender/FontManager.java    -- Custom font loading
│   └── imageSupport/imgRender.java    -- Image/SVG rendering
├── debugFeature/                      -- Testing/deprecated code (see §8)
└── util/                              -- Utilities (color, global config, password strength)
```

---

## 1. Naming Conventions — Violations

| Current | Should Be | Severity |
|---|---|---|
| `color.java` | `ColorUtil.java` | High — class names are PascalCase |
| `global.java` | `AppConfig.java` | High |
| `invoicesPanel.java` | `InvoicesPanel.java` | Medium |
| `sessionManager.java` | `SessionManager.java` | Medium |
| `orderService.java` | `OrderService.java` | Medium |
| `orderPanel.java` | `OrderPanel.java` | Medium |
| `imgRender.java` | `ImageRenderer.java` | Medium |
| `StyleIcon.java` | — | Low — consider removing (unused constants) |
| `Security/` package | `security/` | Medium — Java packages are lowercase |
| `get_User_sale_table()` | `getUserSaleTable()` | Medium — camelCase |
| `add_Util_Panel()` | `addUtilPanel()` | Medium |
| `AVAIlABLE` (enum) | `AVAILABLE` | Low — typo |
| `lblTotal` → `lblTotal` | `lblTotal` | Low — misleading label prefix |

---

## 2. Security Issues

### 2.1 Plain-Text Passwords (Critical)
`Login.java:360` — passwords are compared via `user.getPassword().equals(pass)`, and stored as-is in `Sign_up.java`.

```java
// ❌ Current:
if (rs.getString("password").equals(pass))

// ✅ Required:
if (BCrypt.verifyer().verify(password.toCharArray(), hash).verified)
```

### 2.2 Hardcoded Database Credentials (Critical)
`DB.java:17-19`:
```java
private static final String USER = "root";
private static final String PASSWORD = "";
```

The `hibernate.cfg.xml` also duplicates these credentials.

### 2.3 Weak Email/Username Regex
`FieldCheck.java:16-19`: The regex allows `8-9` in character classes (`A-Za-z8-9+_.-]`) which is likely a typo for `0-9`.

```java
// ❌ Contains "8-9" (matches '8', '-', '9' not 0-9)
private String regex = "^\\w+[A-Za-z8-9+_.-]+@[A-Za-z8-9.-]+$";

// ✅ Should be:
private String regex = "^\\w+[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
```

---

## 3. Code Quality Issues

### 3.1 Thread Safety: Static Instance Fields
`CustomDialog.java:161-165` declares instance-specific fields as `static`:
```java
private static String message;
private static String Header;
private static String iconic_path;
private static String buttonText;
private static JFrame parent;
```
If two dialogs are shown concurrently, they will corrupt each other's state.

### 3.2 Static Coupling Between Layers
Multiple classes access `Login.session` directly:
- `WarehouseService.java`
- `orderService.java`
- `DashPanel.java`, `AnalyticsPanel.java`, `WarehousePanel.java`, `invoicesPanel.java`

This couples business logic to Swing component lifecycle, making unit testing impossible without UI initialization.

### 3.3 Empty/Bare Catch Blocks
Several files have empty catch blocks or only print stack traces:
- `Sign_up.java:248`: `catch (Exception e) {}`
- `WarehousePanel.java:275`: `catch (SQLException e) { e.printStackTrace(); }`

### 3.4 Silver Bullet Annotations
`Menu.java` and related menu files have non-standard `@author Raven` / `@author Modified by MinhCreatorVN` javadoc tags. Standardize to `@author Raven (modified by MinhCreatorVN)`.

### 3.5 Unused Imports
Many files have stale imports from the JDBC→Hibernate migration:
- `Login.java`: No longer needs `DB`, `PreparedStatement`, `ResultSet`, `SQLException`
- `WarehousePanel.java`: No longer needs `DB`, `Connection`, `PreparedStatement`, `ResultSet`, `SQLException`
- Various debug files have unused imports

### 3.6 `PasswordStrengthStatus.java` File Naming
The file is correctly named `PasswordStrengthStatus.java` — no typo in the actual filename. The LSP error was misleading.

---

## 4. Architecture

### 4.1 Package Organization
Current packages mix concerns:
```
functional/database/      — HibernateUtil + legacy DB + DAOs
functional/session/       — session manager
functional/location/      — TimeManager (a utility, not location)
debugFeature/             — Dead code, test files, unrelated "Student" app
```

**Suggestion**: Restructure to feature-based packages:
```
product/        — ProductEntity, ProductDAO, ProductPanel, etc.
invoice/        — InvoiceEntity, InvoiceDAO, InvoicesPanel, OrderService
user/           — UserEntity, UserDAO, Login, Sign_up, SessionManager
common/         — HibernateUtil, DB, FieldCheck, shared widgets
```

### 4.2 Layer Separation
The `service/` package still contains UI code:
- `orderService.java` creates `JDialog`, `JTextField`, `JButton` directly inside the service class
- `orderPanel.java` is a `JDialog` subclass but lives in `service/` instead of `component/`

Move `orderPanel.java` → `component/ModularPanel/`.

### 4.3 Dead Code: `debugFeature/`
This folder contains:
- `GUI.java` — An unrelated **Student Enrollment System** app (not warehouse management)
- `FrameTest.java` — Component testing with hardcoded `admin_invoices` table
- `InventoryServiceBackup.java` — Backup of old JDBC service
- `DateCalculator.java` — Unused utility
- `UIManager.java` — UI theme debug
- `functional_debug.java`, `Shared.java`

**Recommendation**: Delete the entire `debugFeature/` folder or extract it into a separate `sandbox/` module for development only.

### 4.4 Hibernate Entity Relationships
Current entities use `int userId` instead of JPA relationships.
```java
// ❌ Current:
@Column(name = "user_id")
private int userId;

// ✅ Preferred:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private UserEntity user;
```
Object references enable `product.getUser().getUsername()` instead of manual user_id lookups.

---

## 5. Build & Dependencies

### 5.1 System-Scope JARs
`pom.xml` has 6 dependencies with `<scope>system</scope>`:
```
library/swing-toast-notifications-1.0.4.jar
library/swing-crazy-panel-1.0.0.jar
library/swing-chart-1.1.0-beta.jar
library/swing-glasspane-popup-1.5.1.jar
library/datechooser-swing-1.4.2.jar
library/TimingFramework-0.55.jar
```
These prevent other developers from building without the exact JARs, and Maven Central doesn't index them.

**Action**: Deploy to local repo or use `mvn install:install-file` as a build step.

### 5.2 Java Version
`<maven.compiler.source>25</maven.compiler.source>` — JDK 25 is bleeding-edge (March 2025). Hibernate 6.6.x and C3P0 are tested on JDK 17-21. Recommend JDK 21 LTS.

### 5.3 Missing Build Plugins
| Plugin | Purpose |
|---|---|
| `maven-surefire-plugin` | Run unit tests (currently 0 tests exist) |
| `maven-assembly-plugin` | Build fat JAR for distribution |
| `maven-compiler-plugin` | Explicit compiler config |

---

## 6. Testing

**Zero unit tests** exist in the project. The `debugFeature/` folder suggests manual testing only.

**Recommended additions**:

| Test Target | What to Test |
|---|---|
| `UserDAOTest` | CRUD operations, findByEmail, findByUsername |
| `WarehouseServiceTest` | processStock (IN/OUT), addNewProduct, deleteProduct |
| `InventoryDAOTest` | getProductsWithInventory, search, sort |
| `LoginTest` | authentication flow with H2 in-memory DB |

**Dependencies needed**:
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
    <scope>test</scope>
</dependency>
```

---

## 7. Database & Schema

### 7.1 Schema Migration
The app was migrated from per-user table names (`{username}_products`) to a unified schema (`products` with `user_id`). A reference migration script exists at `schema/migrate_to_hibernate.sql`.

**Recommendation**: Formalize with Flyway:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>10.0.0</version>
</dependency>
```

### 7.2 Missing Indexes
Add indexes for common query patterns:
```sql
CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_inventory_user_id ON inventory(user_id);
CREATE INDEX idx_invoices_user_id ON invoices(user_id);
CREATE INDEX idx_invoice_details_invoice_id ON invoice_details(invoice_id);
CREATE INDEX idx_purchase_orders_user_id ON purchase_orders(user_id);
CREATE INDEX idx_sales_orders_user_id ON sales_orders(user_id);
```

### 7.3 Hardcoded Table Names in Views
`DashPanel.java:177` still references a hardcoded query:
```java
private void loadTableData() {
    List<PurchaseOrderEntity> orders = purchaseOrderDAO.findByUserId(userId);
    // ...
}
```
✅ This was fixed during the Hibernate upgrade but verify no raw SQL strings remain in panel code.

---

## 8. Code Smells & Technical Debt

### 8.1 Unused/Redundant Code
| File | Issue |
|---|---|
| `StyleIcon.java` | Declares constants (`NEUTRAL`, `PRIMARY`, etc.) that are never used anywhere in the project |
| `color.java` | `ReadColFromFile()` / `WriteColToFile()` use hardcoded `src/main/resources/...` path (not classpath-safe) |
| `PopUp.java` | Two identical constructors with different param names but same logic |
| `FontManager.java` | Extends `Component` unnecessarily (static utility disguised as a component) |
| `DB.java` | Legacy JDBC kept for backward compat, but unused except `TableInitializer` |
| `MethodUtil.java` | `checkPasswordStrength()` — only called from `PasswordStrengthStatus`? Verify usage |

### 8.2 Swing Anti-Patterns
- `Card.java` overrides both `paintComponent()` and `paint()` — the comment says "testing feature and this not be used in main application"
- `orderService.java` creates modal dialogs with `JDialog.setVisible(true)` before adding components (race condition on some L&Fs)
- `WarehousePanel.java` creates row data using deprecated `Vector` (`new Vector<>()`)

### 8.3 i18n: Hardcoded Strings
All UI strings are in English with Vietnamese comments (`// Lưu chi phí`, `// Tính lợi nhuận`). Consider `ResourceBundle`.

---

## 9. Strengths

Despite the issues above, the project has notable strengths:

| Area | Observation |
|---|---|
| **Menu System** | Custom animated menu with RTL support, light/dark toggle, accent colors — professional quality |
| **Hibernate Migration** | Successfully migrated from raw JDBC with per-user tables to JPA entities with unified schema |
| **Chart Integration** | Pie charts, line charts, bar charts from `swing-chart` library with date-range filtering |
| **PDF Export** | iTextPDF-based invoice generation with Vietnamese font support |
| **FlatLaf Theming** | macOS Light/Dark themes, custom theme properties, accent color overrides |
| **DAO Pattern** | Clean separation of data access into DAO classes with typed query methods |
| **Session Handling** | Unified `userId`-based context after Hibernate upgrade |
| **Transaction Management** | Proper commit/rollback in all Hibernate service methods |

---

## 10. Prioritized Action Plan

| # | Task | Effort | Impact | Category |
|---|---|---|---|---|
| 1 | **BCrypt password hashing** | 2h | 🔴 Critical | Security |
| 2 | **Move DB creds to config file** | 1h | 🔴 Critical | Security |
| 3 | **Delete `debugFeature/` folder** | 0.5h | 🟡 Cleanup | Maintainability |
| 4 | **Fix static fields in `CustomDialog`** | 0.5h | 🟡 Correctness | Quality |
| 5 | **Remove unused imports** | 1h | 🟢 Low | Maintainability |
| 6 | **Rename classes per conventions** | 2h | 🟢 Low | Readability |
| 7 | **Add JUnit 5 + H2 tests** | 8h | 🟢 Quality | Testing |
| 8 | **Replace system-scope JARs** | 4h | 🟢 Build | DevOps |
| 9 | **Add Flyway migration** | 4h | 🟢 Data | DevOps |
| 10 | **Add DB indexes** | 1h | 🟢 Performance | Database |
| 11 | **Downgrade to JDK 21 LTS** | 0.5h | 🟢 Stability | Build |
| 12 | **Move `orderPanel` → component/** | 1h | 🟢 Architecture | Quality |
| 13 | **Add entity relationships** | 3h | 🟢 Architecture | Design |
| 14 | **Fix email regex (8-9 bug)** | 0.2h | 🟢 Correctness | Quality |
| 15 | **Replace Vector with ArrayList** | 1h | 🟢 Modernization | Quality |

---

## Summary

The application has a **solid foundation** — professional-grade custom menu system, successful Hibernate migration, clean DAO layer, and modern FlatLaf theming. The main risks are **plain-text passwords**, **hardcoded credentials**, **dead code accumulation**, and **zero test coverage**. Addressing items 1-3 from the action plan would resolve the most critical security and maintainability concerns immediately.

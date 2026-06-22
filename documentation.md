# Developer Documentation — Warehouse Manager

## Architecture Overview

The application follows a **layered architecture** with clear separation of concerns:

```
┌──────────────────────────────────────────┐
│              UI Layer (Swing)             │
│  component/page/*.java                    │
│  component/ModularPanel/*.java           │
│  component/menu/*.java                   │
├──────────────────────────────────────────┤
│           Service Layer                   │
│  service/WarehouseService.java            │
│  service/OrderService.java                │
├──────────────────────────────────────────┤
│           Data Access Layer               │
│  functional/database/dao/*.java (DAOs)   │
├──────────────────────────────────────────┤
│           Persistence Layer               │
│  entity/*.java (JPA entities)             │
│  Hibernate 6.6 + C3P0 connection pool    │
├──────────────────────────────────────────┤
│           Database                        │
│  MySQL 8.0+ (via Hibernate ORM)          │
└──────────────────────────────────────────┘

Cross-cutting:
  util/AppLogger.java     — Centralized logging
  util/ColorUtil.java     — Color generation
  functional/session/     — Session management
  functional/location/    — Date/time utilities
```

## Package Details

### `minhcreator.main`
- **Application.java** — Entry point. Sets up FlatLaf theme, initializes Hibernate, shows login screen.

### `minhcreator.component`
| Sub-package | Description |
|---|---|
| `page/` | Top-level screens (Login, Sign_up, Dashboard, Analytics, invoice, settings, Logs, AppConfigPage) |
| `ModularPanel/` | Reusable panel implementations (DashPanel, WarehousePanel, AnalyticsPanel, invoicesPanel, LogViewerPanel, ConfigPanel) |
| `menu/` | Custom animated menu system (by Raven) with light/dark mode toggle and accent color picker |
| `form/` | Base form layouts (MainForm, SimpleForm) |
| `model/` | UI data models (Product, ModelCard) |
| `stock/` | Stock status badge renderer |
| `Security/` | Field validation utilities |
| Root | Shared widgets (Card, CustomDialog, PopUp, RoundPanel, PasswordStrengthStatus) |

### `minhcreator.service`
- **WarehouseService.java** — Inventory operations: add product, process stock (IN/OUT), update prices
- **OrderService.java** — Shopping cart and checkout with invoice creation

### `minhcreator.entity` (JPA Entities)
| Entity | Table | Key Fields |
|---|---|---|
| `UserEntity` | `users` | id, username, email, password |
| `ProductEntity` | `products` | id, userId, UPID, name |
| `InventoryEntity` | `inventory` | productId, userId, category, price, sellingPrice, quantity |
| `PurchaseOrderEntity` | `purchase_orders` | id, userId, productId, quantity, importPrice, date |
| `SalesOrderEntity` | `sales_orders` | id, userId, productId, quantity, sellingPrice, date |
| `InvoiceEntity` | `invoices` | invoiceId, userId, customerName, totalAmount, createdAt |
| `InvoiceDetailEntity` | `invoice_details` | detailId, userId, invoiceId, productId, quantity, unitPrice |

All entity tables have **indexes on `user_id`** for query performance.

### `minhcreator.functional.database`
- **HibernateUtil.java** — Configures Hibernate SessionFactory from `appConfig.properties`
- **DB.java** — Legacy JDBC connection (kept for backward compatibility)
- **TableInitializer.java** — Creates database tables (delegated to Hibernate `hbm2ddl.auto=update`)
- **dao/** — Data Access Objects for each entity with typed JPQL queries

### `minhcreator.functional.session`
- **SessionManager.java** — Stores current user's id, username, email after login. Used across the app via `Login.session`.

### `minhcreator.util`
| Class | Purpose |
|---|---|
| `AppLogger.java` | Centralized logging with levels (INFO/WARNING/ERROR/DEBUG), in-memory ring buffer, Swing table binding |
| `ColorUtil.java` | Random color generation for charts |
| `AppConfig.java` | Load application configuration from `appConfig.properties` |
| `MethodUtil.java` | Password strength checking |
| `StyleIcon.java` | Icon style constants |

## Database

### Schema
Unified tables with `user_id` foreign key (not per-user table naming).  
Reference: `schema/db.sql`

### Hibernate Configuration
- **Auto DDL**: `hibernate.hbm2ddl.auto=update` — tables/indexes created automatically
- **Dialect**: MySQLDialect
- **Connection pool**: C3P0 (min 2, max 10 connections)
- **Credentials**: Loaded from `appConfig.properties` at runtime (not hardcoded)

### Performance Indexes
```sql
idx_users_email ON users(email)
idx_users_username ON users(username)
idx_products_user_id ON products(user_id)
idx_inventory_user_id ON inventory(user_id)
idx_purchase_orders_user_id ON purchase_orders(user_id)
idx_sales_orders_user_id ON sales_orders(user_id)
idx_invoices_user_id ON invoices(user_id)
idx_invoice_details_invoice_id ON invoice_details(invoice_id)
idx_invoice_details_user_id ON invoice_details(user_id)
```

## Security

- **Passwords**: Hashed with BCrypt (strength factor 12) before storage
- **Legacy migration**: Plain-text passwords detected on login and auto-upgraded to BCrypt
- **DB credentials**: Configurable via `appConfig.properties` (not hardcoded)
- **SQL injection**: Prevented by Hibernate parameterized queries
- **No secrets in code**: All sensitive values in external config

## Configuration

`src/main/resources/minhcreator/config/appConfig.properties`:
```properties
# Window
title=Warehouse Management System
width=1366
height=768

# Database
db.url=jdbc:mysql://localhost/warehouse
db.user=root
db.password=
```

Settings can be edited via **System → Configuration** in the app, or by editing the file directly.

## Build & Deployment

### Commands
```bash
mvn clean compile          # Compile only
mvn clean package          # Build JAR
mvn exec:java -Dexec.mainClass="minhcreator.main.Application"  # Run
```

### Dependencies
| Scope | Count | Notes |
|---|---|---|
| Maven Central | 15 | FlatLaf, Hibernate, MySQL, iText, BCrypt, etc. |
| System (library/) | 6 | JARs not on Maven Central (swing-chart, toast, etc.) |

### System Dependencies
| JAR | Source |
|---|---|
| `swing-toast-notifications-1.0.4.jar` | Local |
| `swing-crazy-panel-1.0.0.jar` | Local |
| `swing-chart-1.1.0-beta.jar` | Local |
| `swing-glasspane-popup-1.5.1.jar` | Local |
| `datechooser-swing-1.4.2.jar` | Local |
| `TimingFramework-0.55.jar` | Local |

## Extending the Application

### Adding a New Menu Item
1. Add entry to `Menu.java` `menuItems[][]` array
2. Create page wrapper in `component/page/`
3. Create panel implementation in `component/ModularPanel/`
4. Handle menu index in `MainForm.java`

### Adding a New Entity
1. Create entity class in `entity/` package with JPA annotations
2. Register entity in `HibernateUtil.java` and `hibernate.cfg.xml`
3. Create DAO in `functional/database/dao/`
4. Create service methods in appropriate service class

### Adding a New DAO Query
1. Add method to existing DAO or create new DAO
2. Use JPQL with named parameters (never string concatenation)
3. Return typed entities or `List<Object[]>` for projections

## Logging

The `AppLogger` class provides centralized logging:
- Thread-safe synchronized ring buffer (max 1000 entries)
- Log levels: INFO, WARNING, ERROR, DEBUG
- Automatically binds to `LogViewerPanel` table
- ERROR level also writes to `System.err`

Usage:
```java
AppLogger.info("MyComponent", "Operation completed successfully");
AppLogger.error("MyComponent", "Failed: " + exception.getMessage());
```

## Troubleshooting

### "No suitable driver" error
Ensure MySQL connector is on classpath. The driver is loaded explicitly in `HibernateUtil.java`.

### "Unable to determine Dialect"
Hibernate couldn't connect to MySQL. Check:
1. MySQL server is running
2. Database `warehouse` exists
3. Credentials in `appConfig.properties` are correct

### "BCrypt" class not found
Run `mvn dependency:copy-dependencies` to ensure BCrypt library is downloaded.

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md) for contribution guidelines.

<h1 align="center">Warehouse Manager</h1>

<p align="center">
  <b>Open-source warehouse & business management desktop application</b><br>
  <b>Java Swing • FlatLaf 3.7 • Hibernate 6.6 • PostgreSQL</b>
</p>

<p align="center">
  <a href="./LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue" alt="MIT License"></a>
  <a href="./CONTRIBUTING.md"><img src="https://img.shields.io/badge/PRs-Welcome-brightgreen" alt="PRs welcome"></a>
</p>

---

## Features

- **User Authentication** — Secure login/signup with BCrypt password hashing
- **Dashboard** — Overview of profit, expenses, stock levels with real-time cards
- **Inventory Management** — Add, edit, delete products with stock tracking
- **Invoice System** — Create invoices, print PDF with Vietnamese font support
- **Analytics** — Pie charts (income/cost/profit), line charts (financial trends), date-range filtering
- **Shopping Cart** — Multi-item checkout with stock validation
- **Light/Dark Mode** — Theme toggle with accent color picker (Material You)
- **Log Viewer** — Real-time application event log with level filtering
- **Configuration GUI** — Edit database connection & app settings from UI
- **PDF Export** — Generate invoices with iTextPDF 5.5

## Screenshots

<p align="center">
  <img width="700" src="./preview/1.png">
  <img width="700" src="./preview/2.png">
  <img width="700" src="./preview/dashboard-screen.png">
  <img width="700" src="./preview/inventory-screen.png">
  <img width="700" src="./preview/invoice-screen.png">
  <img width="700" src="./preview/5.png">
  <img width="700" src="./preview/log-viewer-screen.png">
  <img width="700" src="./preview/setting-screen.png">
</p>

## Technology Stack

| Component        | Technology                          |
| ---------------- | ----------------------------------- |
| Language         | Java 21+                            |
| UI Framework     | Java Swing + FlatLaf 3.7            |
| Build Tool       | Maven                               |
| Database         | PostgreSQL (Supabase)               |
| ORM              | Hibernate 6.6 (JPA 3.1)             |
| Connection Pool  | C3P0 (Hibernate)                    |
| Charts           | swing-chart                         |
| PDF              | iTextPDF 5.5.13                     |
| Password Hashing | BCrypt (at.favre.lib) 0.10.2        |
| SVG Icons        | FlatSVGIcon (jsvg 2.0.0)            |
| Layout           | MiGLayout 11.4, AbsoluteLayout      |
| Date Picker      | swing-datetime-picker 2.1.3         |
| Modal Dialog     | modal-dialog 2.5.2                  |

## Quick Start

### Prerequisites

- JDK 21 or later
- Maven 3.8+
- PostgreSQL 14+ (or a Supabase project)

### Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/MinhCreator/manager-app.git
   cd manager-app
   ```

2. **Create the database**

   Run the Supabase schema script against your PostgreSQL instance:

   ```bash
   psql -U postgres -d postgres < schema/supabase.sql
   ```

3. **Configure database connection**  
   Edit `src/main/resources/minhcreator/config/appConfig.properties`:

   ```properties
   db.url=jdbc:postgresql://<host>:5432/postgres?sslmode=require
   db.user=postgres
   db.password=YOUR_SUPABASE_DB_PASSWORD
   ```

4. **Build and run**
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="minhcreator.main.Application"
   ```

### Build a Runnable JAR

```bash
mvn assembly:single
java -jar target/manager_app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Project Structure

```
src/main/java/minhcreator/
├── main/Application.java              -- Entry point (JFrame)
├── component/                         -- UI components
│   ├── page/                          -- Screens (Login, Dashboard, etc.)
│   ├── ModularPanel/                  -- Reusable panel implementations
│   ├── menu/                          -- Animated navigation menu system
│   ├── form/                          -- Base form containers
│   ├── model/                         -- UI data models (Product, ModelCard)
│   ├── stock/                         -- Stock status & table renderers
│   ├── Security/                      -- Input validation
│   └── ...                            -- Shared widgets (Card, Dialog, etc.)
├── service/                           -- Business logic layer
├── entity/                            -- JPA entities (7 entities)
├── functional/
│   ├── database/                      -- HibernateUtil, DB, DAOs
│   └── session/                       -- Session management
└── util/                              -- Utilities, logger, config, theme
```

## Configuration

All application settings are in `src/main/resources/minhcreator/config/appConfig.properties`:

- Database connection (URL, user, password)
- Window dimensions, title, location, resizability
- Theme selection (material_light / material_dark)
- Can be edited via **Settings → Configuration** in-app

## Documentation

- [User Guide](./instruction.md) — How to use the application
- [Developer Docs](./documentation.md) — Architecture, API, contribution guide
- [Schema](./schema/) — SQL scripts (PostgreSQL/Supabase)

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE).

## Authors

- **Raven** — Original menu system, chart integration
- **MinhCreatorVN** — Application logic, Hibernate migration, UI customization

# Mizan — Budget Management App

A desktop budget management application built with Java Swing as part of CS251: Intro to Software Engineering at Cairo University, Faculty of Computers and AI.

---

## Overview

Mizan helps users track their spending within a defined budget cycle. Users set a total allowance and a date range, log expenses by category, and monitor their daily limit and remaining balance in real time.

---

## Features

- **Budget Cycle Setup** — Define a total allowance with a custom start and end date
- **Expense Logging** — Log expenses with an amount and category
- **Dashboard** — View today's daily limit gauge and a category spending pie chart
- **Expense History** — Browse, filter, edit, and delete past transactions
- **Category Management** — Add and remove expense categories
- **PIN Lock** — Secure the app with a 4-digit PIN (SHA-256 hashed, lockout after 3 failed attempts)
- **Data Persistence** — All data stored in a local SQLite database (`masroofy.db`)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| UI Framework | Java Swing + FlatLaf (dark theme) |
| Charts | JFreeChart |
| Database | SQLite via JDBC (`sqlite-jdbc`) |
| Documentation | JavaDoc |

---

## Project Structure

```
src/
├── app/           # Entry point and application facade (App, Main)
├── control/       # Business logic (AuthManager, CycleManager, ExpenseManager, LimitCalculator, AlertManager)
├── domain/        # Data models (BudgetCycle, Expense, Category, CycleStatus)
├── persistence/   # Database access (DatabaseHelper — Singleton)
├── ui/            # Swing screens (Dashboard, History, Settings, Lock, Setup, ExpenseLogging)
└── util/          # Shared UI utilities (UIFactory, AppColors, GaugePanel, renderers)
docs/              # Generated JavaDoc HTML output
```

---

## Architecture

The app follows a layered architecture:

```
UI Layer  →  App (Facade)  →  Control Layer  →  Persistence Layer
```

- **UI** screens never touch the database directly — all calls go through `App`
- **App** delegates to the appropriate manager (`CycleManager`, `ExpenseManager`, etc.)
- **DatabaseHelper** is a Singleton providing a single shared SQLite connection

---

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven or your IDE's build system
- Dependencies: `sqlite-jdbc`, `flatlaf`, `jfreechart`

### Run
1. Clone the repository
2. Open in IntelliJ IDEA (or your preferred IDE)
3. Build the project
4. Run `app.Main`

The app creates `masroofy.db` in the working directory on first run.

---

## Documentation

JavaDoc pages are pre-generated in the `/docs` folder.  
Open `docs/index.html` in a browser to browse the full API documentation.

---

## Team

| Name | ID |
|---|---|
| Mahmoud Ehab | 20240549 |
| Yomna Abbas | 20242399 |
| Mohammed Ahmed | 20242265|
|Youssef Ahmed | 20242400 |

---

## Course Info

**CS251** — Intro to Software Engineering  
Cairo University, Faculty of Computers and AI  
Winter 2026 — Homework 2

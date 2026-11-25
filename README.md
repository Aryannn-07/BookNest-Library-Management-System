# BookNest — Library Management System

BookNest is a browser-based library management mini project built with Java 17, Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA, MySQL, HTML, CSS and Bootstrap.

## Features

- Dashboard with book-title, member, active-loan and overdue-loan counts.
- Late fine calculation at ₹5 per day after the due date, shown on loan records and dashboard (calculated, not a payment ledger).
- Reservation queue for unavailable titles, expected-return estimates, FIFO readiness when a copy is returned, reserved-copy issue, and cancellation.
- Dashboard doughnut charts for most-borrowed titles and overdue versus on-time active loans.
- Acquisition-date tracking and recent-acquisitions summary for the last 30 days.
- Book catalogue management: create, edit, search, and delete books where allowed.
- ISBN, title, author, category, total-copy and available-copy tracking.
- Member registration and directory.
- Issue available books to registered members with a due date.
- Return issued books and update availability.
- Loan history with all, active, overdue and returned filters.
- Form validation, confirmation prompts, flash messages and empty states.
- Responsive interface with consistent navigation, tables, forms, status labels and dashboard cards.
- Custom design system in `static/css/style.css`: CSS design tokens (colour, type, radius, shadow), active-page navigation highlight, keyboard-visible focus states, reduced-motion support and print-friendly styles.

## Technology stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Backend framework | Spring Boot 3.5.7, Spring MVC |
| View layer | Thymeleaf, HTML5, CSS3, Bootstrap 5 |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL 8.x |
| Build tool | Maven |

## Requirements

- JDK 17 or later
- Maven 3.6+
- MySQL 8.x
- A modern browser

## Setup and run

1. Start MySQL.
2. Create a database user with permission to create/use the project database, or create it manually:

   ```sql
   CREATE DATABASE library_management;
   ```

3. Configure credentials using environment variables. Do not commit real credentials to source control.

   **Windows PowerShell**
   ```powershell
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="your_mysql_password"
   # Optional:
   # $env:DB_URL="jdbc:mysql://localhost:3306/library_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
   ```

   **macOS/Linux**
   ```bash
   export DB_USERNAME=root
   export DB_PASSWORD='your_mysql_password'
   ```

4. Open a terminal in the folder containing `pom.xml`.
5. Run:

   ```bash
   mvn spring-boot:run
   ```

6. Open [http://localhost:8080](http://localhost:8080).

The application uses `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables when provided. Hibernate is configured with `ddl-auto=update` for this educational project.

## Main routes

| Route | Purpose |
|---|---|
| `/` | Dashboard |
| `/books` | Browse and search books |
| `/books/new` | Add a book |
| `/members` | Member directory |
| `/members/new` | Register a member |
| `/loans` | Loan history and status filters |
| `/loans/issue` | Issue a book |
| `/reservations` | Reservation queue |
| `/reservations/new` | Reserve an unavailable title |

## Project structure

```text
src/main/java/com/aryan/library/
├── controller/   # HTTP routes and page models
├── entity/       # JPA entities: Book, Member, BookLoan
├── repository/   # Database access interfaces
├── service/      # Issue/return business rules
└── LibraryManagementApplication.java

src/main/resources/
├── static/css/   # Shared design system (tokens, components, responsive, print)
├── templates/    # Thymeleaf pages and fragments
└── application.properties
```

## Core workflow

1. Add book records and set the number of copies.
2. Register a member.
3. Issue a book to that member and set a due date.
4. The system records the loan and reduces the available-copy count.
5. When the book is returned, the return date is recorded and availability is restored.
6. Active and overdue loans can be reviewed in the loan history.

## Database model

- **Book** (`books`): ISBN, title, author, category, total copies and available copies.
- **Member** (`members`): full name, email and optional phone.
- **BookLoan** (`book_loans`): references one book and one member, plus issue, due and return dates.

A book can have multiple loan records over time. A member can borrow multiple books over time. Each loan references one book and one member.

## Testing checklist

- Add and edit a book; search by title, author, ISBN or category.
- Register a member and verify it appears in the directory.
- Issue an available book and verify the loan record.
- Confirm an unavailable book cannot be issued.
- Return a book and verify status and availability.
- Check active, overdue and returned filters.
- Verify empty states and validation messages.

## Limitations and future scope

This is an academic mini project. It does not currently include user authentication/role-based access, fine payment collection/receipts, email/SMS reminders, automated backups, or deployment configuration. Before public deployment, add authentication and authorization, CSRF/security review, stronger database migration management, automated tests, and production-ready database-fetching patterns. The current Open Session in View setting is retained for compatibility with the current Thymeleaf pages; a production-oriented version should fetch required relations explicitly or map them into DTOs.

## Security note

Keep database passwords out of source control. Use environment variables or a secret manager for local and deployed environments. Do not expose the application publicly without authentication and a security review.


## Added feature notes

- **Late fines:** The application calculates a fine of ₹5 for each calendar day after the due date. Active overdue loans accrue a live estimate; returned loans stop accruing at their return date. Payment collection and fine waivers are not tracked.
- **Reservations:** Members can reserve a title only when its available-copy count is zero. Reservations are queued by request time. When a copy is returned, the oldest waiting reservation becomes ready and that copy is held for it. Staff can issue the held copy or cancel the reservation.
- **Dashboard analytics:** The most-borrowed chart is calculated from recorded loan history. The overdue percentage is overdue active loans divided by all active loans. Recently acquired titles are determined by acquisition date and a rolling 30-day window; existing records with no acquisition date are excluded until a date is set.
- **Acquisition data:** The book form records an acquisition/purchase date. Existing rows may have a null date after schema update, so update historical dates if accurate acquisition reporting is needed.

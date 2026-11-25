# BookNest – Library Management System

A browser-based Library Management System built with **Java 17, Spring Boot, Thymeleaf and MySQL**. BookNest helps manage books, members, book lending, returns, reservations, overdue tracking and library analytics through a responsive web interface.

## Screenshots

### Dashboard
![BookNest Dashboard](Screenshots/Screenshot%20(397).png)

### Application Screenshots

| Screenshot | Preview |
|---|---|
| Screenshot 2 | ![Screenshot 398](Screenshots/Screenshot%20(398).png) |
| Screenshot 3 | ![Screenshot 399](Screenshots/Screenshot%20(399).png) |
| Screenshot 4 | ![Screenshot 400](Screenshots/Screenshot%20(400).png) |
| Screenshot 5 | ![Screenshot 401](Screenshots/Screenshot%20(401).png) |
| Screenshot 6 | ![Screenshot 402](Screenshots/Screenshot%20(402).png) |
| Screenshot 7 | ![Screenshot 403](Screenshots/Screenshot%20(403).png) |
| Screenshot 8 | ![Screenshot 404](Screenshots/Screenshot%20(404).png) |
| Screenshot 9 | ![Screenshot 405](Screenshots/Screenshot%20(405).png) |
| Screenshot 10 | ![Screenshot 406](Screenshots/Screenshot%20(406).png) |

*All screenshots are stored in the `Screenshots/` directory.*

## Features

### Dashboard and Analytics
- Dashboard displaying total books, members, active loans and overdue loans.
- Doughnut charts for most-borrowed titles and overdue versus on-time active loans.
- Recent acquisitions summary covering the last 30 days.

### Book Management
- Add, edit, search and delete books where allowed.
- Track ISBN, title, author, category and copy availability.
- Record acquisition dates.

### Member Management
- Register library members.
- Maintain a member directory with contact details.

### Book Issue and Return
- Issue available books to registered members.
- Set due dates and maintain loan records.
- Record returns and automatically update book availability.
- View all, active, overdue and returned loans.

### Reservation System
- Allow members to reserve unavailable titles.
- Maintain a FIFO reservation queue.
- Estimate expected return dates.
- Automatically mark the oldest waiting reservation as ready when a copy is returned.
- Issue reserved copies or cancel reservations.

### Late Fine Calculation
- Calculate late fines at ₹5 per calendar day after the due date.
- Display calculated fines on loan records and the dashboard.
- Stop fine accumulation when an overdue book is returned.

*Note: Fine calculation is implemented; payment collection and receipts are not.*

### User Interface
- Responsive layout using Bootstrap.
- Consistent navigation, tables, forms and status indicators.
- Form validation, confirmation prompts and flash messages.
- Custom CSS design system with keyboard-visible focus states, reduced-motion support and print-friendly styles.

## Technology Stack

| Layer | Technology |
|---|---|
| Programming Language | Java 17 |
| Backend Framework | Spring Boot 3.5.7 |
| Web Framework | Spring MVC |
| Frontend | Thymeleaf, HTML5, CSS3, Bootstrap 5 |
| Database | MySQL 8.x |
| ORM | Hibernate |
| Persistence | Spring Data JPA |
| Build Tool | Maven |

## Project Structure

```text
library-management-web/
│
├── src/
│   └── main/
│       ├── java/com/aryan/library/
│       │   ├── controller/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── service/
│       │   └── LibraryManagementApplication.java
│       │
│       └── resources/
│           ├── static/css/
│           ├── templates/
│           └── application.properties
│
├── Screenshots/
├── database.sql
├── pom.xml
├── README.md
└── .gitignore
```

## Requirements

- JDK 17 or later
- Maven 3.6+
- MySQL 8.x
- Modern web browser

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/Aryannn-07/BookNest-Library-Management-System.git
```

### 2. Navigate to the project directory

```bash
cd BookNest-Library-Management-System
```

If `pom.xml` is inside a nested `library-management-web` directory, navigate into that directory before continuing.

### 3. Create the MySQL database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE library_management;
```

### 4. Configure database credentials

Set the following environment variables before running the application.

**Windows PowerShell:**

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/library_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

**macOS/Linux:**

```bash
export DB_URL="jdbc:mysql://localhost:3306/library_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="your_mysql_password"
```

Replace the example credentials with your own local MySQL credentials.

**Important:** Never commit real database credentials to GitHub.

### 5. Run the application

Make sure your terminal is in the directory containing `pom.xml`.

```bash
mvn spring-boot:run
```

Alternatively, build the project:

```bash
mvn clean package
```

Then run the generated JAR:

```bash
java -jar target/library-management-web-1.0.0.jar
```

### 6. Open BookNest

Visit:

```text
http://localhost:8080
```

The application uses `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` environment variables when configured. Hibernate currently uses `ddl-auto=update` for this educational project.

## Main Application Routes

| Route | Purpose |
|---|---|
| `/` | Dashboard |
| `/books` | Browse and search books |
| `/books/new` | Add a book |
| `/members` | Member directory |
| `/members/new` | Register a member |
| `/loans` | Loan history and filters |
| `/loans/issue` | Issue a book |
| `/reservations` | Reservation queue |
| `/reservations/new` | Reserve an unavailable title |

## Core Workflow

1. Add books and specify the number of copies.
2. Register library members.
3. Issue available books to members with due dates.
4. Track active and overdue loans.
5. Return books and update availability.
6. Automatically process waiting reservations when copies become available.
7. Monitor fines and borrowing analytics through the dashboard.

## Database Model

| Entity | Description |
|---|---|
| Book (`books`) | ISBN, title, author, category, total copies and available copies |
| Member (`members`) | Full name, email and optional phone |
| BookLoan (`book_loans`) | Book and member references, issue date, due date and return date |

A book can have multiple loan records over time. A member can borrow multiple books over time. Each loan references one book and one member.

## Testing Checklist

- Add, edit and search books.
- Register members and verify the directory.
- Issue available books.
- Verify unavailable books cannot be issued.
- Return books and check availability.
- Test active, overdue and returned loan filters.
- Verify reservation queue ordering and cancellation.
- Check late fine calculations.
- Verify dashboard analytics and validation messages.

## Limitations and Future Scope

BookNest is an academic mini project. The current version does not include:

- User authentication and role-based access control.
- Fine payment collection and receipts.
- Email or SMS notifications.
- Automated database backups.
- Production deployment configuration.

Potential improvements include authentication, authorization, automated testing, database migration management, stronger security controls and production-ready database access patterns.

The current Open Session in View configuration is retained for compatibility with the Thymeleaf pages. A production-oriented version should fetch required relationships explicitly or use DTOs.

## Security

- Keep database credentials outside source control.
- Use environment variables or a secret manager.
- Do not expose the application publicly without authentication and a security review.

---

**Developed as a B.Tech Computer Science and Engineering mini project.**
CREATE DATABASE IF NOT EXISTS library_management;
-- Tables are generated/updated by Spring Data JPA on application startup.
-- Added schema elements: books.acquisition_date (nullable LocalDate) and
-- book_reservations (id, book_id, member_id, reservation_date, status).
-- Fine amounts are calculated from book_loans.due_date and return_date at ₹5/day;
-- this academic version does not include a fine-payment ledger.

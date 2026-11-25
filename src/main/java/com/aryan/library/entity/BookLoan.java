package com.aryan.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "book_loans")
public class BookLoan {
    public static final long FINE_PER_DAY = 5L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false) private LocalDate issueDate;
    @Column(nullable = false) private LocalDate dueDate;
    private LocalDate returnDate;
    public BookLoan() {}
    public BookLoan(Book book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.book = book; this.member = member; this.issueDate = issueDate; this.dueDate = dueDate;
    }
    public boolean isOverdue() { return returnDate == null && dueDate.isBefore(LocalDate.now()); }
    public boolean isReturned() { return returnDate != null; }
    public long getLateDays() {
        LocalDate effectiveDate = returnDate == null ? LocalDate.now() : returnDate;
        if (dueDate == null || !effectiveDate.isAfter(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, effectiveDate);
    }
    public long getFineAmount() { return getLateDays() * FINE_PER_DAY; }
    public Long getId() { return id; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}

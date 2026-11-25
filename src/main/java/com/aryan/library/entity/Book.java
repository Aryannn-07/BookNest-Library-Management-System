package com.aryan.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "ISBN is required") @Column(nullable = false, unique = true)
    private String isbn;
    @NotBlank(message = "Title is required") @Column(nullable = false)
    private String title;
    @NotBlank(message = "Author is required") @Column(nullable = false)
    private String author;
    @NotBlank(message = "Category is required") @Column(nullable = false)
    private String category;
    @Min(value = 1, message = "At least one copy is required")
    private int totalCopies;
    @Min(0)
    private int availableCopies;
    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    public Book() {}
    public Book(String isbn, String title, String author, String category, int totalCopies) {
        this.isbn = isbn; this.title = title; this.author = author; this.category = category;
        this.totalCopies = totalCopies; this.availableCopies = totalCopies; this.acquisitionDate = LocalDate.now();
    }
    @PrePersist
    public void setDefaults() {
        if (acquisitionDate == null) acquisitionDate = LocalDate.now();
        if (availableCopies > totalCopies) availableCopies = totalCopies;
    }
    @PreUpdate
    public void keepCopyCountsValid() {
        if (availableCopies > totalCopies) availableCopies = totalCopies;
    }
    public Long getId() { return id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) {
        int currentlyIssued = this.totalCopies - this.availableCopies;
        this.totalCopies = totalCopies;
        this.availableCopies = Math.max(0, totalCopies - currentlyIssued);
    }
    public LocalDate getAcquisitionDate() { return acquisitionDate; }
    public void setAcquisitionDate(LocalDate acquisitionDate) { this.acquisitionDate = acquisitionDate; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
}

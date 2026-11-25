package com.aryan.library.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "book_reservations")
public class Reservation {
    public static final String WAITING = "WAITING", READY = "READY", FULFILLED = "FULFILLED", CANCELLED = "CANCELLED";
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="book_id") private Book book;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="member_id") private Member member;
    @Column(nullable=false) private LocalDate reservationDate;
    @Column(nullable=false, length=20) private String status = WAITING;
    public Reservation() {}
    public Reservation(Book book, Member member, LocalDate reservationDate, String status) {
        this.book=book; this.member=member; this.reservationDate=reservationDate; this.status=status;
    }
    public Long getId(){return id;} public Book getBook(){return book;} public Member getMember(){return member;}
    public LocalDate getReservationDate(){return reservationDate;} public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
}

package com.aryan.library.service;
import com.aryan.library.entity.*;
import com.aryan.library.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service
public class LibraryService {
    private final BookRepository books; private final MemberRepository members;
    private final BookLoanRepository loans; private final ReservationRepository reservations;
    public LibraryService(BookRepository books, MemberRepository members, BookLoanRepository loans, ReservationRepository reservations) {
        this.books=books; this.members=members; this.loans=loans; this.reservations=reservations;
    }
    @Transactional
    public void issueBook(Long bookId, Long memberId, LocalDate dueDate) {
        Book book=books.findById(bookId).orElseThrow(()->new IllegalArgumentException("Book not found."));
        Member member=members.findById(memberId).orElseThrow(()->new IllegalArgumentException("Member not found."));
        if(book.getAvailableCopies()<1) throw new IllegalArgumentException("No copies are available. You can reserve this title instead.");
        validateDueDate(dueDate); book.setAvailableCopies(book.getAvailableCopies()-1); books.save(book);
        loans.save(new BookLoan(book,member,LocalDate.now(),dueDate));
    }
    @Transactional
    public void returnBook(Long loanId) {
        BookLoan loan=loans.findById(loanId).orElseThrow(()->new IllegalArgumentException("Loan record not found."));
        if(loan.isReturned()) throw new IllegalArgumentException("This book has already been returned.");
        loan.setReturnDate(LocalDate.now()); Book book=loan.getBook();
        List<Reservation> waiting=reservations.findByBookAndStatusOrderByReservationDateAscIdAsc(book,Reservation.WAITING);
        if(!waiting.isEmpty()) { waiting.get(0).setStatus(Reservation.READY); reservations.save(waiting.get(0)); }
        else { book.setAvailableCopies(Math.min(book.getTotalCopies(),book.getAvailableCopies()+1)); books.save(book); }
        loans.save(loan);
    }
    @Transactional
    public void reserveBook(Long bookId, Long memberId) {
        Book book=books.findById(bookId).orElseThrow(()->new IllegalArgumentException("Book not found."));
        Member member=members.findById(memberId).orElseThrow(()->new IllegalArgumentException("Member not found."));
        if(book.getAvailableCopies()>0) throw new IllegalArgumentException("This title is available now. Issue it directly instead of reserving.");
        if(reservations.existsByBookAndMemberAndStatusIn(book,member,List.of(Reservation.WAITING,Reservation.READY)))
            throw new IllegalArgumentException("This member already has an active reservation for this title.");
        reservations.save(new Reservation(book,member,LocalDate.now(),Reservation.WAITING));
    }
    @Transactional
    public void issueReservedBook(Long reservationId, LocalDate dueDate) {
        Reservation r=reservations.findById(reservationId).orElseThrow(()->new IllegalArgumentException("Reservation not found."));
        if(!Reservation.READY.equals(r.getStatus())) throw new IllegalArgumentException("This reservation is not ready for collection.");
        validateDueDate(dueDate); loans.save(new BookLoan(r.getBook(),r.getMember(),LocalDate.now(),dueDate));
        r.setStatus(Reservation.FULFILLED); reservations.save(r);
    }
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation r=reservations.findById(reservationId).orElseThrow(()->new IllegalArgumentException("Reservation not found."));
        if(!Reservation.WAITING.equals(r.getStatus())&&!Reservation.READY.equals(r.getStatus()))
            throw new IllegalArgumentException("Only waiting or ready reservations can be cancelled.");
        Book book=r.getBook(); boolean held=Reservation.READY.equals(r.getStatus()); r.setStatus(Reservation.CANCELLED); reservations.save(r);
        if(held) {
            List<Reservation> waiting=reservations.findByBookAndStatusOrderByReservationDateAscIdAsc(book,Reservation.WAITING);
            if(!waiting.isEmpty()) { waiting.get(0).setStatus(Reservation.READY); reservations.save(waiting.get(0)); }
            else { book.setAvailableCopies(Math.min(book.getTotalCopies(),book.getAvailableCopies()+1)); books.save(book); }
        }
    }
    private void validateDueDate(LocalDate dueDate) {
        if(dueDate==null||dueDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("Due date must be today or in the future.");
    }
    @Transactional
    public void deleteBook(Long id) {
        Book book=books.findById(id).orElseThrow(()->new IllegalArgumentException("Book not found."));
        if(loans.existsByBookAndReturnDateIsNull(book)) throw new IllegalArgumentException("This book has an active loan. Return it before deleting the book.");
        if(loans.existsByBook(book)) throw new IllegalArgumentException("This book has loan history. Keep the record for library history.");
        if(reservations.existsByBook(book))
            throw new IllegalArgumentException("This book has reservation history. Keep the record for library history.");
        books.delete(book);
    }
    @Transactional
    public void deleteMember(Long id) {
        Member member=members.findById(id).orElseThrow(()->new IllegalArgumentException("Member not found."));
        boolean activeLoan=loans.findAll().stream().anyMatch(l->l.getMember().getId().equals(id)&&!l.isReturned());
        if(activeLoan) throw new IllegalArgumentException("This member has an active loan. Return it before deleting the member.");
        if(loans.existsByMember(member)) throw new IllegalArgumentException("This member has loan history. Keep the record for library history.");
        if(reservations.existsByMember(member)) throw new IllegalArgumentException("This member has reservation history. Keep the record for library history.");
        members.delete(member);
    }
}

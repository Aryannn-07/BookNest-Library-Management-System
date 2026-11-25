package com.aryan.library.repository;
import com.aryan.library.entity.Book;
import com.aryan.library.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findAllByOrderByIssueDateDesc();
    long countByReturnDateIsNull();
    long countByReturnDateIsNullAndDueDateBefore(java.time.LocalDate date);
    boolean existsByBookAndReturnDateIsNull(Book book);
    boolean existsByBook(Book book);
    boolean existsByMember(com.aryan.library.entity.Member member);
}

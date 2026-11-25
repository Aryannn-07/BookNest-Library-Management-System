package com.aryan.library.repository;
import com.aryan.library.entity.Book;
import com.aryan.library.entity.Member;
import com.aryan.library.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByOrderByReservationDateDescIdDesc();
    List<Reservation> findByBookAndStatusOrderByReservationDateAscIdAsc(Book book, String status);
    boolean existsByBookAndMemberAndStatusIn(Book book, Member member, Collection<String> statuses);
    long countByStatus(String status);
    boolean existsByBook(Book book);
    boolean existsByMember(Member member);
}

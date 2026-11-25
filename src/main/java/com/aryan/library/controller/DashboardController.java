package com.aryan.library.controller;
import com.aryan.library.entity.*;
import com.aryan.library.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Controller
public class DashboardController {
    private final BookRepository books; private final MemberRepository members; private final BookLoanRepository loans; private final ReservationRepository reservations;
    public DashboardController(BookRepository books,MemberRepository members,BookLoanRepository loans,ReservationRepository reservations){this.books=books;this.members=members;this.loans=loans;this.reservations=reservations;}
    @GetMapping("/") public String dashboard(Model model){
        List<Book> allBooks=books.findAll();List<BookLoan> allLoans=loans.findAllByOrderByIssueDateDesc();LocalDate today=LocalDate.now();
        long overdue=allLoans.stream().filter(l->!l.isReturned()&&l.getDueDate().isBefore(today)).count();
        long active=allLoans.stream().filter(l->!l.isReturned()).count();
        long overduePercent=active==0?0:Math.round(overdue*100.0/active);
        long fines=allLoans.stream().mapToLong(BookLoan::getFineAmount).sum();
        Map<String,Long> counts=allLoans.stream().collect(Collectors.groupingBy(l->l.getBook().getTitle()+" — "+l.getBook().getAuthor(),Collectors.counting()));
        List<Map.Entry<String,Long>> top=counts.entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())).limit(5).toList();
        LocalDate cutoff=today.minusDays(30);
        List<Book> recent=allBooks.stream().filter(b->b.getAcquisitionDate()!=null&&!b.getAcquisitionDate().isBefore(cutoff)).sorted(Comparator.comparing(Book::getAcquisitionDate).reversed()).limit(5).toList();
        long recentCount=allBooks.stream().filter(b->b.getAcquisitionDate()!=null&&!b.getAcquisitionDate().isBefore(cutoff)).count();
        long recentCopies=allBooks.stream().filter(b->b.getAcquisitionDate()!=null&&!b.getAcquisitionDate().isBefore(cutoff)).mapToLong(Book::getTotalCopies).sum();
        model.addAttribute("bookCount",allBooks.size());model.addAttribute("memberCount",members.count());model.addAttribute("issuedCount",active);model.addAttribute("overdueCount",overdue);model.addAttribute("overduePercent",overduePercent);model.addAttribute("currentFineTotal",fines);
        model.addAttribute("recentLoans",allLoans.stream().limit(6).toList());model.addAttribute("topBorrowedLabels",top.stream().map(Map.Entry::getKey).toList());model.addAttribute("topBorrowedValues",top.stream().map(Map.Entry::getValue).toList());
        model.addAttribute("recentAcquisitions",recent);model.addAttribute("recentAcquisitionCount",recentCount);model.addAttribute("recentAcquisitionCopies",recentCopies);
        model.addAttribute("waitingReservations",reservations.countByStatus(Reservation.WAITING));model.addAttribute("readyReservations",reservations.countByStatus(Reservation.READY));
        return "dashboard";
    }
}

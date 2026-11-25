package com.aryan.library.controller;
import com.aryan.library.entity.*;
import com.aryan.library.repository.*;
import com.aryan.library.service.LibraryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationRepository reservations; private final BookRepository books;
    private final MemberRepository members; private final BookLoanRepository loans; private final LibraryService service;
    public ReservationController(ReservationRepository reservations,BookRepository books,MemberRepository members,BookLoanRepository loans,LibraryService service){
        this.reservations=reservations;this.books=books;this.members=members;this.loans=loans;this.service=service;
    }
    @GetMapping public String list(Model model){
        var all=reservations.findAllByOrderByReservationDateDescIdDesc(); Map<Long,LocalDate> expectedDates=new HashMap<>();
        var allLoans=loans.findAll();
        for(Reservation r:all) allLoans.stream().filter(l->l.getBook().getId().equals(r.getBook().getId())&&!l.isReturned()).map(BookLoan::getDueDate).min(LocalDate::compareTo).ifPresent(d->expectedDates.put(r.getId(),d));
        model.addAttribute("reservations",all);model.addAttribute("expectedDates",expectedDates);
        model.addAttribute("waitingCount",reservations.countByStatus(Reservation.WAITING));model.addAttribute("readyCount",reservations.countByStatus(Reservation.READY));
        model.addAttribute("defaultDueDate",LocalDate.now().plusDays(14));model.addAttribute("minDueDate",LocalDate.now());return "reservations/list";
    }
    @GetMapping("/new") public String form(Model model){model.addAttribute("books",books.findAll().stream().filter(b->b.getAvailableCopies()==0).toList());model.addAttribute("members",members.findAll());return "reservations/form";}
    @PostMapping public String create(@RequestParam Long bookId,@RequestParam Long memberId,RedirectAttributes ra){
        try{service.reserveBook(bookId,memberId);ra.addFlashAttribute("success","Reservation placed. The oldest waiting reservation will be marked ready when a copy is returned.");}
        catch(IllegalArgumentException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/reservations";
    }
    @PostMapping("/{id}/issue") public String issue(@PathVariable Long id,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate dueDate,RedirectAttributes ra){
        try{service.issueReservedBook(id,dueDate);ra.addFlashAttribute("success","Reserved book issued successfully.");}
        catch(IllegalArgumentException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/reservations";
    }
    @PostMapping("/{id}/cancel") public String cancel(@PathVariable Long id,RedirectAttributes ra){
        try{service.cancelReservation(id);ra.addFlashAttribute("success","Reservation cancelled.");}
        catch(IllegalArgumentException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/reservations";
    }
}

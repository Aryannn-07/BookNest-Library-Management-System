package com.aryan.library.controller;
import com.aryan.library.repository.BookLoanRepository;
import com.aryan.library.repository.BookRepository;
import com.aryan.library.repository.MemberRepository;
import com.aryan.library.service.LibraryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
@Controller
@RequestMapping("/loans")
public class LoanController {
    private final BookLoanRepository loans; private final BookRepository books; private final MemberRepository members; private final LibraryService service;
    public LoanController(BookLoanRepository loans, BookRepository books, MemberRepository members, LibraryService service) { this.loans=loans; this.books=books; this.members=members; this.service=service; }
    @GetMapping public String list(@RequestParam(required=false, defaultValue="all") String status, Model model) {
        var all=loans.findAllByOrderByIssueDateDesc();
        if ("active".equals(status)) all=all.stream().filter(l -> !l.isReturned()).toList();
        else if ("overdue".equals(status)) all=all.stream().filter(l -> l.isOverdue()).toList();
        else if ("returned".equals(status)) all=all.stream().filter(l -> l.isReturned()).toList();
        model.addAttribute("loans",all); model.addAttribute("status",status); return "loans/list";
    }
    @GetMapping("/issue") public String issueForm(Model model) { model.addAttribute("books",books.findAll().stream().filter(b -> b.getAvailableCopies()>0).toList()); model.addAttribute("members",members.findAll()); model.addAttribute("defaultDueDate",LocalDate.now().plusDays(14)); model.addAttribute("minDueDate",LocalDate.now()); return "loans/issue"; }
    @PostMapping("/issue") public String issue(@RequestParam Long bookId, @RequestParam Long memberId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate dueDate, RedirectAttributes ra) {
        try { service.issueBook(bookId,memberId,dueDate); ra.addFlashAttribute("success","Book issued successfully."); return "redirect:/loans"; }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error",e.getMessage()); return "redirect:/loans/issue"; }
    }
    @PostMapping("/{id}/return") public String returnBook(@PathVariable Long id, RedirectAttributes ra) { try { service.returnBook(id); ra.addFlashAttribute("success","Book returned successfully."); } catch (IllegalArgumentException e) { ra.addFlashAttribute("error",e.getMessage()); } return "redirect:/loans"; }
}

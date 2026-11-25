package com.aryan.library.controller;
import com.aryan.library.entity.Book;
import com.aryan.library.repository.BookRepository;
import com.aryan.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository books; private final LibraryService service;
    public BookController(BookRepository books, LibraryService service){this.books=books;this.service=service;}
    @GetMapping public String list(@RequestParam(required=false,defaultValue="") String q,Model model){model.addAttribute("books",q.isBlank()?books.findAll():books.search(q));model.addAttribute("q",q);return "books/list";}
    @GetMapping("/new") public String createForm(Model model){model.addAttribute("book",new Book());model.addAttribute("pageTitle","Add Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());return "books/form";}
    @PostMapping public String create(@Valid @ModelAttribute Book book,BindingResult result,RedirectAttributes ra,Model model){
        if(result.hasErrors()){model.addAttribute("pageTitle","Add Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());return "books/form";}
        try{book.setAvailableCopies(book.getTotalCopies());if(book.getAcquisitionDate()==null)book.setAcquisitionDate(LocalDate.now());books.save(book);ra.addFlashAttribute("success","Book added successfully.");return "redirect:/books";}
        catch(DataIntegrityViolationException e){model.addAttribute("pageTitle","Add Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());model.addAttribute("error","That ISBN is already registered.");return "books/form";}
    }
    @GetMapping("/{id}/edit") public String editForm(@PathVariable Long id,Model model){model.addAttribute("book",books.findById(id).orElseThrow());model.addAttribute("pageTitle","Edit Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());return "books/form";}
    @PostMapping("/{id}") public String update(@PathVariable Long id,@Valid @ModelAttribute Book form,BindingResult result,RedirectAttributes ra,Model model){
        if(result.hasErrors()){model.addAttribute("pageTitle","Edit Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());return "books/form";}
        Book existing=books.findById(id).orElseThrow();existing.setIsbn(form.getIsbn());existing.setTitle(form.getTitle());existing.setAuthor(form.getAuthor());existing.setCategory(form.getCategory());existing.setTotalCopies(form.getTotalCopies());if(form.getAcquisitionDate()!=null)existing.setAcquisitionDate(form.getAcquisitionDate());
        try{books.save(existing);ra.addFlashAttribute("success","Book updated successfully.");return "redirect:/books";}
        catch(DataIntegrityViolationException e){model.addAttribute("pageTitle","Edit Book");model.addAttribute("defaultAcquisitionDate",LocalDate.now());model.addAttribute("error","That ISBN is already registered.");model.addAttribute("book",existing);return "books/form";}
    }
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id,RedirectAttributes ra){try{service.deleteBook(id);ra.addFlashAttribute("success","Book deleted.");}catch(IllegalArgumentException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/books";}
}

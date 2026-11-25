package com.aryan.library.controller;
import com.aryan.library.entity.Member;
import com.aryan.library.repository.MemberRepository;
import com.aryan.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository members; private final LibraryService service;
    public MemberController(MemberRepository members, LibraryService service) { this.members=members; this.service=service; }
    @GetMapping public String list(Model model) { model.addAttribute("members",members.findAll()); return "members/list"; }
    @GetMapping("/new") public String form(Model model) { model.addAttribute("member",new Member()); return "members/form"; }
    @PostMapping public String create(@Valid @ModelAttribute Member member, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) return "members/form";
        try { members.save(member); ra.addFlashAttribute("success","Member added successfully."); return "redirect:/members"; }
        catch (DataIntegrityViolationException e) { model.addAttribute("error","That email is already registered."); return "members/form"; }
    }
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes ra) { try { service.deleteMember(id); ra.addFlashAttribute("success","Member deleted."); } catch (IllegalArgumentException e) { ra.addFlashAttribute("error",e.getMessage()); } return "redirect:/members"; }
}

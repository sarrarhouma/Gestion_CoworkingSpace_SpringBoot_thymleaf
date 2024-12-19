package com.poly.gestioncoworkingspace.controllers;

import com.poly.gestioncoworkingspace.entities.Member;
import com.poly.gestioncoworkingspace.services.MemberService;
import com.poly.gestioncoworkingspace.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SubscriptionService subscriptionService;

    // Méthode de recherche avec pagination par mot-clé (mc)
    @GetMapping("/user/search")
    public String getMembersByMC(@RequestParam(name = "mc", defaultValue = "") String mc,
                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                 Model model) {
        var members = memberService.getMembersByMC(mc, PageRequest.of(page - 1, size));
        model.addAttribute("members", members.getContent());
        model.addAttribute("pageNumbers", members.getTotalPages() > 0 ? new int[members.getTotalPages()] : null);
        model.addAttribute("currentPage", members.getNumber());
        model.addAttribute("mc", mc);

        return "Memberslist"; // Vue pour la liste des membres
    }

    // Afficher tous les membres (sans recherche ni pagination)
    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "Memberslist"; // Vue dans templates/members/list.html
    }

    // Afficher le formulaire d'ajout d'un nouveau membre
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        return "AddMember"; // Vue dans templates/members/add.html
    }

    // Ajouter un nouveau membre
    @PostMapping("/admin/add")
    public String addMember(@ModelAttribute Member member) {
        memberService.addMember(member);
        return "redirect:/members";
    }

    // Afficher le formulaire de modification d'un membre
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        return "addMember"; // Vue dans templates/members/edit.html
    }

    // Modifier un membre existant
    @PostMapping("/admin/edit/{id}")
    public String editMember(@PathVariable Long id, @ModelAttribute Member member) {
        memberService.updateMember(id, member);
        return "redirect:/members";
    }

    // Supprimer un membre
    @GetMapping("/admin/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}

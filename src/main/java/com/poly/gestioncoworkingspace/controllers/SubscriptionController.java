package com.poly.gestioncoworkingspace.controllers;

import com.poly.gestioncoworkingspace.entities.Subscription;
import com.poly.gestioncoworkingspace.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // Méthode de recherche avec pagination par mot-clé (mc)
    @GetMapping("/user/search")
    public String getSubscriptionsByMC(@RequestParam(name = "mc", defaultValue = "") String mc,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size,
                                       Model model) {
        var subscriptions = subscriptionService.getSubscriptionsByMC(mc, PageRequest.of(page - 1, size));
        model.addAttribute("subscriptions", subscriptions.getContent());
        model.addAttribute("pageNumbers", subscriptions.getTotalPages() > 0 ? new int[subscriptions.getTotalPages()] : null);
        model.addAttribute("currentPage", subscriptions.getNumber());
        model.addAttribute("mc", mc);

        return "SubscriptionList"; // Vue pour la liste des abonnements
    }

    // Afficher tous les abonnements (sans recherche ni pagination)
    @GetMapping
    public String listSubscriptions(Model model) {
        model.addAttribute("subscriptions", subscriptionService.getAllSubscriptions());
        return "SubscriptionList"; // templates/subscriptions/list.html
    }

    // Afficher le formulaire d'ajout d'un nouvel abonnement
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("subscription", new Subscription());
        return "AddSubscription"; // templates/subscriptions/add.html
    }

    // Ajouter un nouvel abonnement
    @PostMapping("/admin/add")
    public String addSubscription(@ModelAttribute Subscription subscription) {
        subscriptionService.addSubscription(subscription);
        return "redirect:/subscriptions";
    }

    // Afficher le formulaire de modification d'un abonnement
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("subscription", subscriptionService.getSubscriptionById(id));
        return "AddSubscription"; // templates/subscriptions/edit.html
    }

    // Modifier un abonnement existant
    @PostMapping("/admin/edit/{id}")
    public String editSubscription(@PathVariable Long id, @ModelAttribute Subscription subscription) {
        subscriptionService.updateSubscription(id, subscription);
        return "redirect:/subscriptions";
    }

    // Supprimer un abonnement
    @GetMapping("/admin/delete/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return "redirect:/subscriptions";
    }
}

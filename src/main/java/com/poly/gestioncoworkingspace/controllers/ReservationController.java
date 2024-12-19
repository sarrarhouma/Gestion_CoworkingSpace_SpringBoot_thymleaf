package com.poly.gestioncoworkingspace.controllers;

import com.poly.gestioncoworkingspace.entities.Reservation;
import com.poly.gestioncoworkingspace.services.MemberService;
import com.poly.gestioncoworkingspace.services.MeetingRoomService;
import com.poly.gestioncoworkingspace.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final MeetingRoomService meetingRoomService;

    // Méthode de recherche avec pagination par mot-clé (mc)
    @GetMapping("/user/search")
    public String getReservationsByMC(@RequestParam(name = "mc", defaultValue = "") String mc,
                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "5") int size,
                                      Model model) {
        var reservations = reservationService.getReservationsByMC(mc, PageRequest.of(page - 1, size));
        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("pageNumbers", reservations.getTotalPages() > 0 ? new int[reservations.getTotalPages()] : null);
        model.addAttribute("currentPage", reservations.getNumber());
        model.addAttribute("mc", mc);

        return "ReservationList"; // Vue pour la liste des réservations
    }

    // Afficher la liste des réservations (sans recherche ni pagination)
    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "ReservationList"; // templates/reservations/list.html
    }

    // Afficher le formulaire pour ajouter une réservation
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("meetingRooms", meetingRoomService.getAllRooms());
        return "AddReservation"; // templates/reservations/add.html
    }

    // Ajouter une nouvelle réservation
    @PostMapping("/admin/add")
    public String addReservation(@ModelAttribute Reservation reservation) {
        reservationService.addReservation(reservation);
        return "redirect:/reservations";
    }

    // Afficher le formulaire pour modifier une réservation
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("meetingRooms", meetingRoomService.getAllRooms());
        return "AddReservation"; // templates/reservations/edit.html
    }

    // Modifier une réservation existante
    @PostMapping("/admin/edit/{id}")
    public String editReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        reservationService.updateReservation(id, reservation);
        return "redirect:/reservations";
    }

    // Supprimer une réservation
    @GetMapping("/admin/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}

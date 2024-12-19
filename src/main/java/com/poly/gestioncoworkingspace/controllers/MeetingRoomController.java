package com.poly.gestioncoworkingspace.controllers;

import com.poly.gestioncoworkingspace.entities.MeetingRoom;
import com.poly.gestioncoworkingspace.services.MeetingRoomService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    // Méthode avec pagination et recherche par mot-clé (mapping distinct)
    @GetMapping("/user/search")
    public String getRoomsByMC(@RequestParam(name = "mc", defaultValue = "") String mc,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size,
                               Model model) {
        var rooms = meetingRoomService.getRoomsByMC(mc, PageRequest.of(page - 1, size));
        model.addAttribute("rooms", rooms.getContent());
        model.addAttribute("pageNumbers", rooms.getTotalPages() > 0 ? new int[rooms.getTotalPages()] : null);
        model.addAttribute("currentPage", rooms.getNumber());
        model.addAttribute("mc", mc);
        return "RoomList"; // Template RoomList.html
    }

    @GetMapping
    public String listMeetingRooms(Model model) {
        model.addAttribute("rooms", meetingRoomService.getAllRooms());
        return "RoomList"; // Template RoomList.html
    }


    // Afficher le formulaire d'ajout d'une nouvelle salle
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new MeetingRoom());
        return "addRoom"; // Vue dans templates/meeting-rooms/add.html
    }

    // Ajouter une nouvelle salle
    @PostMapping("/admin/add")
    public String addMeetingRoom(@ModelAttribute MeetingRoom room) {
        meetingRoomService.addRoom(room);
        return "redirect:/rooms";
    }

    // Afficher le formulaire de modification d'une salle
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MeetingRoom room = meetingRoomService.getRoomById(id);
        model.addAttribute("room", room);
        return "addRoom"; // Correspond au fichier addRoom.html dans templates
    }

    // Modifier une salle existante
    @PostMapping("/admin/edit/{id}")
    public String editMeetingRoom(@PathVariable Long id, @ModelAttribute MeetingRoom room) {
        room.setId(id); // Associe l'ID existant pour éviter la création d'une nouvelle salle
        meetingRoomService.updateRoom(id, room);
        return "redirect:/rooms";
    }

    // Supprimer une salle
    @GetMapping("/admin/delete/{id}")
    public String deleteMeetingRoom(@PathVariable Long id) {
        meetingRoomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}
